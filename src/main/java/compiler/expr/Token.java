package compiler.expr;

public class Token implements ISourceLocation {
    TokenType tokenType;
    Object value;
    int line;
    int pos;
    public Token(TokenType tokenType, Object value) {
        this.tokenType = tokenType;
        this.value = value;
    }
    public Token(TokenType tokenType) {
       this(tokenType,null);
    }

    public Token(TokenType tokenType, int line, int pos) {
        this.tokenType = tokenType;
        this.line = line;
        this.pos = pos;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if(TokenType.NUM.equals(tokenType)){
            return "" + value;
        }else {
            return tokenType.getName();
        }

    }

    @Override
    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
