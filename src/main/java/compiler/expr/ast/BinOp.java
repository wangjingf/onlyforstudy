package compiler.expr.ast;

import compiler.expr.Token;

public class BinOp extends ASTNode{
    ASTNode left;
    Token op;
    ASTNode right;

    public BinOp(ASTNode left, Token op, ASTNode right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }



    public Token getOp() {
        return op;
    }

    public void setOp(Token op) {
        this.op = op;
    }

    public ASTNode getLeft() {
        return left;
    }

    public void setLeft(ASTNode left) {
        this.left = left;
    }

    public ASTNode getRight() {
        return right;
    }

    public void setRight(ASTNode right) {
        this.right = right;
    }

    public String toString(){
        return left.toString()+op.getTokenType().getName()+right.toString();
    }
}
