package antlr.graphql.ast;
import antlr.graphql.Node;

public class TypeName extends Type {
    String name;

    public TypeName(String text) {
        this.name =text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
