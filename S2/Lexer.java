import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Lexer {

    private List<Token> tokens;
    private int index;
    private List<String> data;
    private int indexData;

    public Lexer(String code) {
        index = 0;
        indexData = 0;
        data = new ArrayList<>();
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
            System.out.println(tokens.get(index));
            return tokens.get(index++);
        }
        else {
            return null;
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
                Matcher matcher = token.getMatcher(input.substring(index));
                if (matcher.lookingAt()) {
                    tokens.add(token);
                    String matchedText = matcher.group();
                    index += matchedText.length();
                    if (token == Token.REP) {
                        token = Token.DECIMAL;
                        tokens.add(token);
                        matchedText = matchedText.replaceFirst("^[^\\d]*", "");
                        matcher = token.getMatcher(matchedText);
                    }
                    if (token == Token.DECIMAL) {
                        System.out.println("test");
                        if (matcher.lookingAt()) {
                        String digit = matcher.group();
                        data.add(digit);
                        System.out.println("MATCH: " + data);
                        }
                    }
                    if (token == Token.HEX) {
                        if (matcher.lookingAt()) {
                            String hex = matcher.group();
                            data.add(hex);
                            System.out.println("MATCH: " + data);
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
        //for(String s : data) {
        //    System.out.println(s);
        //}
        return tokens;
    }
}
