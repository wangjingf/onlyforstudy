package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

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
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
