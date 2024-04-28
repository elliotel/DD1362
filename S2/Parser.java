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
        while (lexer.peek() == Token.COMMENT) {
            lexer.popWithNewlines();
        }
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
        int line = lexer.peekNewlines();
        if (next == null) {
            return null;
        }
        switch (next) {
            case ERROR:
                throw new RuntimeException("Syntaxfel på rad " + line);
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
                throw new RuntimeException("Syntaxfel på rad " + line);
        }
    }

    private ParseTree movement() {
        Token move = lexer.pop();
        int line = lexer.popNewlines();
        ParseTree left = new ParseTree(null, null, move.name());
        ParseTree right = decimal();
        Token curr = lexer.popWithNewlines();
        if (curr != Token.PERIOD) {
            throw new RuntimeException("Syntaxfel på rad  " + line);
        }
        return new ParseTree(left, right);
    }

    private ParseTree decimal() {
        Token curr = lexer.pop();
        int line = lexer.popNewlines();
        while (curr == Token.COMMENT) {
            curr = lexer.popWithNewlines();
        }
        if (curr != Token.DECIMAL) {
            throw new RuntimeException("Syntaxfel på rad  " + line);
        }
        String decimal = lexer.nextData();
        return new ParseTree(null, null, decimal);
    }

    private ParseTree turn() {
        Token direction = lexer.pop();
        int line = lexer.popNewlines();
        ParseTree left = new ParseTree(null, null, direction.name());
        ParseTree right = decimal();
        while (lexer.peek() == Token.COMMENT) {
            lexer.popWithNewlines();
        }
        if (lexer.peek() != Token.PERIOD) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        lexer.popWithNewlines();
        return new ParseTree(left, right);
    }

    private ParseTree brushMode() {
        Token mode = lexer.pop();
        int line = lexer.popNewlines();
        ParseTree left = new ParseTree(null, null, mode.name());
        while (lexer.peek() == Token.COMMENT) {
            lexer.popWithNewlines();
        }
        if (lexer.peek() != Token.PERIOD) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        lexer.popWithNewlines();
        return new ParseTree(left);
    }

    private ParseTree colorChange() {
        lexer.pop();
        int line = lexer.popNewlines();
        ParseTree left = new ParseTree(null, null, Token.COLOR.name());
        ParseTree right = hexCode();
        while (lexer.peek() == Token.COMMENT) {
            lexer.popWithNewlines();
        }
        if (lexer.peek() != Token.PERIOD) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        lexer.popWithNewlines();
        return new ParseTree(left, right);
    }

    private ParseTree hexCode() {
        Token curr = lexer.pop();
        int line = lexer.popNewlines();
        while (curr == Token.COMMENT) {
            curr = lexer.popWithNewlines();
        }
        if (curr != Token.HEX) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        String hex = lexer.nextData();
        return new ParseTree(null, null, hex);
    }
    
    private ParseTree repeat() {
        lexer.pop();
        int line = lexer.popNewlines();
        Token next = lexer.peek();
        ParseTree innerLeft = new ParseTree(null, null, Token.REP.name());
        ParseTree innerRight = null;
        while (next == Token.COMMENT) {
            lexer.pop();
            line = lexer.popNewlines();
            next = lexer.peek();
        }
        if (next == Token.DECIMAL) {
            innerRight = decimal();
        }
        else {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        ParseTree left = new ParseTree(innerLeft, innerRight);
        ParseTree right = null;
        next = lexer.peek();
        if (next == Token.QUOTE) {
            lexer.popWithNewlines();
            right = commandSequence();
            System.out.println(lexer.peek());
            while (lexer.peek() == Token.COMMENT) {
                lexer.popWithNewlines();
            }
            System.out.println(lexer.peek());
            if (lexer.peek() != Token.QUOTE) {
                throw new RuntimeException("Syntaxfel på rad " + line);
            }
            lexer.popWithNewlines();
        }
        else if (next == Token.REP) {
            right = repeat();
        }
        else {
            right = command();
        }
        return new ParseTree(left, right);
    }
}
