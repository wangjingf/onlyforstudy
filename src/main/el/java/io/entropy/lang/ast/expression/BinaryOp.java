package io.entropy.lang.ast.expression;

import java.util.HashMap;
import java.util.Map;

public enum BinaryOp {

    MUL("*"),DIV("/"),MOD("%"),PLUS("+"),MINUS("-"),LEFT_SHIFT("<<"),RIGHT_SHIFT(">>"),LEFT_SHIFT_LOGICAL("<<<"),
    RIGHT_SHIFT_LOGICAL(">>>"),GT(">"),GE(">="),LT("<"),LE("<="),EQ("="),NOT_EQ("!="),BIT_AND("&"),BIT_OR("|"),
    AND("&&"),OR("||"),ASSIGN("="),MUL_ASSIGN("*="),DIV_ASSIGN("/="),MOD_ASSIGN("%="),
    PLUS_ASSIGN("+="),MINUS_ASSIGN("-="),LEFT_SHIFT_ASSIGN("<<="),RIGHT_SHIFT_ASSIGN(">>="),
    RIGHT_SHIFT_LOGICAL_ASSIGN(">>>="),BIT_AND_ASSIGN("^="),BIT_OR_ASSIGN("|=");
    String op;

    BinaryOp(String op) {
        this.op = op;
    }
    static Map<String,BinaryOp> map = new HashMap<>();
    static{
        for (BinaryOp binaryOp : BinaryOp.values()) {
            map.put(binaryOp.op,binaryOp);
        }
    }
    public static BinaryOp from(String op){
        return  map.get(op);
    }
}
