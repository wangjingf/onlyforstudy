package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;

import java.util.List;

public class EnumValue extends Value<List<String>> {
        String name;

    public EnumValue(String text) {
        this.name = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
         }
        visitor.endVisit(this);
    }
}
