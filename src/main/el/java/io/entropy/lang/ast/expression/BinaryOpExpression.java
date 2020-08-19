package io.entropy.lang.ast.expression;

import io.entropy.lang.ast.SingleExpression;

public class BinaryOpExpression extends SingleExpression {
    SingleExpression left;
    SingleExpression right;
    BinaryOp op;

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

    public BinaryOp getOp() {
        return op;
    }

    public void setOp(BinaryOp op) {
        this.op = op;
    }

    @Override
    public String toString() {
        return left.toString()+op.op+right.toString();
    }
}
