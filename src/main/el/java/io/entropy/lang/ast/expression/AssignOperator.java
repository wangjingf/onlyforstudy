package io.entropy.lang.ast.expression;

import java.util.HashMap;
import java.util.Map;

public enum AssignOperator {
    MUL("*="),DIV("/="),MOD("%="),PLUS("+="),MINUS("-=")
    ,LEFT_SHIFT("<<="),RIGHT_SHIFT(">>="),RIGHT_SHIFT_LOGICAL(">>>="),
    BIT_AND("&="),BIT_X_OR_("^="),BIT_OR("|=");

    String op;

    AssignOperator(String op) {
        this.op = op;
    }
    static Map<String,AssignOperator> map = new HashMap<>();
    static{
        for (AssignOperator binaryOp : AssignOperator.values()) {
            map.put(binaryOp.op,binaryOp);
        }
    }
    public static AssignOperator from(String op){
        return  map.get(op);
    }
}
