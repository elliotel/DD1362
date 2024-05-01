import java.util.regex.Pattern;
import java.util.regex.Matcher;

public enum Token {
    FORW ("forw([ \t\r\n]+|(?:%[^\r\n]*\r?\n))"),
    BACK ("back([ \t\r\n]+|(?:%[^\r\n]*\r?\n))"),
    LEFT ("left([ \t\r\n]+|(?:%[^\r\n]*\r?\n))"),
    RIGHT ("right([ \t\r\n]+|(?:%[^\r\n]*\r?\n))"),
    DOWN ("down"),
    UP ("up"),
    COLOR ("color([ \t\r\n]+|(?:%[^\r\n]*\r?\n))"),
    //REP ("rep[ \t\r\n]+\\d+[ \t\r\n]+"),
    REP ("rep([ \t\r\n]|(?:%[^\r\n]*\r?\n))+(\\d+|%.*?\r?\n[ \t\r\n]*\\d+)([ \t\r\n]+|(?:%[^\r\n]*\r?\n))"),
    PERIOD ("\\."),
    QUOTE ("\""),
    DECIMAL ("\\d+"),
    HEX ("#([A-Fa-f0-9]{6})"),
    COMMENT ("%[^\r\n]*\r?\n"),
    ERROR (".*");

    private Pattern pattern;
    
    Token(String regex) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Matcher getMatcher(String input) {
        return pattern.matcher(input);
    }

}