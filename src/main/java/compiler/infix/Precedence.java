package compiler.infix;

public enum Precedence {
    NONE,ASSIGNMENT,OR,AND,EQ,
    TERM,//+-
    FACTOR,//*/
    UNARY,//!-
    CALL,//.[]运算
    PRIMARY
}
