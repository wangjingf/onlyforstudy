package antlr.graphql.ast;
import antlr.graphql.Node;

public class VariableDefinition extends Node {
    String variable = null;
    Type type = null;
    Value defaultValue = null;

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Value getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Value defaultValue) {
        this.defaultValue = defaultValue;
    }
}
