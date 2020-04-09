package compiler.expr;

public class Lexer {
    String expr;
    int index;
    int line=1;
    int pos;
    public Lexer(String expr){
        this.expr = expr;
    }
    void advance(){
        advanceComment();
        while(!isEnd()){
            if(Character.isWhitespace(peek())){
                skip();
            }
            break;
        }
        advanceComment();
    }
    void skip(){
        skip(1);
    }
    void skip(int num){

        index = index+num;
        pos += num;
    }
    char peek(int skip){
       char c = peek();
        if(c=='\n'){
            line++;
            pos = 0;
        }
       skip(skip);
       return c;
    }
    char peek(){
        char c = expr.charAt(index);
        return c;
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
            char c = expr.charAt(index);
            skip();
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
                   skip(2);
                   break;
               }
           }else{
               skip();
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
        char expectedChar =  peek(1);

        if(expectedChar != c){
            throw new jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException("expected is :"+c+" but is："+expectedChar);
        }
    }
    Token numToken(){
        advance();
        StringBuilder sb = new StringBuilder();
        while(index < expr.length()){
            char c = peek(1);
            if(Character.isDigit(c)){
                sb.append(c);
            }else if(Character.isLetter(c)){
                throw new SyntaxException("expected token type:"+TokenType.NUM+" but is "+ c);
            }else{
                skip(-1);
                break;
            }
        }
        Token token = new Token(TokenType.NUM,Integer.valueOf(sb.toString()));
        token.setLine(line);
        token.setPos(pos);
        return token;
    }

    public Token getNextToken(){
        while (index < expr.length()){
            advance();
            char c = peek(1);
            if(c == '+'){
                return new Token(TokenType.PLUS,line,pos);
            }else if(c == '-'){
                return new Token(TokenType.SUB,line,pos);
            }else if(c == '*'){
                return new Token(TokenType.MUL,line,pos);
            }else if(c == '/'){
                return new Token(TokenType.DIV,line,pos);
            }else if(c == '('){
                return new Token(TokenType.LEFT_PARA,line,pos);
            }else if(c == ')'){
                return new Token(TokenType.RIGHT_PARA,line,pos);
            }else if(Character.isDigit(c)){
                skip(-1);
                return numToken();
            }else{
                throw new SyntaxException("unexpected input"+c);
            }
        }
        return new Token(TokenType.END);
    }


}

