package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.schema.TypeDefinitions;

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
    protected void accept0(GraphQLAstVisitor visitor) {

        if(visitor.visit(this)){
            acceptChild(visitor,directives);
        }
        visitor.endVisit(this);
    }

    public List<Field> getFields(TypeDefinitions definitions){
        FragmentDefinition frag = definitions.getFragment(getFragmentName());
        List<Field> allFields = frag.getSelectionSet().getAllFields(definitions);
        for (Field field : allFields) { // 将frag上的字段放到directive上
            field.getDirectives().addAll(getDirectives());
        }
        return allFields;
    }
}
