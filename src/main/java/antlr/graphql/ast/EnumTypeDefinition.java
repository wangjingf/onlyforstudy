package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class EnumTypeDefinition extends TypeDefinition {
    String name;
    List<Directive> directives =  new ArrayList<>();
    List<EnumValueDefinition> enumValueDefinitions = new ArrayList<>();

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

    public List<EnumValueDefinition> getEnumValueDefinitions() {
        return enumValueDefinitions;
    }

    public void setEnumValueDefinitions(List<EnumValueDefinition> enumValueDefinitions) {
        this.enumValueDefinitions = enumValueDefinitions;
    }

    @Override
    protected void accept0(GraphqlAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,enumValueDefinitions);
            acceptChild(visitor,directives);
        }
        visitor.endVisit(this);
    }
}
