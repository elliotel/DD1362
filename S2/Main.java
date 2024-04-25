import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    /*
    public static void main(String[] args) {
        String code = fileToString("code.txt");
        Lexer lexer = new Lexer(code);
        while (lexer.peek() != null) {
            System.out.println("Token: " + lexer.pop());
        }
    }
    */

    public static void main(String[] args) {
        String code = fileToString("code.txt");
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);
        parser.parse();
    }

    public static String fileToString(String fileLocation) {
        String code = "";
        try { 
            code = Files.readString(Paths.get(fileLocation));
        }
        catch (IOException e) {
            System.out.println("Specified file does not exist!");
        }
        return code;
    }
}

