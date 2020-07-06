package antlr.graphql.ast;

import antlr.g4.GraphqlAstVisitor;

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

    @Override
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
