package compiler.expr.ast;

import compiler.expr.Token;

public class RealNode extends ASTNode {
    Token token;
    double value;

    public RealNode(Token token) {
        this.token = token;
        this.value = (double) token.getValue();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String toString(){
        return this.value+"";
    }
}
