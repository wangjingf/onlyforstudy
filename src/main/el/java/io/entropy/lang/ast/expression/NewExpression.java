package io.entropy.lang.ast.expression;

import io.entropy.lang.ast.Argument;
import io.entropy.lang.ast.SingleExpression;
import io.study.util.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewExpression extends SingleExpression {
    SingleExpression expression = null;
    List<Argument> arguments = new ArrayList<>();

    public SingleExpression getExpression() {
        return expression;
    }

    public void setExpression(SingleExpression expression) {
        this.expression = expression;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new ");

        sb.append(expression.toString());
        if(!arguments.isEmpty()){
            sb.append("(");
            List<String> collect = arguments.stream().map(vs -> {
                return vs.toString();
            }).collect(Collectors.toList());
            sb.append(StringHelper.join(collect,","));
            sb.append(")");
        }
        return sb.toString();
    }
}
