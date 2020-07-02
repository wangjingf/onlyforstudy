package antlr.graphql.ast;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class ListType extends Type {
    Type type = null;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
