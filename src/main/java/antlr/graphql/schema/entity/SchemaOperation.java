package antlr.graphql.schema.entity;

import antlr.graphql.TypeHelper;
import antlr.graphql.ast.*;
import antlr.graphql.schema.TypeDefinitions;
import antlr.graphql.schema.type.GraphQLScalarType;
import compiler.expr.SemanticsException;
import io.study.exception.StdException;

import java.util.*;

public class SchemaOperation {
    Operation operation;
    String name;
    List<VariableDefinition> variables = new ArrayList<>();
    List<Directive> directives = new ArrayList<>();
    List<FieldInfo> fields = new ArrayList<>();

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VariableDefinition> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableDefinition> variables) {
        this.variables = variables;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    /**
     * 获取变量的值，顺便执行类型转换、值变量转换
     * @param args
     * @return
     */
    public Map<String,Object> resolveVars(Map<String,Object> args){
        Map<String,Object> ret = new HashMap<>();
        for (VariableDefinition definition : this.getVariables()) {
            Object value = args.get(definition.getVariable());
            if(definition.getType().isNonNullType()){
                if(value == null){
                    throw new SemanticsException("miss demand variable:").param("variable",definition.getVariable());
                }
            }
            if(value != null){
                value = TypeHelper.parseValue(value, definition.getType());
            }
            ret.put(definition.getVariable(), value);
        }
        return ret;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public void setFields(List<FieldInfo> fields) {
        this.fields = fields;
    }
    public static SchemaOperation from(OperationDefinition definition, TypeDefinitions definitions){
        ObjectType rootType = definitions.getQueryType() ;
        SchemaOperation operation = new SchemaOperation();
        operation.setName(definition.getName());
        operation.setOperation(definition.getOperation());
        for (VariableDefinition variable : definition.getVariables()) {// 更换变量为实际的类型
            Type type = definitions.requireImplType(variable.getType());
            variable.setType(type);
            operation.getVariables().add(variable);
        }
        operation.setVariables(definition.getVariables());
        operation.setDirectives(definition.getDirectives());
        definition.getSelectionSet().getAllFields(definitions);
        operation.validate(definitions);
        return operation;
    }

    private void validate(TypeDefinitions definitions) {
        for (FieldInfo field : fields) {
            if(!field.getType().hasChild()){
                throw new SemanticsException("object type mus has child").param("type",field.getType());
            }
        }
        for (VariableDefinition definition : getVariables()) {
            String typeName = definition.getType().getPrimitiveTypeName();
            if(definitions.containsScalarType(typeName) || definitions.containsInputType(name)){ //标量类型
                throw new SemanticsException("invalid input type,args type must be scalar type or input type").param("type",typeName);
            }
        }
    }

}
