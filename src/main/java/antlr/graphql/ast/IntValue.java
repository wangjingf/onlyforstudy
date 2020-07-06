package antlr.graphql.ast;

import antlr.g4.GraphqlAstVisitor;

public class IntValue extends Value<Integer> {
    Integer value;

    public IntValue(Integer value) {
        this.value = value;
    }

    @Override
   public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
