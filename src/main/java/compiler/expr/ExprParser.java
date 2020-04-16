package compiler.expr;

import compiler.expr.ast.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *  基本推导公式
 *  E->E+T|E-T|T => E->T(+T)* | T(-T)*
 *  T->T*F|T/F|F => T->F(*F)* | T->F(/F)*
 *  F->int|(expr)
 *  更换为：E改名为expr,T改名为term,F为名为factor
 *  program -> PROGRAM id;(var declaration) compoundStatement dot
 *  compoundStatement -> begin statementList end
 *  statementlist -> statement | statement;statementlist
 *  statement -> assignOp | var declaration | expr |empty | compoundStatement| procedureCall
 *  assignOp -> variable = expr
 *  variable -> id;
 *
 *  produreCall -> id(expr(,expr)*|empty)
 *  var declation -> var (paramList:variable)*; | procedureDeclaration
 *  procedureDeclaration -> produce id(produceParamList);vardeclaration begin statement end;
 *  produceParams -> produceParam(,procureParam)*|empty
 *  produceParam -> id(,id)*:type
 *  paramList->param,paramList|param
 *  param->variable
 *  param -> expr
 *
 *  https://ruslanspivak.com/lsbasi-part7/
 */
public class ExprParser {
    final Lexer lexer;
    Token currentToken;
    public ExprParser(String expr){
        this.lexer = new Lexer(expr);
        currentToken = lexer.getNextToken();
    }
    public ASTNode program(){
        eatToken(TokenType.PROGRAM);
        Token token = eatToken(TokenType.IDENTIFIER);
        Identifier identifier = new Identifier((String) token.getValue());
        eatToken(TokenType.SEMI);
        List<ASTNode> varDeclaration = declarations();
        CompoundNode node =  compoundStatement();
        eatToken(TokenType.DOT);
        return new Program(identifier,varDeclaration,node);
    }
    public CompoundNode compoundStatement(){
        eatToken(TokenType.BEGIN);
        List<ASTNode> ret= statementList();
        eatToken(TokenType.END);
        return new CompoundNode(ret);
    }
    public List<ASTNode> statementList(){
        List<ASTNode> statementList = new LinkedList<>();
        statementList.add(statement());
        while (true){
            if(currentToken.tokenType.equals(TokenType.SEMI)){
                eatToken(TokenType.SEMI);
                statementList.add(statement());
            }else{
                break;
            }
        }
        return statementList;
    }
    public ASTNode statement(){
        if(TokenType.BEGIN.equals(currentToken)){
            return compoundStatement();
        }else if(lexer.peek()!=null && lexer.peek().getTokenType().equals(TokenType.ASSIGN)){
            return assign();
        }else if(lexer.current() == '('){
            return procedureCall();
        }else{//将空作为默认选择即可
                return empty();
        }
    }
    public ASTNode assign(){
        Identifier variable = (Identifier) variable();
        eatToken(TokenType.ASSIGN);
        ASTNode expr = expr();
        return new Assign(variable,expr);
    }
    ProcedureCall procedureCall(){
         String procedureName = (String) currentToken.getValue();
         eatToken(TokenType.IDENTIFIER);
         eatToken(TokenType.LEFT_PARA);
         List<ASTNode> params = new ArrayList<>();
         if(currentToken.getTokenType() != TokenType.RIGHT_PARA){
             params.add(expr());
         }
         while(currentToken.getTokenType() != TokenType.RIGHT_PARA){
             eatToken(TokenType.COMMA);
             params.add(expr());
         }
         eatToken(TokenType.RIGHT_PARA);
         return new ProcedureCall(procedureName,params);
    }
    Procedure procedureDeclaration(){
        eatToken(TokenType.PROCEDURE);
        Identifier identifier = variable();
        eatToken(TokenType.LEFT_PARA);
        List<VarDeclaration> varDeclarations = new LinkedList<>();
        if(!currentToken.getTokenType().equals(TokenType.RIGHT_PARA)){
            varDeclarations.addAll(procedureParams());
        }
        eatToken(TokenType.RIGHT_PARA);
        eatToken(TokenType.SEMI);
        List<VarDeclaration> vars = new LinkedList<>();
        if(currentToken.getTokenType() == TokenType.VAR){
            vars.addAll(varDeclarations());
        }
        CompoundNode body = compoundStatement();
        eatToken(TokenType.SEMI);
        Procedure procedure = new Procedure(varDeclarations, identifier, body);
        procedure.getVars().addAll(vars);
        return procedure;
    }

