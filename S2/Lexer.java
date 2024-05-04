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
    private int quoteCount;

    public Lexer(String code) {
        index = 0;
        indexData = 0;
        indexNewlines = 0;
        quoteCount = 0;
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
        if (indexNewlines < newlines.size()) {
            return newlines.get(indexNewlines);
        }
        else {
            return newlines.get(newlines.size()-1);
        }
    }

    public int popNewlines() {
        if (indexNewlines < newlines.size()) {
            return newlines.get(indexNewlines++);
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
                index++;
            }
            if (index >= input.length()) {
                break;
            }
    
            boolean tokenMatched = false;
            for (Token token : Token.values()) {
                String text = input.substring(index);
                Matcher matcher = token.getMatcher(text);
                if (matcher.lookingAt()) {
                    String removed = input.substring(0, index);
                    int newLineCount = countNewlines(removed);
                    if (token == Token.QUOTE) {
                        int comments = 0;
                        while (tokens.get(tokens.size() - 1 - comments) == Token.COMMENT) {
                            comments++;
                        }
                        while (tokens.get(tokens.size() - 2 - comments) == Token.COMMENT) {
                            comments++;
                        }
                        if (tokens.get(tokens.size() - 2 - comments) == Token.REP) {
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
                    newlines.add(newLineCount + 1);
                    String matchedText = matcher.group();
                    index += matchedText.length();
                    /*
                    if (token == Token.REP) {
                        token = Token.DECIMAL;
                        tokens.add(token);
                        newlines.add(newLineCount + 1);
                        //for (int ind = 0; ind  < matchedText.length(); ind++) {
                        //    if (matchedText.charAt(ind) == '%') {
                        //        String start = matchedText.substring(0, ind);
                        //        String end = matchedText.substring(ind).replaceFirst("^[^\n]*", "");
                        //        matchedText = start + end;
                        //        ind = 0;
                        //    }
                        //}
                        matchedText = matchedText.replaceFirst("^[^\\d]*", "");
                        matcher = token.getMatcher(matchedText);
                    } */
                    if (token == Token.REP) {
                        rep = true;
                    }
                    if (token == Token.DECIMAL) {
                        if (matcher.lookingAt()) {  
                        String digit = matcher.group();
                        if (rep) {
                            char c = text.charAt(digit.length());
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
