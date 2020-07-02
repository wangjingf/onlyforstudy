package antlr.graphql.ast;

public class StringValue extends Value<String> {
    String value;

    public StringValue(String text) {
        this.value = text;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
