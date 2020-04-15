package compiler.expr;

import compiler.expr.ast.*;
import compiler.expr.scope.PascalScope;
import compiler.expr.symbol.BuiltinTypeSymbol;
import compiler.expr.symbol.Symbol;
import compiler.expr.symbol.SymbolTable;
import compiler.expr.symbol.VarSymbol;
import io.study.helper.Variant;
import io.study.reflect.ReflectHelper;

import java.util.Map;

public class Interpreter {
    PascalScope scope = new PascalScope();
    SymbolTable symbolTable = new SymbolTable();
    public Object visit(ASTNode node){
        return ReflectHelper.invokeMethod(this,"visit"+node.getName(),node);
    }

    SemanticsException error(String message){
        return new SemanticsException(message);
    }

    public Object visitBinOp(BinOp binOp){
        TokenType tokenType = binOp.getOp().getTokenType();
        Object leftValue =  visit(binOp.getLeft());
        Object rightValue =  visit(binOp.getRight());
        /*if(leftValue.getClass() != rightValue.getClass()){
            throw error("left and right must match,but is ,left:"+leftValue+" right:"+rightValue);
        }
        */
        Double left = Variant.valueOf(leftValue).doubleValue();
        Double right = Variant.valueOf(rightValue).doubleValue();
        if(TokenType.PLUS.equals(tokenType)){
           return left + right;
        }else if(TokenType.SUB.equals(tokenType)){
           return left - right;
        }else if(TokenType.MUL.equals(tokenType)){
            return left * right;
        }else if(TokenType.INT_DIV.equals(tokenType)){
               return left / right;
        }else if(TokenType.REAL_DIV == tokenType){
            return left / right;
        }
        return null;
    }
    public Object visitNoOp(NoOp noOp){
        return null;
    }
    public Object visitAssign(Assign assign){
        requireSymbol(assign.getLeft().getId());
        Object value = visit(assign.getRight());
        scope.addVar(assign.getLeft().getId(),value);
        return null;
    }
    public Object visitCompoundNode(CompoundNode node){
        Object ret = null;
        for (ASTNode astNode : node.getChildren()) {
            ret = visit(astNode);
        }
        return ret;
    }
    public Object visitIdentifier(Identifier id){
        requireSymbol(id.getId());
        return requireValue(id.getId());
    }
    public Object visitIntNode(IntNode node){
        return node.getValue();
    }
    public Object visitProgram(Program program){
        for (VarDeclaration declaration : program.getVarDeclarations()) {
            visitVarDeclaration(declaration);
        }
        return visitCompoundNode(program.getCompoundNode());
    }
    public Object visitRealNode(RealNode num){
        return num.getValue();
    }
    public Object visitVarDeclaration(VarDeclaration varDeclaration){
         Symbol symbol = requireSymbol((String) varDeclaration.getType().getValue());
         symbolTable.define(new VarSymbol(varDeclaration.getId().getId(), (BuiltinTypeSymbol) symbol));
         return null;
    }
    public Object requireValue(String name){
        if(scope.containsVar("name")){
            throw error("var "+name +" is not initialized!");
        }
        return scope.getVar(name);
    }
    public Symbol requireSymbol(String name){
        Symbol symbol = symbolTable.lookup(name);
        if(symbol == null){
            throw error("unexpected type:"+name);
        }
        return symbol;
    }
    public PascalScope getScope(){
        return scope;
    }
    public static void main(String[] args){
        ExprParser parser = new ExprParser("1+2*3-1");
        ASTNode node = parser.expr();
        Interpreter interpreter = new Interpreter();
        Object result =  interpreter.visit(node);
        System.out.println(result);
    }
}
