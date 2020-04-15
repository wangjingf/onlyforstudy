package compiler.expr.ast;

import compiler.expr.Token;

public class IntNode extends ASTNode {
    Token token;
    int value;

    public IntNode(Token token) {
        this.token = token;
        this.value = (int) token.getValue();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString(){
        return this.value+"";
    }
}
