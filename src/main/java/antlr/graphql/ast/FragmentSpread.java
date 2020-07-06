package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class FragmentSpread extends Selection {
    String fragmentName;
    List<Directive> directives = new ArrayList<>();

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    @Override
    protected void accept0(GraphqlAstVisitor visitor) {

        if(visitor.visit(this)){
            acceptChild(visitor,directives);
        }
        visitor.endVisit(this);
    }
}
