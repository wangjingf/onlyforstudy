package compiler.expr.ast;

public class ASTNode {
    public ASTNode() {
        this.name = getClass().getSimpleName();
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
