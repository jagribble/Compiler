public class Token {
    private String val ="";
    private TokenType type;

    public Token(String v, TokenType t){
        this.val = v;
        this.type = t;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }
}
