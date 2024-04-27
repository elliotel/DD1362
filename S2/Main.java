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
        ParseTree parseTree = parser.parse();
        execute(parseTree);
        
    }

    public static void execute(ParseTree parseTree) {
        ParseTree left = parseTree.getLeft();
        ParseTree right = parseTree.getRight();
        String data = parseTree.getData();
        if (left != null) {
            if (left.getLeft() != null) {
                if (left.getLeft().getLeft() != null && left.getLeft().getLeft().getData() == "REP") {
                    int reps = Integer.parseInt(left.getLeft().getRight().getData());
                    for (int i = 0; i < reps; i++) {
                    execute(left.getRight());
                    }
                }
                else {
                    execute(left);
                }
            }
            else {
                if (right != null) {
                    run(left.getData(), right.getData());
                }
                else {
                    run(left.getData(), null);
                }
            }
            if (right != null) {
                if (right.getLeft() != null) {
                    execute(right);
                }
            }
        }
        else {
            throw new RuntimeException("Syntax Error!");
        }
        
    }
    
    public static void run(String leftData, String rightData) {
        switch (leftData) {
            case "FORW":
                int forward = Integer.parseInt(rightData);
                System.out.println("Walk forward " + forward + " steps.");
                break;
            case "BACK":
                int backwards = Integer.parseInt(rightData);
                System.out.println("Walk backwards " + backwards + " steps.");
                break;
            case "LEFT":
                int left = Integer.parseInt(rightData);
                System.out.println("Rotating pen " + left + " degrees to the left.");
                break;
            case "RIGHT":
                int right = Integer.parseInt(rightData);
                System.out.println("Rotating pen " + right + " degrees to the right.");
                break;
            case "UP":
                System.out.println("Raising pen.");
                break;
            case "DOWN":
                System.out.println("Lowering pen.");
                break;
            case "COLOR":
                String hex = rightData;
                System.out.println("Changing color to " + hex + ".");
                break;
            
        }
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

