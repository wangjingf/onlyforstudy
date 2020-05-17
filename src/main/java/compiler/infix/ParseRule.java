package compiler.infix;

import java.util.concurrent.Callable;
import java.util.function.Function;

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
