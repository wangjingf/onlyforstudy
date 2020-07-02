package antlr.graphql.ast;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class ArrayValueWithVariable extends Node {
    List<ValueWithVariable> valueWithVariables = new ArrayList<>();

    public List<ValueWithVariable> getValueWithVariables() {
        return valueWithVariables;
    }

    public void setValueWithVariables(List<ValueWithVariable> valueWithVariables) {
        this.valueWithVariables = valueWithVariables;
    }
}
