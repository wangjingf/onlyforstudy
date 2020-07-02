package antlr.graphql.ast;
import antlr.graphql.Node;

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
}
