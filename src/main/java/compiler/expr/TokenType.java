package compiler.expr;

import java.util.Map;

public enum TokenType {
    MUL("*"),INT_DIV("/"),PLUS("+"),SUB("-")
    ,LEFT_PARA("("),RIGHT_PARA(")"),COMMA(",")
    ,ASSIGN(":=")
    ,DOT(".")
    ,SEMI(";")
    ,REAL_DIV("DIV")
    ,BEGIN("BEGIN")
    ,END("END")
    ,INTEGER
    ,PROGRAM("PROGRAM")
    ,PROCEDURE("procedure")
    ,REAL
    ,IDENTIFIER
    ,VAR("VAR")
    ,COLON(":")
    ,EOF;
    String name;
    TokenType(String name){
        this.name = name;
    }
    TokenType(){}
    public String getName() {
        return name;
    }



    @Override
    public String toString() {
        return "TokenType{" +
                "name='" + name + '\'' +
                '}';
    }
}
