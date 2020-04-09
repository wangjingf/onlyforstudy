package compiler.expr;

import compiler.expr.ast.ASTNode;
import compiler.expr.ast.BinOp;
import compiler.expr.ast.Num;

/**
 *  基本推导公式
 *  E->E+T|E-T|T => E->T(+T)* | T(-T)*
 *  T->T*F|T/F|F => T->F(*F)* | T->F(/F)*
 *  F->int|(expr)
 *  更换为：E改名为expr,T改名为term,F为名为factor
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
        while(currentToken.getTokenType().equals(TokenType.DIV) || currentToken.getTokenType().equals(TokenType.MUL)){
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
        }else if(TokenType.NUM.equals(currentToken.getTokenType())){
            node = new Num(currentToken);
            eatToken(currentToken);
        }else{
            throw new SyntaxException("unexpected input");
        }
        return node;
    }
    public void eatToken(Token token){
        eatToken(token.getTokenType());
    }
    public void eatToken(TokenType expected){
        if(expected.equals(currentToken.getTokenType())){
            currentToken = lexer.getNextToken();
        }else{
            throw new SyntaxException("expected "+expected+" but is :"+ currentToken.getTokenType());
        }
    }
    public static void main(String[] args){
        ExprParser parser = new ExprParser("1+(2*3)/2-1");
        ASTNode node = parser.expr();
    }
}
