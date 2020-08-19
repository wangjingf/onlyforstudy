package io.entropy.lang.ast.literal;

import io.entropy.lang.ast.Literal;

public class BooleanLiteral extends Literal {
    Boolean value;

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value+"";
    }
}
