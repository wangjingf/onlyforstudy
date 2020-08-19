package io.entropy.lang.ast.expression;

import io.entropy.lang.ast.SingleExpression;

public class AssignExpression extends SingleExpression {
    SingleExpression left;
    SingleExpression right;

    public SingleExpression getLeft() {
        return left;
    }

    public void setLeft(SingleExpression left) {
        this.left = left;
    }

    public SingleExpression getRight() {
        return right;
    }

    public void setRight(SingleExpression right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return
                  left +
                "=" + right;
    }
}
