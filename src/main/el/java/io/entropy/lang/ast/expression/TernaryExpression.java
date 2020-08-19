package io.entropy.lang.ast.expression;


import io.entropy.lang.ast.SingleExpression;

public class TernaryExpression extends SingleExpression {
    SingleExpression condition;
    SingleExpression trueExpr;
    SingleExpression falseExpr;

    public SingleExpression getCondition() {
        return condition;
    }

    public void setCondition(SingleExpression condition) {
        this.condition = condition;
    }

    public SingleExpression getTrueExpr() {
        return trueExpr;
    }

    public void setTrueExpr(SingleExpression trueExpr) {
        this.trueExpr = trueExpr;
    }

    public SingleExpression getFalseExpr() {
        return falseExpr;
    }

    public void setFalseExpr(SingleExpression falseExpr) {
        this.falseExpr = falseExpr;
    }

    @Override
    public String toString() {
        return condition.toString()+"?"+trueExpr.toString()+":"+falseExpr.toString();
    }
}
