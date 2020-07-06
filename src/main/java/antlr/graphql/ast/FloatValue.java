package antlr.graphql.ast;

import antlr.g4.GraphqlAstVisitor;

public class FloatValue extends Value<Float> {
    Float value;

    public FloatValue(Float value) {
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
