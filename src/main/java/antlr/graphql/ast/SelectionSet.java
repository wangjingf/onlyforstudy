package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class SelectionSet extends Node {
     List<Selection> selections = new ArrayList<>();

    public List<Selection> getSelections() {
        return selections;
    }

    public void setSelections(List<Selection> selections) {
        this.selections = selections;
    }

    @Override
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
