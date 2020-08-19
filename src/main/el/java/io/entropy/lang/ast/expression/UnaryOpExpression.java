package io.entropy.lang.ast.expression;

import io.entropy.lang.ast.SingleExpression;

public class UnaryOpExpression extends SingleExpression {
    UnaryOp op;
    SingleExpression expression;

    public UnaryOp getOp() {
        return op;
    }

    public void setOp(UnaryOp op) {
        this.op = op;
    }

    public SingleExpression getExpression() {
        return expression;
    }

    public void setExpression(SingleExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(op.isBefore){
            builder.append(op.op);
        }
        builder.append(expression.toString());
        if(!op.isBefore){
            builder.append(op.op);
        }
        return builder.toString();
    }
}
