package compiler.expr.ast;

public class Assign extends ASTNode {
    Identifier left;
    ASTNode right;


    public Assign( Identifier left, ASTNode right) {
        this.left = left;
        this.right = right;
    }

    public Identifier getLeft() {
        return left;
    }

    public void setLeft(Identifier left) {
        this.left = left;
    }

    public ASTNode getRight() {
        return right;
    }

    public void setRight(ASTNode right) {
        this.right = right;
    }
    public String toString(){
        return left.getId()+"=" +right.toString();
    }
}
