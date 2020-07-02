package antlr.graphql.ast;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class FragmentDefinition extends Definition {
    String name;
    String typeCondition = null;
    List<Directive> directives = new ArrayList<>();
    SelectionSet selectionSet = null;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeCondition() {
        return typeCondition;
    }

    public void setTypeCondition(String typeCondition) {
        this.typeCondition = typeCondition;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    public SelectionSet getSelectionSet() {
        return selectionSet;
    }

    public void setSelectionSet(SelectionSet selectionSet) {
        this.selectionSet = selectionSet;
    }
}
