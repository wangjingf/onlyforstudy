package antlr.graphql.ast;
import antlr.graphql.Node;

public abstract class TypeDefinition extends TypeSystemDefinition {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
