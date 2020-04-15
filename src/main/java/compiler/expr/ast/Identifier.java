package compiler.expr.ast;

public class Identifier extends ASTNode {
    String id;
    public Identifier(String id) {
        this.id = id;
    }
    public String toString(){
        return this.id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
