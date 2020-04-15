package compiler.expr.ast;

import compiler.expr.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VarDeclaration extends ASTNode {
    Token type;
    Identifier id;
    public VarDeclaration(){
        this(null,null);
    }
    public VarDeclaration(Identifier id,Token type) {
        this.id = id;
        this.type = type;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public Token getType() {
        return type;
    }

    public void setType(Token type) {
        this.type = type;
    }
    public String toString(){
        return getId()+":"+ type.getValue();
    }
}
