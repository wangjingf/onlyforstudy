package antlr.graphql.ast;

public class NullValue extends Value<Object> {
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }
}
