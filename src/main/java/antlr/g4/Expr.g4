grammar Expr;
options{
    output=AST;
    ASTLabelType=CommonTree;
    language=Java;
}
expr:
    '(' expr ')' # Paren  //运算符优先级依次递减
    | '-' expr # UnaryOp
   |  '!' expr # Negation
   | expr '*' expr  # BinaryOp
     | expr '/' expr  # BinaryOp
    | expr '+' expr  # BinaryOp
     |expr '-' expr  # BinaryOp
    | <assoc=right> expr '?' expr ':' expr # TernaryOp
     |expr '>' expr # BinaryOp
     | expr '<' expr # BinaryOp
     | expr '==' expr # BinaryOp
     |expr '!=' expr # BinaryOp
     | expr '&' expr # BinaryOp
     | expr '|' expr # BinaryOp
     |ID # Identifier
     |INT # Int;
ID : LETTER[A-Z_a-z1-9]*   ;
INT : [0-9]+;
fragment LETTER : [A-Za-z]; //定义一个段落
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines