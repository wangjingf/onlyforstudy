package io.entropy.lang.ast.literal;

import io.entropy.lang.ast.Literal;

public class StringLiteral extends Literal {
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value+"";
    }
}
