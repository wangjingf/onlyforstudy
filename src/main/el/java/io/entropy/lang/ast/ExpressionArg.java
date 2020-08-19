package io.entropy.lang.ast;

public class ExpressionArg extends Argument {
    SingleExpression expr;

    public SingleExpression getExpr() {
        return expr;
    }

    public void setExpr(SingleExpression expr) {
        this.expr = expr;
    }

    @Override
    public String toString() {
        return expr.toString();
    }
}
