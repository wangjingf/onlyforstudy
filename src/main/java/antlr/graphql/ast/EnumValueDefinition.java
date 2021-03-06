package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class EnumValueDefinition extends Node {
    String enumValue = null;
    List<Directive> directives = new ArrayList<>();

    public String getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    @Override
    public void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,directives);
        }
        visitor.endVisit(this);
    }
}
