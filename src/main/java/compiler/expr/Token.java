package compiler.expr;

public class Token {
    TokenType tokenType;
    Object value;

    public Token(TokenType tokenType, Object value) {
        this.tokenType = tokenType;
        this.value = value;
    }
    public Token(TokenType tokenType) {
       this(tokenType,null);
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
}
