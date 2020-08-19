package io.entropy.lang.ast.expression;

import io.entropy.lang.ast.SingleExpression;

public class IdentifierExpression extends SingleExpression {
    String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }
}
