package compiler.expr;

public class Lexer {
    String expr;
    int index;
    int line = 1;
    int pos;
    static final char EOF = 0;
    char currentChar;

    public Lexer(String expr) {
       this(expr,0);
    }
    public Lexer(String expr,int index) {
        this.expr = expr;
        if (expr.length() > index) {
            currentChar = expr.charAt(index);
            for (int i = 0; i < index; i++) {
                if(expr.charAt(i) == '\n'){
                    line++;
                    pos = 0;
                }
                pos++;
            }
        } else {
            currentChar = EOF;
        }
        this.index = index;
    }
    void advance() {
        while (!isEnd()) {
            if (currentChar == '\n') {
                line++;
                pos = 0;
            }
            if (Character.isWhitespace(currentChar)) {
                nextChar();
            }else{
                break;
            }
        }

        advanceComment();
    }
    public char current(){
        return currentChar;
    }

    public char peekChar() {
        int pos = index + 1;
        if (pos > expr.length()) {
            return EOF;
        }
        return expr.charAt(pos);
    }

    void nextChar() {
        index++;
        pos++;
        if (index >= expr.length()) {
            currentChar = EOF;
        } else {
            currentChar = expr.charAt(index);
        }
    }

    public boolean isEnd() {
        return currentChar == EOF;
    }

    void advanceComment() {
        if (currentChar == '/') {
            if (peekChar() == '/') {
                advanceLineComment();
            } else if (peekChar() == '*') {
                advanceMultiLineComment();
            }
        }

    }

    void advanceLineComment() {
        nextChar();
        nextChar();// 跳过//
        while (true) {
            if (currentChar == EOF) {
                break;
            } else if (currentChar == '\n') {
                nextChar();
                break;
            } else {
                nextChar();
            }
        }
    }

    void advanceMultiLineComment() {
        nextChar();
        nextChar();// 跳过/*
        while (true) {
            if (currentChar == EOF) {
                break;
            } else if (currentChar == '*' && peekChar() == '/') {
                nextChar();
                nextChar();
            } else {
                nextChar();
            }
        }
    }


    Token numToken() {
        advance();
        StringBuilder sb = new StringBuilder();
        boolean isReal = false;
        while (index < expr.length()) {
            if (Character.isDigit(currentChar)) {
                sb.append(currentChar);
                nextChar();
            } else if (currentChar == '.') {
                if (Character.isDigit(peekChar())) {
                    isReal = true;
                }
                sb.append(currentChar);
                nextChar();
            } else if (Character.isLetter(currentChar)) {
                throw error("expected token type:number but is " + currentChar);
            } else {
                break;
            }
        }
        if (isReal) {
            return new Token(TokenType.REAL, Double.valueOf(sb.toString()));
        } else {
            return new Token(TokenType.INTEGER, Integer.valueOf(sb.toString()));
        }
    }

    boolean isIdentifierType(char c) {
        return Character.isLetter(c) || c == '_';
    }

    Token identifier() {
        advance();
        StringBuilder sb = new StringBuilder();
        while (index < expr.length()) {
            if (isIdentifierType(currentChar) || Character.isDigit(currentChar)) {
                sb.append(currentChar);
                nextChar();
            } else {
                break;
            }
        }
        String s = sb.toString();
        TokenType tokenType = TokenHelper.getKeywordToken(s);
        if (tokenType == null) {
            tokenType = TokenType.IDENTIFIER;
        }
        return new Token(tokenType, s);
    }

    public Token peek() {
        if(currentChar == EOF){
            return null;
        }
        return new Lexer(expr,index).getNextToken();
    }

    public Token getNextToken() {
        advance();
        Token token = null;
        if (isIdentifierType(currentChar)) {
            token = identifier();
        } else if (currentChar == ':' && peekChar() == '=') {
            token = new Token(TokenType.ASSIGN, null);
            nextChar();nextChar();
        } else if (TokenHelper.isSymbolToken(currentChar)) {
            token = new Token(TokenHelper.getSymbolToken(currentChar), null);
            nextChar();
        } else if (Character.isDigit(currentChar)) {
            token = numToken();
        } else if (currentChar == EOF) {
            token = new Token(TokenType.EOF);
        } else {
            throw error("unepected input char:" + currentChar);
        }
        return token;
    }
    LexerException error(String message){
        LexerException exception = new LexerException(message+" at line "+line+" col:"+pos);
        return exception;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }
}

