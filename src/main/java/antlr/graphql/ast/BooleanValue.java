package antlr.graphql.ast;

public class BooleanValue extends Value<Boolean> {
    Boolean value;

    public BooleanValue(Boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
}
