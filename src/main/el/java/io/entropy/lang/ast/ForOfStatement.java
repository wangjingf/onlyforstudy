package io.entropy.lang.ast;

import io.study.util.StringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ForOfStatement extends Statement {
    List<SingleExpression> initExprs ;
    List<SingleExpression> iterableExprs ;
    List<VariableDeclaration> declarations = new ArrayList<>();

    public List<SingleExpression> getInitExprs() {
        return initExprs;
    }

    public void setInitExprs(List<SingleExpression> initExprs) {
        this.initExprs = initExprs;
    }

    public List<SingleExpression> getIterableExprs() {
        return iterableExprs;
    }

    public void setIterableExprs(List<SingleExpression> iterableExprs) {
        this.iterableExprs = iterableExprs;
    }

    public List<VariableDeclaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<VariableDeclaration> declarations) {
        this.declarations = declarations;
    }
    @Override
    public String toString() {
       return null;
    }
}
