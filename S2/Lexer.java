import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer {

    private List<Token> tokens;
    private int index;
    private List<Integer> newlines;
    private int indexNewlines;
    private List<String> data;
    private int indexData;

    public Lexer(String code) {
        index = 0;
        indexData = 0;
        indexNewlines = 0;
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

    public Token popWithNewlines() {
        popNewlines();
        return pop();
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
            return newlines.get(indexNewlines);
        }
        else {
            return 0;
        }
    }

    public int popNewlines() {
        if (index < newlines.size()) {
            return newlines.get(indexNewlines++);
        }
        else {
            return 0;
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
                String removed = input.substring(0, index);
                int newLineCount = countNewlines(removed);
                Matcher matcher = token.getMatcher(input.substring(index));
                if (matcher.lookingAt()) {
                    tokens.add(token);
                    newlines.add(newLineCount + 1);
                    String matchedText = matcher.group();
                    index += matchedText.length();
                    if (token == Token.REP) {
                        token = Token.DECIMAL;
                        tokens.add(token);
                        matchedText = matchedText.replaceFirst("^[^\\d]*", "");
                        matcher = token.getMatcher(matchedText);
                    }
                    if (token == Token.DECIMAL) {
                        if (matcher.lookingAt()) {  
                        String digit = matcher.group();
                        data.add(digit);
                        }
                    }
                    if (token == Token.HEX) {
                        if (matcher.lookingAt()) {
                            String hex = matcher.group();
                            data.add(hex);
                            }
                    }
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

    private int countNewlines(String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                count++;
            }
        }
        return count;
    }
}
