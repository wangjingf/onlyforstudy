package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;

import java.util.ArrayList;
import java.util.List;

public class ArrayValue extends Value<List<Value>> {
    List<Value> values = new ArrayList<>();

    public List<Value> getValue() {
        return values;
    }

    public void setValue(List<Value> values) {
        this.values = values;
    }

    @Override
    public void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,values);
        }
        visitor.endVisit(this);
    }
}
