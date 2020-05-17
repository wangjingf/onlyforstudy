package compiler.infix;

import compiler.expr.*;
import compiler.expr.ast.ASTNode;
import compiler.expr.ast.BinOp;
import compiler.expr.ast.IntNode;
import compiler.expr.ast.UnaryNode;
import io.study.exception.StdException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * pratt parser
 * http://www.craftinginterpreters.com/compiling-expressions.html#parsing-infix-expressions
 */
public class Program {
    Map<TokenType,ParseRule> parseRuleMap = new LinkedHashMap<>();
    Lexer lexer = null;
    Token currentToken;
    Token prevToken;

    public void advance(){
        prevToken = currentToken;
        currentToken = lexer.getNextToken();
    }
    public void consume(TokenType tokenType){
        if(tokenType != currentToken.getTokenType()){
            throw new SyntaxException("invalid token:"+currentToken.getTokenType()+",expect is :"+tokenType);
        }
        advance();
    }
    public Program(Lexer lexer){
        this.lexer = lexer;
        advance();
    }

    public Object  parsePrecedence(Precedence precedence){
        advance();
        Function prefixRule = parseRuleMap.get(prevToken.getTokenType()).getPrefix();
        if(prefixRule == null){
            throw new StdException("expect expression");
        }
        Object left  = prefixRule.apply(null);
        while(precedence.ordinal() < parseRuleMap.get(currentToken.getTokenType()).precedence.ordinal()){
            advance();
            Function infix = parseRuleMap.get(prevToken.getTokenType()).infix;
            left = infix.apply(left);
        }
        return left;
    }
    public Object grouping(Object param){
            expression();
            consume(TokenType.RIGHT_PARA);
            return null;
    }
    public Object unary(Object param){
        return new UnaryNode((ASTNode) parsePrecedence(Precedence.UNARY));
    }
    public Object expression(){
        return parsePrecedence(Precedence.ASSIGNMENT);
    }

    public Object binary(Object left){
        TokenType prevTokenType = prevToken.getTokenType();
        ParseRule parseRule = parseRuleMap.get(prevTokenType);
        Object right = parsePrecedence(Precedence.values()[parseRule.precedence.ordinal()+1]);
        return new BinOp((ASTNode) left,new Token(prevTokenType), (ASTNode) right);
    }
    public Object number(){
        return new IntNode(new Token(TokenType.INTEGER,prevToken.getValue()));
    }
    public Object number(Object object){
        return number();
    }
    public void init(){
        parseRuleMap.put(TokenType.LEFT_PARA,new ParseRule(this::grouping,null,Precedence.NONE));
        parseRuleMap.put(TokenType.PLUS,new ParseRule(null,this::binary,Precedence.TERM));
        parseRuleMap.put(TokenType.SUB,new ParseRule(this::unary,this::binary,Precedence.TERM));
        parseRuleMap.put(TokenType.INTEGER,new ParseRule(this::number,null,Precedence.NONE));
        parseRuleMap.put(TokenType.EOF,new ParseRule(null,null,Precedence.NONE));
    }
    public static void main(String[] args){
        Program program = new Program(new Lexer("-1+2+3"));
        program.init();
        Object ret = program.expression();
        System.out.println(ret);
    }


}
