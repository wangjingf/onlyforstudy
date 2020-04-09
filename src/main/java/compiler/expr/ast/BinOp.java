package compiler.expr.ast;

import compiler.expr.Token;

public class BinOp extends ASTNode{
    Token left;
    Token op;
    Token right;

    public BinOp(Token left, Token op, Token right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public Token getLeft() {
        return left;
    }

    public void setLeft(Token left) {
        this.left = left;
    }

    public Token getOp() {
        return op;
    }

    public void setOp(Token op) {
        this.op = op;
    }

    public Token getRight() {
        return right;
    }

    public void setRight(Token right) {
        this.right = right;
    }
}
