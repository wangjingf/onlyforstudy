package antlr.graphql;

import antlr.g4.GraphQLAstVisitor;
import antlr.g4.ListGraphQLAstVisitor;
import antlr.g4.graphql.GraphqlLexer;
import antlr.g4.graphql.GraphqlParser;
import antlr.graphql.ast.*;
import antlr.graphql.schema.Environment;
import antlr.graphql.schema.GraphQLSchema;
import antlr.graphql.schema.TypeDefinitions;
import antlr.graphql.schema.entity.*;
import antlr.graphql.schema.entity.directive.IDirectiveResolver;
import antlr.graphql.schema.type.GraphQLScalarType;
import antlr.graphql.schema.validate.KnownTypeValidator;
import compiler.expr.SemanticsException;
import compiler.expr.SyntaxException;
import io.study.exception.StdException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GraphQlParser {

    static final Logger logger = LoggerFactory.getLogger(GraphqlParser.class);
    String schema;
    public GraphQlParser(String schema){
        this.schema = schema;
    }
    public void dumpToken(){

        AstBuildVisitor visitor = new AstBuildVisitor();
        GraphqlLexer lexer = new GraphqlLexer(CharStreams.fromString(schema));
        Map<String,Integer> map =  lexer.getTokenTypeMap();
        Map<Integer,String> tokenMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            tokenMap.put(entry.getValue(),entry.getKey());
        }
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Token token1 = tokens.LT(1);

        while (token1.getType() != Token.EOF){
            System.out.println(tokenMap.get(token1.getType())+"::"+token1.getText());
            tokens.consume();
            token1 = tokens.LT(1);
        }
    }

    /**
     * 缺少语义校验了呢
     * 语义校验如何处理？
     * @return
     */
    public Document parseDocument(){
        AstBuildVisitor visitor = new AstBuildVisitor();
        GraphqlLexer lexer = new GraphqlLexer(CharStreams.fromString(schema));
        CommonTokenStream tokens = new CommonTokenStream(lexer);


        GraphqlParser parser = new GraphqlParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        // ParseTree tree =  parser.document();
        try{
            GraphqlParser.DocumentContext document = parser.document();
            return (Document) visitor.visitDocument(document);
        }catch (ParseCancellationException exception){
            Throwable e = exception.getCause();
            if(e instanceof RecognitionException){
                RecognitionException recognitionException = (RecognitionException) e;
                Token offendingToken = recognitionException.getOffendingToken();
                int line = offendingToken.getLine();
                int column = offendingToken.getCharPositionInLine();
                offendingToken.getType();

                throw new SyntaxException("at line"+line+" column "+column+" expected is "+recognitionException.getExpectedTokens().toString(VocabularyImpl.fromTokenNames(parser.getTokenNames())));
            }
            throw StdException.adapt(exception.getCause());
        }


    }

    /**
     * 解析文档，得到所有类型的定义
     * @param document
     * @return
     */
    public TypeDefinitions parseSchema(Document document){
        TypeDefinitions typeDefinitions = new TypeDefinitions();
        addTypeDefinition(document,typeDefinitions);//注册所有的类型定义
        validateDocument(document,typeDefinitions);
        requireRootType(document,typeDefinitions);
        return typeDefinitions;
    }

    private void requireRootType(Document document, TypeDefinitions typeDefinitions) {
        String queryType = null;
        String mutationType = null;
        for (Definition definition : document.getDefinitions()) {
            if("Query".equals(definition) && definition instanceof ObjectTypeDefinition && queryType == null){
                queryType =  ((ObjectTypeDefinition) definition).getName();
            }
            if("Mutation".equals(definition) && definition instanceof ObjectTypeDefinition && mutationType == null){
                mutationType  = ((ObjectTypeDefinition) definition).getName();
            }
            if(definition instanceof SchemaDefinition){
                for (OperationTypeDefinition operationTypeDefinition : ((SchemaDefinition) definition).getOperationTypeDefinitions()) {
                    if(Operation.QUERY.equals(operationTypeDefinition.getOperation())){
                        queryType = operationTypeDefinition.getTypeName();
                    }
                    if(Operation.MUTATION.equals(operationTypeDefinition.getOperation())){
                        mutationType = operationTypeDefinition.getTypeName();
                    }
                }
            }
        }
        if(queryType == null || mutationType == null){
            if(queryType == null)
                throw new SemanticsException("miss root query type");
            if(mutationType == null)
                throw new SemanticsException("miss root mutation type");
        }
        typeDefinitions.setQueryType(typeDefinitions.getObjectTypes().get(queryType));
        typeDefinitions.setQueryType(typeDefinitions.getObjectTypes().get(mutationType));
    }

    public GraphQLSchema makeExecutionSchema(Document document, Environment environment){
        TypeDefinitions typeDefinitions = parseSchema(document);
        validateDirectives(document,environment);
        for (Map.Entry<String, GraphQLScalarType> entry : typeDefinitions.getScalarTypeMap().entrySet()) {
            if(!environment.containsScalarType(entry.getKey())){
                throw new SemanticsException("miss scalar type definition:"+entry.getKey());
            }

        }
        typeDefinitions.getScalarTypeMap().putAll(typeDefinitions.getScalarTypeMap());
        return new GraphQLSchema(typeDefinitions,environment);
    }

    private void validateDirectives(Document document,Environment environment) {
        document.accept(new GraphQLAstVisitor(){
            @Override
            public boolean visit(Directive directive) {
                IDirectiveResolver directiveResolver = environment.getDirectiveResolver(directive.getName());
                if(directiveResolver == null){
                    throw new SemanticsException("graphql.err_unknown_directive").param("directive",directive.getName());
                }
                directiveResolver.validate(directive);
                return super.visit(directive);
            }
        });
    }


    void addTypeDefinition(Document document,TypeDefinitions typeDefinitions){
        InnerTypeDefinitions d = new InnerTypeDefinitions();
        document.accept(new GraphQLAstVisitor(){
            @Override
            public boolean visit(FragmentDefinition fragmentDefinition) {
                d.fragments.put(fragmentDefinition.getName(),fragmentDefinition);
                return super.visit(fragmentDefinition);
            }

            @Override
            public boolean visit(EnumTypeDefinition enumTypeDefinition) {
                if(d.enums.containsKey(enumTypeDefinition.getName())){
                    throw  new SemanticsException("already exist enum type "+enumTypeDefinition.getName());
                }
                d.enums.put(enumTypeDefinition.getName(),enumTypeDefinition);
                return true;
            }

            @Override
            public boolean visit(InputObjectTypeDefinition type) {
                if(d.inputs.containsKey(type.getName())){
                    throw  new SemanticsException("already exist enum type "+type.getName());
                }
                d.inputs.put(type.getName(),type);
                return super.visit(type);
            }

            @Override
            public boolean visit(UnionTypeDefinition type) {
                if(d.unions.containsKey(type.getName())){
                    throw  new SemanticsException("already exist enum type "+type.getName());
                }
                d.unions.put(type.getName(),type);
                return super.visit(type);
            }

            @Override
            public boolean visit(ScalarTypeDefinition scalarTypeDefinition) {
                if(d.scalars.contains(scalarTypeDefinition.getName())){
                    throw  new SemanticsException("already exist enum type "+scalarTypeDefinition.getName());
                }
                d.scalars.add(scalarTypeDefinition.getName());
                return super.visit(scalarTypeDefinition);
            }

            @Override
            public boolean visit(InterfaceTypeDefinition type) {
                if(d.interfaces.containsKey(type.getName())){
                    throw  new SemanticsException("already exist enum type "+type.getName());
                }
                d.interfaces.put(type.getName(),type);
                return super.visit(type);
            }

            @Override
            public boolean visit(ObjectTypeDefinition type) {
                if(d.objects.containsKey(type.getName())){
                    throw  new SemanticsException("already exist enum type "+type.getName());
                }
                d.objects.put(type.getName(),type);
                return super.visit(type);
            }
        });
        d.init(typeDefinitions);
    }

    void validateDocument(Document document,TypeDefinitions typeDefinitions){
        List<GraphQLAstVisitor> validators = new ArrayList<>();
        validators.add(new KnownTypeValidator().getValidatorVisitor(document,typeDefinitions));
        ListGraphQLAstVisitor listGraphQLAstVisitor = new ListGraphQLAstVisitor(validators);
        document.accept(listGraphQLAstVisitor);
        if(!listGraphQLAstVisitor.getVisitErrors().isEmpty()){
            for (Throwable throwable : listGraphQLAstVisitor.getVisitErrors()) {
                logger.error(throwable.getMessage());
            }
            throw StdException.adapt(listGraphQLAstVisitor.getVisitErrors().get(0));
        }
    }
    static class InnerTypeDefinitions{
        Map<String,EnumTypeDefinition> enums = new HashMap<>();
        List<String> scalars = new LinkedList<>();
        Map<String,FragmentDefinition> fragments = new HashMap<>();
        Map<String,InputObjectTypeDefinition> inputs = new HashMap<>();
        Map<String,UnionTypeDefinition> unions = new HashMap<>();
        Map<String,InterfaceTypeDefinition> interfaces = new HashMap<>();
        Map<String,ObjectTypeDefinition> objects = new HashMap<>();
        public void init(TypeDefinitions typeDefinitions) {
            for (Map.Entry<String, EnumTypeDefinition> entry : enums.entrySet()) {
                EnumType type = new EnumType(entry.getKey(),entry.getValue().getEnumValueDefinitions());
                type.setDirectives(entry.getValue().getDirectives());
                typeDefinitions.registerType(type);
            }
            for (String scalar : scalars) {
                typeDefinitions.getScalarTypeMap().put(scalar,null);
            }
            for (Map.Entry<String, InterfaceTypeDefinition> entry : interfaces.entrySet()) {
                InterfaceType interfaceType = new InterfaceType(entry.getKey());
                InterfaceTypeDefinition value = entry.getValue();
                interfaceType.setDirectives(value.getDirectives());
                for (FieldDefinition definition : value.getDefinitions()) { //字段的子类型还未注册
                    interfaceType.registerField(toField(definition)); //field的类型还未注册。
                }
                typeDefinitions.registerType(interfaceType);
            }
            for (Map.Entry<String, InputObjectTypeDefinition> entry : inputs.entrySet()) {
                InputType input = new InputType();
                InputObjectTypeDefinition value = entry.getValue();
                input.setDirectives(value.getDirectives());
                for (InputValueDefinition valueDefinition : value.getInputValueDefinition()) {
                    FieldInfo fieldInfo = toField(valueDefinition);
                    input.registerField(fieldInfo);
                }
                typeDefinitions.registerType(input);
            }
            for (Map.Entry<String, ObjectTypeDefinition> entry : objects.entrySet()) {
                ObjectTypeDefinition value = entry.getValue();
                Map<String,FieldInfo> fields = new HashMap<>();
                ObjectType type = new ObjectType(entry.getKey(),fields);
                for (FieldDefinition definition : value.getFieldDefinition()) {
                    fields.put(definition.getName(),toField(definition));
                }
                type.setDirectives(value.getDirectives());
                for (String interfaceName :value.getImplementsInterfaces()){
                    if(!typeDefinitions.containsInterfaceType(interfaceName)){
                        throw new SemanticsException("miss interface:"+interfaceName);
                    }
                    type.getSuperTypes().add(typeDefinitions.getInterfaces().get(interfaceName));
                }
                type.init();
                typeDefinitions.registerType(type);
            }
            for (Map.Entry<String, UnionTypeDefinition> entry : unions.entrySet()) {
                UnionType unionType = new UnionType();
                UnionTypeDefinition value = entry.getValue();
                unionType.setDirectives(value.getDirectives());
                for (Type type : value.getUnionMembers()) {
                    ObjectType objectType = typeDefinitions.getObjectTypes().get(type.getName());
                    if(objectType == null){
                        throw new SemanticsException("miss union child type:"+type.getName());
                    }
                    unionType.getTypes().put(objectType.getName(),objectType);
                }
                typeDefinitions.registerType(unionType);
            }
            typeDefinitions.getFragments().putAll(fragments);
            typeDefinitions.init();
        }
        public FieldInfo toField(FieldDefinition definition){
            FieldInfo field = new FieldInfo();
            field.setName(definition.getName());
            Type type = definition.getType();
            field.setType(type);
            field.setArguments(definition.getArgumentsDefinition());
            field.setDirectives(definition.getDirectives());
            return field;
        }
        public FieldInfo toField(InputValueDefinition definition){
            FieldInfo field = new FieldInfo();
            field.setName(definition.getName());
            Type type = definition.getType();
            field.setType(type);
            field.setDefaultValue(definition.getDefaultValue());
            field.setDirectives(definition.getDirectives());
            return field;
        }
    }
}