    /**
     * procedureParams -> param;params | empty
     * param -> id(,id)*:type
     * paramList
     * @return
     */
    List<VarDeclaration> procedureParams(){
        List<VarDeclaration> params = new LinkedList<>();
        if(currentToken.getTokenType()!= TokenType.IDENTIFIER){
            eatToken(TokenType.RIGHT_PARA);
            return params;
        }
        params.addAll(procedureParam());
        while (TokenType.SEMI.equals(currentToken.getTokenType())){
            eatToken(TokenType.SEMI);
            params.addAll(procedureParam());
        }
        return params;
    }
    List<VarDeclaration> procedureParam(){
        List<VarDeclaration> ret = new LinkedList<>();
        List<Identifier> ids = new LinkedList<>();
        ids.add(variable());
        while(TokenType.COMMA.equals(currentToken.getTokenType())){
            eatToken(TokenType.COMMA);
            ids.add(variable());
        }
        eatToken(TokenType.COLON);
        Token type = eatToken(TokenType.IDENTIFIER);
        for (Identifier id : ids) {
            ret.add(new VarDeclaration(id,type));
        }
        return ret;
    }
    public List<VarDeclaration> varDeclarations(){
        List<VarDeclaration> ret = new LinkedList<>();
        eatToken(TokenType.VAR);
        while(TokenType.IDENTIFIER.equals(currentToken.getTokenType())){
            List<Identifier> ids = variableList();
            eatToken(TokenType.COLON);
            Token type = eatToken(TokenType.IDENTIFIER);
            for (Identifier id : ids) {
                ret.add(new VarDeclaration(id,type));
            }
            eatToken(TokenType.SEMI);
        }
        return ret;
    }
    public List<ASTNode> declarations(){
        List<ASTNode> ret = new LinkedList<>();
        while(true){
            if(currentToken.getTokenType() == TokenType.VAR){
                ret.addAll(varDeclarations());
            }else if(currentToken.getTokenType() == TokenType.PROCEDURE){
                ret.add(procedureDeclaration());
            }else{
                break;
            }
        }
        return ret;
    }


    public List<Identifier> variableList(){
        List<Identifier> astNodes = new LinkedList<>();
        astNodes.add(variable());
        while (TokenType.COMMA.equals(currentToken.getTokenType())){
            eatToken(TokenType.COMMA);
            astNodes.add(variable());
        }
        return astNodes;
    }
    public ASTNode empty(){
        return new NoOp();
    }
    public Identifier type(){
        Token oldToken = currentToken;
        eatToken(TokenType.IDENTIFIER);

        return new Identifier((String) oldToken.getValue());
    }
    public Identifier variable(){
        Token oldToken = currentToken;
        eatToken(TokenType.IDENTIFIER);

        return new Identifier((String) oldToken.getValue());
    }
    public List<ASTNode> paramList(){
        List<ASTNode> params = new ArrayList<>();
        params.add(expr());
        while (true){
            if(currentToken.getTokenType() == TokenType.COMMA){
                eatToken(TokenType.COMMA);
                params.add(expr());
            }else{
                break;
            }
        }
        return params;
    }
    public ASTNode param(){
        return expr();
    }
    public ASTNode expr(){
        ASTNode node = this.term();
        Token token = null;
        while(currentToken.getTokenType().equals(TokenType.PLUS) || currentToken.getTokenType().equals(TokenType.SUB)){
            token = currentToken;
            eatToken(currentToken.tokenType);
            node = new BinOp(node,token,term());
        }
        return node;
    }
    public ASTNode term(){
        ASTNode node = this.factor();
        Token token = null;
        while(currentToken.getTokenType().equals(TokenType.INT_DIV)
                || currentToken.getTokenType().equals(TokenType.REAL_DIV)
                || currentToken.getTokenType().equals(TokenType.MUL)){
            token = currentToken;
            eatToken(currentToken.getTokenType());
            node = new BinOp(node,token,factor());
        }
        return node;
    }
    public ASTNode factor(){
        ASTNode node = null;
        if(TokenType.LEFT_PARA.equals(currentToken.getTokenType())){
            eatToken(currentToken);
            node = expr();
            eatToken(TokenType.RIGHT_PARA);
        }else if(TokenType.INTEGER.equals(currentToken.getTokenType())){
            node = new IntNode(currentToken);
            eatToken(currentToken);
        }else if(TokenType.REAL.equals(currentToken.getTokenType())){
            node = new RealNode(currentToken);
            eatToken(currentToken);
        }else if(TokenType.IDENTIFIER.equals(currentToken.getTokenType())){
            node = new Identifier((String) currentToken.getValue());
            eatToken(currentToken);
        }else{
            throw error("unexpected input:"+currentToken);
        }
        return node;
    }
    public void eatToken(Token token){
        eatToken(token.getTokenType());
    }
    public Token eatToken(TokenType expected){
        Token oldToken = currentToken;
        if(expected.equals(currentToken.getTokenType())){
            currentToken = lexer.getNextToken();
            return oldToken;
        }else{
            throw error("expected "+expected+" but is :"+ currentToken.getTokenType());
        }
    }
    SyntaxException error(String message){
        message += " at line "+lexer.getLine()+" pos:"+lexer.getPos();
        return new SyntaxException(message);
    }
    public static void main(String[] args){
        ExprParser parser = new ExprParser("1+(2*3)/2-1");
        ASTNode node = parser.expr();
    }
}

