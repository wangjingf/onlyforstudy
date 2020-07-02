package antlr.graphql.ast;
import antlr.graphql.Node;

public class NonNullType extends Type {
    Type type = null;

    public NonNullType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
