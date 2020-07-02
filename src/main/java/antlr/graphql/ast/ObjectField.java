package antlr.graphql.ast;
import antlr.graphql.Node;

public class ObjectField extends Node {
    String name;
    Value value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
