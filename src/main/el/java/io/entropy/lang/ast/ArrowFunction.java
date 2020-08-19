package io.entropy.lang.ast;

import io.study.util.StringHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArrowFunction extends FunctionExpression {
    SingleExpression returnExpr;

    public SingleExpression getReturnExpr() {
        return returnExpr;
    }

    public void setReturnExpr(SingleExpression returnExpr) {
        this.returnExpr = returnExpr;
    }

    @Override
    public String toString() {
        String template = "({{params}})=>{{{body}}}";
        StringBuilder sb = new StringBuilder();
        Map<String,Object> vars = new HashMap<>();
        vars.put("params",StringHelper.join(getParams(),","));
        if(returnExpr != null){
            vars.put("body",returnExpr.toString());
        }else{
            List<String> collect = body.stream().map(vs -> vs.toString()).collect(Collectors.toList());
            vars.put("body",StringHelper.join(collect,";"));
        }
        return StringHelper.format(template,vars);
    }
}
