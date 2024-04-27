public class Parser {
    private Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public ParseTree parse() {
        if (lexer.peek() != null) {
        return commandSequence();
        }
        return null;
    }

    private ParseTree commandSequence() {
        if (lexer.peek() != null) {
        ParseTree left = command();
        ParseTree right = null;
        if (lexer.peek() != Token.QUOTE) {
            right = commandSequence();
        }
        return new ParseTree(left, right);
        }
        else
        return null;
    }

    private ParseTree command() {
        Token next = lexer.peek();
        if (next == null) {
            return null;
        }
        switch (next) {
            case ERROR:
                throw new RuntimeException("Syntax Error!");
            case FORW:
            case BACK:
                return movement();
            case LEFT:
            case RIGHT:
                return turn();
            case UP:
            case DOWN:
                return brushMode();
            case COLOR:
                return colorChange();
            case REP:
                return repeat();
            default:
                throw new RuntimeException("Syntax Error!");
        }
    }

    private ParseTree movement() {
        Token move = lexer.pop();
        ParseTree left = new ParseTree(null, null, move.name());
        ParseTree right = decimal();
        return new ParseTree(left, right);
    }

    private ParseTree decimal() {
        Token next = lexer.pop();
        if (next != Token.DECIMAL) {
            throw new RuntimeException("Syntax Error!");
        }
        String decimal = lexer.nextData();
        return new ParseTree(null, null, decimal);
    }

    private ParseTree turn() {
        Token direction = lexer.pop();
        ParseTree left = new ParseTree(null, null, direction.name());
        ParseTree right = decimal();
        return new ParseTree(left, right);
    }

    private ParseTree brushMode() {
        Token mode = lexer.pop();
        ParseTree left = new ParseTree(null, null, mode.name());
        return new ParseTree(left);
    }

    private ParseTree colorChange() {
        lexer.pop();
        ParseTree left = new ParseTree(null, null, Token.COLOR.name());
        ParseTree right = hexCode();
        return new ParseTree(left, right);
    }

    private ParseTree hexCode() {
        Token next = lexer.pop();
        if (next != Token.HEX) {
            throw new RuntimeException("Syntax Error!");
        }
        String hex = lexer.nextData();
        return new ParseTree(null, null, hex);
    }
    
    private ParseTree repeat() {
        lexer.pop();
        Token next = lexer.peek();
        int reps = 0;   
        ParseTree innerLeft = new ParseTree(null, null, Token.REP.name());
        ParseTree innerRight = null;
        if (next == Token.DECIMAL) {
            innerRight = decimal();
        }
        else {
            throw new RuntimeException("Syntax Error!");
        }
        ParseTree left = new ParseTree(innerLeft, innerRight, Token.REP.name());
        ParseTree right = null;
        next = lexer.peek();
        if (next == Token.QUOTE) {
            lexer.pop();
            right = commandSequence();
            lexer.pop();
        }
        else {
            right = command();
        }
        return new ParseTree(left, right);
    }
}
