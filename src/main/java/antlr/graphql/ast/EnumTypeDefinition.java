package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;

import java.util.ArrayList;
import java.util.List;

public class EnumTypeDefinition extends TypeDefinition {
    List<Directive> directives =  new ArrayList<>();
    List<EnumValueDefinition> enumValueDefinitions = new ArrayList<>();


    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    public List<EnumValueDefinition> getEnumValueDefinitions() {
        return enumValueDefinitions;
    }

    public void setEnumValueDefinitions(List<EnumValueDefinition> enumValueDefinitions) {
        this.enumValueDefinitions = enumValueDefinitions;
    }

    @Override
    protected void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,enumValueDefinitions);
            acceptChild(visitor,directives);
        }
        visitor.endVisit(this);
    }
}
