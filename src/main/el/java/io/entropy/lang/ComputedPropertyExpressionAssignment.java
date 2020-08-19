package io.entropy.lang;

import io.entropy.lang.ast.PropertyAssignment;
import io.entropy.lang.ast.SingleExpression;

public class ComputedPropertyExpressionAssignment extends PropertyAssignment {
    SingleExpression propName;
    SingleExpression value;

    public SingleExpression getPropName() {
        return propName;
    }

    public void setPropName(SingleExpression propName) {
        this.propName = propName;
    }

    public SingleExpression getValue() {
        return value;
    }

    public void setValue(SingleExpression value) {
        this.value = value;
    }
}
