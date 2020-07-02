package antlr.graphql.ast;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class FragmentSpread extends Node {
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
}
