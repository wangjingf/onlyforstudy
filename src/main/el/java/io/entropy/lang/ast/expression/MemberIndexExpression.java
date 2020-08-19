package io.entropy.lang.ast.expression;

import io.entropy.lang.ast.SingleExpression;

public class MemberIndexExpression extends SingleExpression {
    SingleExpression parent;
    SingleExpression prop;

    public SingleExpression getParent() {
        return parent;
    }

    public void setParent(SingleExpression parent) {
        this.parent = parent;
    }

    public SingleExpression getProp() {
        return prop;
    }

    public void setProp(SingleExpression prop) {
        this.prop = prop;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(parent.toString());

        sb.append("[");
        sb.append(prop);
        sb.append("]");
        return sb.toString();
    }
}
