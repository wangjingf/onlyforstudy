package io.entropy.lang.ast;

import java.util.ArrayList;
import java.util.List;

public class ForStatement extends IterationStatement {
    List<SingleExpression> initExprs ;
    List<SingleExpression> conditions ;
    List<SingleExpression> nextExprs ;
    List<VariableDeclaration> declarations = new ArrayList<>();

    public List<SingleExpression> getInitExprs() {
        return initExprs;
    }

    public void setInitExprs(List<SingleExpression> initExprs) {
        this.initExprs = initExprs;
    }

    public List<SingleExpression> getConditions() {
        return conditions;
    }

    public void setConditions(List<SingleExpression> conditions) {
        this.conditions = conditions;
    }

    public List<SingleExpression> getNextExprs() {
        return nextExprs;
    }

    public void setNextExprs(List<SingleExpression> nextExprs) {
        this.nextExprs = nextExprs;
    }

    public List<VariableDeclaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<VariableDeclaration> declarations) {
        this.declarations = declarations;
    }
}
