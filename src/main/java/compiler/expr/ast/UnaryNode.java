package compiler.expr.ast;

import compiler.expr.Token;

public class UnaryNode extends ASTNode {
    ASTNode astNode;
    public UnaryNode(ASTNode node){
        this.astNode = node;
    }

    @Override
    public String toString() {
       return "-("+astNode.toString()+")";
    }
}
