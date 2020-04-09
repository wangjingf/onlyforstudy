package compiler.expr;

public class Lexer {
    String expr;
    int index;
    public Lexer(String expr){
        this.expr = expr;
    }
    void advance(){
        advanceComment();
        while(!isEnd()){
            if(Character.isWhitespace(expr.charAt(index))){
                index++;
            }
            break;
        }
        advanceComment();
    }
    public boolean isEnd(){
        return index>=expr.length();
    }
    void advanceComment(){
        if(index >= expr.length()-1){
            return;
        }
        char c = expr.charAt(index);
        char nextChar = expr.charAt(index+1);
        if(c == '/' ){
            if(nextChar == '/'){
                eat("//");
                advanceLineComment();
            }else if(nextChar == '*'){
                eat("/*");
                advanceMultiLineComment();
            }
        }

    }
    void advanceLineComment(){
        while (index < expr.length()){
            char c = expr.charAt(index++);
            if(c != '\n'){
            }else{
                break;
            }
        }
    }

    void advanceMultiLineComment(){
       while(!isEnd()){
           char c = expr.charAt(index);
           if(c == '*'){
               if(isEnd()){
                   throw new jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException("unexpected end");
               }
               char nextChar = expr.charAt(index+1);
               if(nextChar == '/'){
                   index = index+2;
                   break;
               }
           }else{
               index++;
           }
       }
        throw new jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException("unexpected end");
    }
    void eat(String  s){
        for (int i = 0; i < s.length(); i++) {
            eat(s.charAt(i));
        }
    }
    void eat(char c){
        if(isEnd()){
            throw new jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException("expected is :"+c+" but is end");
        }
        char expectedChar =  expr.charAt(index++);
        if(expectedChar != c){
            throw new jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException("expected is :"+c+" but is："+expectedChar);
        }
    }
    Token numToken(){
        advance();
        StringBuilder sb = new StringBuilder();
        while(index < expr.length()){
            char c = expr.charAt(index++);
            if(Character.isDigit(c)){
                sb.append(c);
            }else if(!Character.isWhitespace(c)){
                throw new SyntaxException("expected token type"+TokenType.NUM+" but is "+ c);
            }else{
                index--;
                break;
            }
        }
        return new Token(TokenType.NUM,Integer.valueOf(sb.toString()));
    }
    public Token getNextToken(){
        while (index < expr.length()){
            advance();
            char c = expr.charAt(index++);
            if(c == '+'){
                return new Token(TokenType.PLUS);
            }else if(c == '-'){
                return new Token(TokenType.SUB);
            }else if(c == '*'){
                return new Token(TokenType.MUL);
            }else if(c == '/'){
                return new Token(TokenType.DIV);
            }else if(c == '（'){
                return new Token(TokenType.LEFT_PARA);
            }else if(c == ')'){
                return new Token(TokenType.LEFT_PARA);
            }else if(Character.isDigit(c)){
                index--;
                return numToken();
            }else{
                throw new SyntaxException("unexpected input"+c);
            }
        }
        return new Token(TokenType.END);
    }
}

