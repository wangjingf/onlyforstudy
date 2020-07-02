package antlr.graphql.ast;

public class VariableReference extends Value<String> {
    String name;

    public VariableReference(String text) {
        this.name = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public void setValue(String value) {

    }
}
