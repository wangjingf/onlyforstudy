package io.entropy.lang.ast.expression;

import io.entropy.lang.ast.ASTNode;
import io.entropy.lang.ast.SingleExpression;

public class MemberDotExpression extends SingleExpression {
    SingleExpression parent;
    String propName;
    boolean allowUndefine;

    public SingleExpression getParent() {
        return parent;
    }

    public void setParent(SingleExpression parent) {
        this.parent = parent;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public boolean isAllowUndefine() {
        return allowUndefine;
    }

    public void setAllowUndefine(boolean allowUndefine) {
        this.allowUndefine = allowUndefine;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(parent.toString());
        if(isAllowUndefine()){
            sb.append("?");
        }
        sb.append(".");
        sb.append(propName);
        return sb.toString();
    }
}
