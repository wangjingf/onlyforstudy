package io.entropy.lang.ast.expression;

import io.entropy.lang.ast.SingleExpression;
import io.study.util.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParenthesizedExpression extends SingleExpression {
    List<SingleExpression> exprs = new ArrayList<>();

    public List<SingleExpression> getExprs() {
        return exprs;
    }

    public void setExprs(List<SingleExpression> exprs) {
        this.exprs = exprs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        List<String> collect = exprs.stream().map(vs -> vs.toString()).collect(Collectors.toList());
        sb.append(StringHelper.join(collect,","));
        sb.append(")");
        return sb.toString();
    }
}
