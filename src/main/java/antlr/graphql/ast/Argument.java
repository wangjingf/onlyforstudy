package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

public class Argument extends Node {
    String name;
    Value value  = null;

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
        if(visitor.visit(this)){
            acceptChild(visitor,value);
        }
        visitor.endVisit(this);
    }
}
