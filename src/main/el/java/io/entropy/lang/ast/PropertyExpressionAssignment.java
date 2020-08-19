package io.entropy.lang.ast;

public class PropertyExpressionAssignment extends PropertyAssignment {
    String propName;
    SingleExpression value ;

    @Override
    public String getPropName() {
        return propName;
    }

    @Override
    public void setPropName(String propName) {
        this.propName = propName;
    }

    @Override
    public SingleExpression getValue() {
        return value;
    }

    @Override
    public void setValue(SingleExpression value) {
        this.value = value;
    }
}
