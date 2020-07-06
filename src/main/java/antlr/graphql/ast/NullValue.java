package antlr.graphql.ast;

import antlr.g4.GraphqlAstVisitor;

public class NullValue extends Value<Object> {
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }

    @Override
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
