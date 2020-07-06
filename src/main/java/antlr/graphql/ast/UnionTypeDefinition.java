package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class UnionTypeDefinition extends TypeDefinition {
    String name;
    List<Directive> directives = new ArrayList<>();
    List<Type> unionMembers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    public List<Type> getUnionMembers() {
        return unionMembers;
    }

    public void setUnionMembers(List<Type> unionMembers) {
        this.unionMembers = unionMembers;
    }

    @Override
    protected void accept0(GraphqlAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,directives);
            acceptChild(visitor,unionMembers);
        }
        visitor.endVisit(this);
    }
}
