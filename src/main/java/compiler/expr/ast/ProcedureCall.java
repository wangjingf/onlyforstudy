package compiler.expr.ast;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class ProcedureCall extends ASTNode {
    String procedureName;

    List<ASTNode> params = new LinkedList<>();

    public ProcedureCall(String procedureName, List<ASTNode> params) {
        this.procedureName = procedureName;
        this.params = params;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public List<ASTNode> getParams() {
        return params;
    }

    public void setParams(List<ASTNode> params) {
        this.params = params;
    }
}
