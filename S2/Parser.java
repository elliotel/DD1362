public class Parser {
    private Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void parse() {
        while (lexer.peek() != null) {
        command();
        }
    }

    private void command() {
        Token next = lexer.peek();
        if (next == null) {
            return;
        }
        switch (next) {
            case ERROR:
                throw new RuntimeException("Syntax Error!");
            case FORW:
            case BACK:
                movement();
                break;
            case LEFT:
            case RIGHT:
                turn();
                break;
            case UP:
            case DOWN:
                brushMode();
                break;
            case COLOR:
                colorChange();
                break;
            case REP:
                repeat();
                break;
            default:
                throw new RuntimeException("Syntax Error!");
        }
    }

    private void movement() {
        lexer.pop();
        decimal();
    }

    private void decimal() {
        Token next = lexer.pop();
        if (next != Token.DECIMAL) {
            throw new RuntimeException("Syntax Error!");
        }
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
