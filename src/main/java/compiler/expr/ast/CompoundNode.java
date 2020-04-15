package compiler.expr.ast;

import java.util.LinkedList;
import java.util.List;

public class CompoundNode extends ASTNode {
    List<ASTNode> children = new LinkedList<>();

    public CompoundNode(List<ASTNode> children) {
        this.children = children;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public void setChildren(List<ASTNode> children) {
        this.children = children;
    }
}
