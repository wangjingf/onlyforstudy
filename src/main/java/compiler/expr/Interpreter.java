package compiler.expr;

import compiler.expr.ast.*;
import compiler.expr.scope.PascalScope;
import compiler.expr.stack.ActivatedRecord;
import compiler.expr.stack.CallStack;
import compiler.expr.symbol.*;
import io.study.helper.Variant;
import io.study.reflect.ReflectHelper;

import java.util.LinkedList;
import java.util.List;

public class Interpreter {
    //PascalScope currentScope = new PascalScope();
    CallStack callStack = new CallStack();
    SymbolTable currentSymbolTable = new SymbolTable();
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
        callStack.peek().put(assign.getLeft().getId(),value);
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
        ActivatedRecord activatedRecord = new ActivatedRecord(1,program.getIdentifier().getId(),TokenType.PROGRAM);
        callStack.push(activatedRecord);
        for (ASTNode declaration : program.getVarDeclarations()) {
            visit(declaration);
        }
        Object ret = visitCompoundNode(program.getCompoundNode());
        callStack.pop();
        return ret;
    }
    void defineVarSymbol(VarDeclaration declaration){
        BuiltinTypeSymbol typeSymbol = (BuiltinTypeSymbol) requireSymbol((String) declaration.getType().getValue());
        currentSymbolTable.define(new VarSymbol(declaration.getId().getId(),typeSymbol ));
    }
    public Object visitProcedureCall(ProcedureCall procedureCall){
        Symbol symbol = currentSymbolTable.lookup(procedureCall.getProcedureName());
        if(!(symbol instanceof ProcedureSymbol)){
            throw error("expected produceSymbol but is "+symbol);
        }
        for (ASTNode declaration : ((ProcedureSymbol) symbol).getAllVars()) {
            if(declaration instanceof VarDeclaration){//定义内部变量
               defineVarSymbol((VarDeclaration) declaration);
            }else if(declaration instanceof Procedure){
                defineProcedureSymbol((Procedure) declaration);
            }

        }
        int size = callStack.size();
        ActivatedRecord activatedRecord = new ActivatedRecord(size+1,procedureCall.getProcedureName(),TokenType.PROCEDURE);
        callStack.push(activatedRecord);
        List<Object> actualParam = new LinkedList<>();
        for (ASTNode astNode : procedureCall.getParams()) {
            actualParam.add(visit(astNode));
        }

        int i = 0;
        for (VarDeclaration declaration : ((ProcedureSymbol) symbol).getParams()) { //形参转换为实参
            activatedRecord.put(declaration.getId().getId(),actualParam.get(i));
            i++;
        }
        visit(((ProcedureSymbol)symbol).getBody());
        callStack.pop();
        return null;
    }
    void defineProcedureSymbol(Procedure procedure){
        ProcedureSymbol procedureSymbol = new ProcedureSymbol(procedure.getId().getId());
        procedureSymbol.setBody(procedure.getBody());
        procedureSymbol.setParams(procedure.getParams());
        procedureSymbol.setVars(procedure.getVars());
        currentSymbolTable.define(procedureSymbol);
    }
    public Object visitProcedure(Procedure procedure){

        SymbolTable scopedSymbolTable = new SymbolTable();
        scopedSymbolTable.setParent(currentSymbolTable);
        currentSymbolTable = scopedSymbolTable;
        defineProcedureSymbol(procedure);
        return null;
    }
    public Object visitRealNode(RealNode num){
        return num.getValue();
    }
    public Object visitVarDeclaration(VarDeclaration varDeclaration){
         Symbol symbol = requireSymbol((String) varDeclaration.getType().getValue());
         currentSymbolTable.define(new VarSymbol(varDeclaration.getId().getId(), (BuiltinTypeSymbol) symbol));
         return null;
    }
    public Object requireValue(String name){
        if(callStack.peek().contains("name")){
            throw error("var "+name +" is not initialized!");
        }
        return callStack.peek().get(name);
    }
    public Symbol requireSymbol(String name){
        Symbol symbol = currentSymbolTable.lookup(name);
        if(symbol == null){
            throw error("unexpected type:"+name);
        }
        return symbol;
    }

    public static void main(String[] args){
        ExprParser parser = new ExprParser("1+2*3-1");
        ASTNode node = parser.expr();
        Interpreter interpreter = new Interpreter();
        Object result =  interpreter.visit(node);
        System.out.println(result);
    }
}
