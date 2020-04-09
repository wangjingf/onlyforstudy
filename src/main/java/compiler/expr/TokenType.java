package compiler.expr;

public enum TokenType {
    MUL("*"),DIV("/"),NUM(null),PLUS("+"),SUB("-"),LEFT_PARA("("),RIGHT_PARA(")"),END("end");
    String name;
    TokenType(String name){
        this.name = name;
    }
}
