import java.util.regex.Pattern;
import java.util.regex.Matcher;

public enum Token {
    FORW ("forw[ \t\n]+"),
    BACK ("back[ \t\n]+"),
    LEFT ("left[ \t\n]+"),
    RIGHT ("right[ \t\n]+"),
    DOWN ("down"),
    UP ("up"),
    COLOR ("color[ \t\n]+"),
    REP ("rep[ \t\n]+\\d+[ \t\n]+"),
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