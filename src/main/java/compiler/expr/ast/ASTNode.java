package compiler.expr.ast;

public class ASTNode {
    public ASTNode(String type) {
        this.type = type;
    }

    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
