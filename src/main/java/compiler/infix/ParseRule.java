package compiler.infix;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * ParseRule rules[] = {
 *   { grouping, NULL,    PREC_NONE },       // TOKEN_LEFT_PAREN
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_RIGHT_PAREN
 *   { NULL,     NULL,    PREC_NONE },      // TOKEN_LEFT_BRACE
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_RIGHT_BRACE
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_COMMA
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_DOT
 *   { unary,    binary,  PREC_TERM },       // TOKEN_MINUS
 *   { NULL,     binary,  PREC_TERM },       // TOKEN_PLUS
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_SEMICOLON
 *   { NULL,     binary,  PREC_FACTOR },     // TOKEN_SLASH
 *   { NULL,     binary,  PREC_FACTOR },     // TOKEN_STAR
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_BANG
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_BANG_EQUAL
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_EQUAL
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_EQUAL_EQUAL
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_GREATER
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_GREATER_EQUAL
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_LESS
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_LESS_EQUAL
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_IDENTIFIER
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_STRING
 *   { number,   NULL,    PREC_NONE },       // TOKEN_NUMBER
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_AND
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_CLASS
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_ELSE
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_FALSE
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_FOR
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_FUN
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_IF
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_NIL
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_OR
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_PRINT
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_RETURN
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_SUPER
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_THIS
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_TRUE
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_VAR
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_WHILE
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_ERROR
 *   { NULL,     NULL,    PREC_NONE },       // TOKEN_EOF
 * };
 */
public class ParseRule {
    Function prefix;
    Function infix;
    Precedence precedence;

    public ParseRule(Function prefix, Function infix, Precedence precedence) {
        this.prefix = prefix;
        this.infix = infix;
        this.precedence = precedence;
    }

    public Function getPrefix() {
        return prefix;
    }

    public void setPrefix(Function prefix) {
        this.prefix = prefix;
    }

    public Function getInfix() {
        return infix;
    }

    public void setInfix(Function infix) {
        this.infix = infix;
    }

    public Precedence getPrecedence() {
        return precedence;
    }

    public void setPrecedence(Precedence precedence) {
        this.precedence = precedence;
    }
}
