public class Parser {
    private Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public ParseTree parse() {
        if (lexer.peek() != null) {
        return command();
        }
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
                break;
            case LEFT:
            case RIGHT:
                return turn();
                break;
            case UP:
            case DOWN:
                return brushMode();
                break;
            case COLOR:
                return colorChange();
                break;
            case REP:
                return repeat();
                break;
            default:
                throw new RuntimeException("Syntax Error!");
        }
    }

    private ParseTree movement() {
        lexer.pop();
        decimal();
    }

    private void decimal() {
        Token next = lexer.pop();
        if (next != Token.DECIMAL) {
            throw new RuntimeException("Syntax Error!");
        }
        String decimal = lexer.nextData();
        System.out.println(decimal);
    }

    private void turn() {
        lexer.pop();
    }

    private void brushMode() {
        lexer.pop();
    }

    private void colorChange() {
        lexer.pop();
        hexCode();
    }

    private void hexCode() {
        Token next = lexer.pop();
        if (next != Token.HEX) {
            throw new RuntimeException("Syntax Error!");
        }
        String hex = lexer.nextData();
        System.out.println(hex);
    }
    
    private void repeat() {
        lexer.pop();
        Token next = lexer.peek();
        if (next == Token.DECIMAL) {
            decimal();
        }
        else {
            throw new RuntimeException("Syntax Error!");
        }
        next = lexer.peek();
        if (next == Token.QUOTE) {
            lexer.pop();
            commandSequence();
            lexer.pop();
        }
        else {
            command();
        }
    }

    private void commandSequence() {
        command();
        if (lexer.peek() != Token.QUOTE) {
            commandSequence();
        }
    }
}
