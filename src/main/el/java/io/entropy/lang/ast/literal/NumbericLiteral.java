package io.entropy.lang.ast.literal;

import io.entropy.lang.ast.Literal;

public class NumbericLiteral extends Literal {
    Number value;

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value+"";
    }
}
