public class ParseTree {
    private ParseTree left;
    private ParseTree right;
    private String data;

    public ParseTree(ParseTree left, ParseTree right, String data) {
        this.left = left;
        this.right = right;
        this.data = data;
    }

    public ParseTree(ParseTree left) {
        this(left, null, null);
    }

    public ParseTree(ParseTree left, ParseTree right) {
        this(left, right, null);
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
}