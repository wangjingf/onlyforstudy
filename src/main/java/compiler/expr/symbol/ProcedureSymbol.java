package compiler.expr.symbol;

import compiler.expr.ast.ASTNode;
import compiler.expr.ast.VarDeclaration;

import java.util.LinkedList;
import java.util.List;

public class ProcedureSymbol extends Symbol {
    String name;
    ASTNode body;
    List<VarDeclaration> params = new LinkedList<>();
    /**
     * 程序内部声明的变量
     */
    List<VarDeclaration> vars = new LinkedList<>();
    public ProcedureSymbol(String name) {
        super(name);
    }

    public ASTNode getBody() {
        return body;
    }

    public void setBody(ASTNode body) {
        this.body = body;
    }

    public List<VarDeclaration> getParams() {
        return params;
    }

    public void setParams(List<VarDeclaration> params) {
        this.params = params;
    }

    public List<VarDeclaration> getVars() {
        return vars;
    }

    public void setVars(List<VarDeclaration> vars) {
        this.vars = vars;
    }
    public List<VarDeclaration> getAllVars(){
        List<VarDeclaration> ret = new LinkedList<>();
        ret.addAll(vars);
        ret.addAll(params);
        return ret;
    }
}
