public class ParseTree {
    private Token token;
    private String data;
    private ParseTree left;
    private ParseTree right;

    public ParseTree(Token token, String data, ParseTree left, ParseTree right) {
        this.token = token;
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public ParseTree(Token token, String data) {
        this(token, data, null, null);
    }

    public ParseTree(Token token, String data, ParseTree left) {
        this(token, data, left, null);
    }

    public Token getToken() {
        return token;
    }

    public String getData() {
        return data;
    }

    public ParseTree getLeft() {
        return left;
    }

    public ParseTree getRight() {
        return right;
    }

    public void setLeft(ParseTree left) {
        this.left = left;
    }

    public void setRight(ParseTree right) {
        this.right = right;
    }
}