import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.util.Scanner;

public class Main {

    
    public static void main(String[] args) {
        String code = fileToString("code.txt");
        //try {
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);
        ParseTree parseTree = parser.parse();
        Program program = new Program(parseTree);
        program.execute();
        //}
        //catch (RuntimeException e) {
        //    System.out.println(e.getMessage());
        //}
        
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
    
        /*
    
        public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String code = "";
        while (sc.hasNextLine()) {
            code += sc.nextLine() + "\n";
        }
        try {
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer);
        ParseTree parseTree = parser.parse();
        Program program = new Program(parseTree);
        program.execute();
        }
        catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
        sc.close();
    }
     */
}

