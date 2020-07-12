package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;

import java.util.ArrayList;
import java.util.List;

public class InterfaceTypeDefinition extends TypeDefinition {
    List<Directive> directives = new ArrayList<>();
    List<FieldDefinition> definitions = new ArrayList<>();



    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    public List<FieldDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<FieldDefinition> definitions) {
        this.definitions = definitions;
    }

    @Override
    protected void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,directives);
            acceptChild(visitor,definitions);
        }
        visitor.endVisit(this);
    }
}
