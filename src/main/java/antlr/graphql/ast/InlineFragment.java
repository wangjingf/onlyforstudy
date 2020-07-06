package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class InlineFragment extends Selection {
    String typeCondition = null;
    List<Directive> directives = new ArrayList<>();
    SelectionSet selectionSet = null;

    public String getTypeCondition() {
        return typeCondition;
    }

    public void setTypeCondition(String  typeCondition) {
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

    @Override
    protected void accept0(GraphqlAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,directives);
            acceptChild(visitor,selectionSet);
        }
        visitor.endVisit(this);
    }
}
