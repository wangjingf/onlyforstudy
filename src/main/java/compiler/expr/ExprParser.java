package compiler.expr;

import compiler.expr.ast.ASTNode;
import compiler.expr.ast.Num;

/**
 *  基本推导公式
 *  E->E+T|E-T|T => E->T(+T)* | T(-T)*
 *  T->T*F|T/F|F => T->F(*F)* | T->F(/F)*
 *  F->int
 *  更换为：E改名为expr,T改名为term,F为名为factor
 *
 *  https://ruslanspivak.com/lsbasi-part7/
 */
public class ExprParser {
    final Lexer lexer;
    public ExprParser(String expr){
        this.lexer = new Lexer(expr);
    }
    public void term(){

    }
    public void factor(){}
    public ASTNode expr(){
        Num num = new Num(eatToken(TokenType.NUM)) ;
        Token token = lexer.getNextToken();
        while (!lexer.isEnd()){
            if(token.getTokenType().equals(TokenType.MUL)){

            }
        }
        return num;
    }
    public Token eatToken(TokenType expected){
        Token token = lexer.getNextToken();
        if(expected.equals(token.getTokenType())){
            throw new SyntaxException("expected:"+expected+" but is :"+ token.getTokenType());
        }
        return token;
    }

}
