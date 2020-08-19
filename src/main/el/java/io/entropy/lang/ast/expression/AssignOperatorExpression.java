package io.entropy.lang.ast.expression;

import io.entropy.lang.ast.SingleExpression;

public class AssignOperatorExpression extends SingleExpression {
    SingleExpression left;
    SingleExpression right;
    AssignOperator assignOp;

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

    public AssignOperator getAssignOp() {
        return assignOp;
    }

    public void setAssignOp(AssignOperator assignOp) {
        this.assignOp = assignOp;
    }

    @Override
    public String toString() {
        return left.toString()+assignOp.op+right.toString();
    }
}
