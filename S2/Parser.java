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
            lexer.pop();
        }
        if (lexer.peek() != null) {
        ParseTree left = command();
        ParseTree right = null;
        while (lexer.peek() == Token.COMMENT) {
            lexer.pop();
        }
        if (lexer.peek() != Token.QUOTE) {
            right = commandSequence();
        }
        return new ParseTree(left, right);
        }
        else
        return null;
    }

    private ParseTree command() {
        while (lexer.peek() == Token.COMMENT) {
            lexer.pop();
        }
        Token next = lexer.peek();
        if (next == null) {
            return null;
        }
        switch (next) {
            case REP:
                return repeat();
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
            case ERROR:
                throw new RuntimeException("Syntaxfel på rad " + lexer.peekNewlines());
            default:
                throw new RuntimeException("Syntaxfel på rad " + lexer.peekNewlines());
        }
    }

    private ParseTree movement() {
        int line = lexer.peekNewlines();
        Token move = lexer.pop();
        ParseTree left = new ParseTree(null, null, move.name());
        ParseTree right;
        try {
        right = decimal();
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        line = lexer.peekNewlines();
        try {
            checkPeriod();
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        return new ParseTree(left, right);
    }

    private ParseTree decimal() {
        int line = lexer.peekNewlines();
        Token curr = lexer.pop();
        while (curr == Token.COMMENT) {
            line = lexer.peekNewlines();
            curr = lexer.pop();
        }
        if (curr == null) {
            throw new IllegalArgumentException();
        }
        else if (curr != Token.DECIMAL) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        String decimal = lexer.nextData();
        if (Integer.parseInt(decimal) == 0) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        return new ParseTree(null, null, decimal);
    }

    private ParseTree turn() {
        int line = lexer.peekNewlines();
        Token direction = lexer.pop();
        ParseTree left = new ParseTree(null, null, direction.name());
        ParseTree right;
        try {
        right = decimal();
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        line = lexer.peekNewlines();
        try {
            checkPeriod();
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        return new ParseTree(left, right);
    }

    private ParseTree brushMode() {
        int line = lexer.peekNewlines();
        Token mode = lexer.pop();
        ParseTree left = new ParseTree(null, null, mode.name());
        try {
            checkPeriod();
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        return new ParseTree(left);
    }

    private ParseTree colorChange() {
        int line = lexer.peekNewlines();
        lexer.pop();
        ParseTree left = new ParseTree(null, null, Token.COLOR.name());
        ParseTree right;
        try {
            right = hexCode();
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        line = lexer.peekNewlines();
        try {
            checkPeriod();
        }
        catch (IllegalArgumentException e) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        return new ParseTree(left, right);
    }

    private ParseTree hexCode() {
        int line = lexer.peekNewlines();
        Token curr = lexer.pop();
        while (curr == Token.COMMENT) {
            line = lexer.peekNewlines();
            curr = lexer.pop();
        }
        if (curr == null) {
            throw new IllegalArgumentException();
        }
        else if (curr != Token.HEX) {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        String hex = lexer.nextData();
        return new ParseTree(null, null, hex);
    }
    
    private ParseTree repeat() {
        lexer.pop();
        int line = lexer.peekNewlines();
        Token next = lexer.peek();
        ParseTree innerLeft = new ParseTree(null, null, Token.REP.name());
        ParseTree innerRight = null;
        while (next == Token.COMMENT) {
            lexer.pop();
            line = lexer.peekNewlines();
            next = lexer.peek();
        }
        if (next == Token.DECIMAL) {
            
            try {
            innerRight = decimal();
            }
            catch (IllegalArgumentException e) {
                throw new RuntimeException("Syntaxfel på rad " + line);
            }
        }
        else {
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
        ParseTree left = new ParseTree(innerLeft, innerRight);
        ParseTree right = null;
        line = lexer.peekNewlines();
        next = lexer.peek();
        while (next == Token.COMMENT) {
            lexer.pop();
            line = lexer.peekNewlines();
            next = lexer.peek();
        }
        if (next == Token.QUOTE) {
            lexer.pop();
            while (lexer.peek() == Token.COMMENT) {
                line = lexer.peekNewlines();
                lexer.pop();
            }
            right = commandSequence();
            line = lexer.peekNewlines();
            while (lexer.peek() == Token.COMMENT) {
                line = lexer.peekNewlines();
                lexer.pop();
            }
            if (lexer.peek() != Token.QUOTE) {
                throw new RuntimeException("Syntaxfel på rad " + line);
            }
            lexer.pop();
        }
        else if (next == Token.REP) {
            right = repeat();
        }
        else {
            right = command();
        }
        return new ParseTree(left, right);
    }

    private void checkPeriod() {
        int line = lexer.peekNewlines();
        Token curr = lexer.pop();
        while (curr == Token.COMMENT) {
            curr = lexer.pop();
        }
        if (curr != Token.PERIOD) {
            if (curr == null) {
                throw new IllegalArgumentException();
            }
            line = lexer.peekNewlines();
            throw new RuntimeException("Syntaxfel på rad " + line);
        }
    }
}