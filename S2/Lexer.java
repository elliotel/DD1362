import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer {

    private List<Token> tokens;
    private int index;

    public Lexer(String code) {
        tokens = tokenize(code);
        index = 0;
    }

    public Token peek() {
        if (index < tokens.size()) {
            return tokens.get(index);
        }
        else {
            return null;
        }
    }

    public Token pop() {
        if (index < tokens.size()) {
            System.out.println(tokens.get(index));
            return tokens.get(index++);
        }
        else {
            return null;
        }
    }

    private List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int index = 0;
        while (index < input.length()) {
            //Hoppar över blanksteg
            while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
                index++;
            }
            if (index >= input.length()) {
                break;
            }
    
            boolean tokenMatched = false;
            for (Token token : Token.values()) {
                Matcher matcher = token.getMatcher(input.substring(index));
                if (matcher.lookingAt()) {
                    String matchedText = matcher.group();
                    tokens.add(token);
                    if (token == Token.REP) {
                        tokens.add(Token.DECIMAL);
                    }
                    index += matchedText.length();
                    tokenMatched = true;
                    break;
                }
            }
            if (!tokenMatched) {
                index++;
            }
        }
    
        return tokens;
    }
}
