package compiler.expr.ast;

import java.util.LinkedList;
import java.util.List;

public class Procedure extends ASTNode{
    List<VarDeclaration> params = new LinkedList<>();
    /**
     * 包含声明的procedure及变量信息
     */
    List<ASTNode> vars = new LinkedList<>();
    Identifier id;
    CompoundNode body;

    public Procedure(List<VarDeclaration> params, Identifier id, CompoundNode body) {
        this.params = params;
        this.id = id;
        this.body = body;
    }

    public List<VarDeclaration> getParams() {
        return params;
    }

    public void setParams(List<VarDeclaration> params) {
        this.params = params;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public CompoundNode getBody() {
        return body;
    }

    public void setBody(CompoundNode body) {
        this.body = body;
    }

    public List<ASTNode> getVars() {
        return vars;
    }

    public void setVars(List<ASTNode> vars) {
        this.vars = vars;
    }
}
