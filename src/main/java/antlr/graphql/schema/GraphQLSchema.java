package antlr.graphql.schema;

import antlr.graphql.GraphQlParser;
import antlr.graphql.ast.Definition;
import antlr.graphql.ast.Directive;
import antlr.graphql.ast.Document;
import antlr.graphql.ast.OperationDefinition;
import antlr.graphql.schema.data.DefaultDataFetcher;
import antlr.graphql.schema.data.FetcherContext;
import antlr.graphql.schema.data.IDataFetcher;
import antlr.graphql.schema.entity.SchemaOperation;
import antlr.graphql.schema.entity.FieldInfo;
import antlr.graphql.schema.entity.directive.DirectiveContext;
import antlr.graphql.schema.entity.directive.IDirectiveResolver;
import compiler.expr.SemanticsException;
import io.study.lang.Guard;

import java.util.*;

public class GraphQLSchema {
    TypeDefinitions typeDefinitions = null;
    Environment environment = null;

    public GraphQLSchema(TypeDefinitions typeDefinitions, Environment environment) {
        this.typeDefinitions = typeDefinitions;
        this.environment = environment;
    }

    public Object query(String schema, Map<String, Object> args, Object rootData) {
        GraphQlParser parser = new GraphQlParser(schema);
        Document document = parser.parseDocument();
        List<SchemaOperation> operations = new ArrayList<>();
        for (Definition definition : document.getDefinitions()) {
            Guard.assertTrue(definition instanceof OperationDefinition, "graph.err_invalid_schema");
            SchemaOperation operation = SchemaOperation.from((OperationDefinition) definition, typeDefinitions);
            operations.add(operation);
        }
        if(operations.size() > 1){
            for (SchemaOperation operation : operations) {
                if(operation.getName() == null){
                    throw new SemanticsException("multi operation name is required:");
                }
            }
        }
        Map<String,Object> ret = new HashMap<>();
        for (SchemaOperation operation : operations) {
            Map<String, Object> data = new LinkedHashMap<>();
            Map<String, Object> vars = operation.resolveVars(args); //根据args重新获取新的变量
            for (FieldInfo selectionField : operation.getFields()) {
                FetcherContext context = new FetcherContext(typeDefinitions);
                context.addPath(selectionField.getName());

                Object o = fetchData(rootData, typeDefinitions.getQueryType().getName(), context, selectionField, vars);
                data.put(selectionField.getName(), o);
                executeDirective(selectionField,vars,data,context);
            }
            ret.put(operation.getName(),data);
        }

        return ret;
    }
    void executeDirective(FieldInfo fieldInfo, Map<String,Object> args,Map<String,Object> values,FetcherContext fetcherContext){
        if(fieldInfo.getDirectives().isEmpty()){
            return ;
        }
        for (Directive directive : fieldInfo.getDirectives()) {
            IDirectiveResolver directiveResolver = environment.getDirectiveResolver(directive.getName());
            DirectiveContext context = new DirectiveContext(args,values,fieldInfo);
            context.setFetcherContext(fetcherContext);
            directiveResolver.execute(directive,context);
        }
    }
    public Object fetchData(Object parentValue, String parentTypeName, FetcherContext context, FieldInfo field, Map<String, Object> args) {
        Map<String, IDataFetcher> map = environment.getDataFetcher(parentTypeName);
        Guard.notEmpty(map, "graphql.err_miss_data_fetcher:" + parentTypeName);
        IDataFetcher dataFetcher = null;
        dataFetcher = map.get(field.getName());
        if (dataFetcher == null) {
            dataFetcher = new DefaultDataFetcher();
        }
        Object o = dataFetcher.fetchData(parentValue, field, args, context); //获取到当前字段的数据
        if (field.getChildFields().isEmpty()) {//没有子节点了
            return o;
        }
        if (field.getType().isListType()) {
            List list = (List) o;
            List ret = new LinkedList();
            for (Object o1 : list) {
                Map<String, Object> m = fetchSubData(o1,field,context,args);
                ret.add(m);
            }
            return ret;
        } else {

            return fetchSubData(o,field,context,args);
        }

    }
    Map<String,Object> fetchSubData(Object parentValue, FieldInfo field,FetcherContext context,Map<String,Object> args){
        Map<String,Object> m = new HashMap<>();
        for (Map.Entry<String, FieldInfo> entry : field.getChildFields().entrySet()) {
            FieldInfo child = entry.getValue();
            FetcherContext copy = context.copy();
            copy.addPath(entry.getKey());
            Object childData = fetchData(parentValue, field.getType().getPrimitiveTypeName(), copy, child,args);
            String name = child.getName();
            if(child.getAlias() != null){
                name = child.getAlias();
            }
            m.put(name, childData);
            executeDirective(child,args,m,context);
        }
        return m;
    }
}
