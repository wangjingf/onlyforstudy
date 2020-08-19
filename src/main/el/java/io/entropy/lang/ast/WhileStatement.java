package io.entropy.lang.ast;

import io.study.util.StringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WhileStatement extends Statement {
    List<SingleExpression> conditions = new ArrayList<>();
    Statement body;

    public List<SingleExpression> getConditions() {
        return conditions;
    }

    public void setConditions(List<SingleExpression> conditions) {
        this.conditions = conditions;
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
    }
    @Override
    public String toString() {
        String template = "while({{conditions}}){{{body}}}";
        Map<String,Object> vars  = new HashMap<>();
        vars.put("body",body.toString());
        List<String> list = conditions.stream().map(vs -> vs.toString()).collect(Collectors.toList());
        vars.put("conditions",StringHelper.join(list,","));
        return StringHelper.format(template,vars);
    }
}
