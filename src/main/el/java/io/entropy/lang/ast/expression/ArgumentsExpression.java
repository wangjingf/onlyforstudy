package io.entropy.lang.ast.expression;

import io.entropy.lang.ast.Argument;
import io.entropy.lang.ast.SingleExpression;
import io.study.util.StringHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ArgumentsExpression extends SingleExpression {
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
    public String toString(){
        StringBuilder sb = new StringBuilder();
        List<String> list = new LinkedList<>();
        for (Argument argument : arguments) {
            list.add(argument.toString());
        }
        sb.append(StringHelper.join(list,"," ));
        return sb.toString();
    }
}
