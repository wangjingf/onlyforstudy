package compiler.expr.ast;

import compiler.expr.Token;

import java.util.List;

public class Program extends ASTNode {
    Identifier identifier;
    List<ASTNode> varDeclarations;
    CompoundNode compoundNode;
    public Program(Identifier identifier,List<ASTNode> varDeclarations,CompoundNode compoundNode) {
        this.identifier = identifier;
        this.varDeclarations = varDeclarations;
        this.compoundNode = compoundNode;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public List<ASTNode> getVarDeclarations() {
        return varDeclarations;
    }

    public void setVarDeclarations(List<ASTNode> varDeclarations) {
        this.varDeclarations = varDeclarations;
    }

    public CompoundNode getCompoundNode() {
        return compoundNode;
    }

    public void setCompoundNode(CompoundNode compoundNode) {
        this.compoundNode = compoundNode;
    }
}
