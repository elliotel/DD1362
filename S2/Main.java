import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    
    public static void main(String[] args) {
        String code = fileToString("code.txt");
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);
        ParseTree parseTree = parser.parse();
        Program program = new Program(parseTree);
        program.execute();
        
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

