package antlr.graphql.ast;

import antlr.g4.GraphqlAstVisitor;

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

    @Override
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
