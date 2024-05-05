import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer {

    private List<Token> tokens;
    private int index;
    private List<Integer> newlines;
    private List<String> data;
    private int indexData;
    private int quoteCount;
    private int line;

    public Lexer(String code) {
        index = 0;
        indexData = 0;
        quoteCount = 0;
        line = 1;
        data = new ArrayList<>();
        newlines = new ArrayList<>();
        tokens = tokenize(code);
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
            return tokens.get(index++);
        }
        else {
            return null;
        }
    }
    
    public int peekNewlines() {
        if (index < newlines.size()) {
            return newlines.get(index);
        }
        else {
            return newlines.get(newlines.size()-1);
        }
    }

    public String nextData() {
        if (indexData < data.size()) {
            return data.get(indexData++);
        }
        else {
            return null;
        }
    }

    private List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int index = 0;
        boolean rep = false;
        while (index < input.length()) {
            //Hoppar över blanksteg
            while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
                if (input.charAt(index) == '\n') {
                    line++;
                }
                index++;
            }
            if (index >= input.length()) {
                break;
            }
    
            for (Token token : Token.values()) {
                Matcher matcher = token.getMatcher(input);
                matcher.region(index, input.length());
                if (matcher.lookingAt()) {
                    newlines.add(line);
                    if (token == Token.COMMENT) {
                        line++;
                    }
                    if (token == Token.QUOTE) {
                        int comments = 0;
                        while (tokens.size() > comments && tokens.get(tokens.size() - 1 - comments) == Token.COMMENT) {
                            comments++;
                        }
                        while (tokens.size() > comments + 1 && tokens.get(tokens.size() - 2 - comments) == Token.COMMENT) {
                            comments++;
                        }
                        if (tokens.size() > comments + 1 && tokens.get(tokens.size() - 2 - comments) == Token.REP) {
                            quoteCount++;
                        }
                        else {
                            quoteCount--;
                        }
                        if (quoteCount < 0) {
                            tokens.add(Token.ERROR);
                        }
                        else {
                            tokens.add(token);
                        }
                    }
                    else {
                        tokens.add(token);
                    }
                    if (token == Token.REP) {
                        rep = true;
                    }
                    if (token == Token.DECIMAL) {
                        if (matcher.lookingAt()) {  
                        String digit = matcher.group();
                        if (rep) {
                            char c = input.charAt(index + digit.length());
                            if (!(Character.isWhitespace(c) || c == '%')) {
                                tokens.add(Token.ERROR);
                            }
                            rep = false;
                        }
                        data.add(digit);
                        }
                    }
                    if (token == Token.HEX) {
                        if (matcher.lookingAt()) {
                            String hex = matcher.group();
                            data.add(hex);
                            }
                    }
                    String matchedText = matcher.group();
                    if (token == Token.REP || token == Token.FORW || token == Token.BACK || token == Token.LEFT || token == Token.RIGHT || token == Token.COLOR) {
                        for (int i = index; i < index + matchedText.length(); i++) {
                            if (input.charAt(i) == '\n') {
                                line++;
                            }
                        }
                    }
                    index += matchedText.length();
                    break;
                }
            }
        }
        return tokens;
    }
}