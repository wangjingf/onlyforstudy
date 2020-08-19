package io.entropy.lang.ast.expression;

import java.util.HashMap;
import java.util.Map;

public enum UnaryOp {
    BEFORE_PLUSPLUS("++"),BEFORE_MINUS_MINUS("--"),
    PLUS("+"),MINUS("-"),BIT_NOT("~"),NOT("!"),
    AFTER_PLUS_PLUS("++",false),AFTER_MINUS_MINUS("--",false);

    String op;
    boolean isBefore=true;
    UnaryOp(String op) {
        this.op = op;
    }
    UnaryOp(String op,boolean isBefore){
        this.op = op;
        this.isBefore = isBefore;
    }
    static Map<String,UnaryOp> map = new HashMap<>();
    static{
        for (UnaryOp unaryOp : UnaryOp.values()) {
            map.put(unaryOp.op,unaryOp);
        }
    }
    public static UnaryOp from(String op){
        return map.get(op);
    }
}
