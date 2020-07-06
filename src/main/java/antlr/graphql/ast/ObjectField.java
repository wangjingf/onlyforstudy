package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
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

    @Override
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
