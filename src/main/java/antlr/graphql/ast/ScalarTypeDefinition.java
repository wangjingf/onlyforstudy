package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class ScalarTypeDefinition extends TypeDefinition {
    String name;
    List<Directive> directives = new ArrayList<>();

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

    @Override
    protected void accept0(GraphqlAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,directives);
        }
        visitor.endVisit(this);
    }
}
