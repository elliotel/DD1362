import java.lang.Math;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Program {

    private double x = 0;
    private double y = 0;
    private int angle = 0;
    private String color = "#0000FF";
    private Boolean drawing = false;

    ParseTree parseTree;

    public Program(ParseTree parseTree) {
        this.parseTree = parseTree;
    }

    public void execute() {
        execute(parseTree);
    }

    private void execute(ParseTree parseTree) {
        ParseTree left = parseTree.getLeft();
        ParseTree right = parseTree.getRight();
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
    
    private void run(String leftData, String rightData) {
        DecimalFormat df = new DecimalFormat("#0.0000");
        df.setRoundingMode(RoundingMode.HALF_UP);
        switch (leftData) {
            case "FORW":
                int forward = Integer.parseInt(rightData);
                double oldXfw = x;
                double oldYfw = y;
                x += forward * Math.cos(Math.PI*angle/180);
                y += forward * Math.sin(Math.PI*angle/180);
                if (x > -0.00005 && x < 0) { x = 0; }
                if (y > -0.00005 && y < 0) { y = 0; }
                if (drawing) {
                    System.out.println(color + " " + df.format(oldXfw) + " " + df.format(oldYfw) + " " + df.format(x) + " " + df.format(y));
                }
                break;
            case "BACK":
                int backward = Integer.parseInt(rightData);
                double oldXbw = x;
                double oldYbw = y;
                x -= backward * Math.cos(Math.PI*angle/180);
                y -= backward * Math.sin(Math.PI*angle/180);
                if (x == -0) { x = 0; }
                if (y == -0) { y = 0; }
                if (drawing) {
                    System.out.println(color + " " + df.format(oldXbw) + " " + df.format(oldYbw) + " " + df.format(x) + " " + df.format(y));
                }
                break;
            case "LEFT":
                int left = Integer.parseInt(rightData);
                angle += left;
                break;
            case "RIGHT":
                int right = Integer.parseInt(rightData);
                angle -= right;
                break;
            case "UP":
                drawing = false;
                break;
            case "DOWN":
                drawing = true;
                break;
            case "COLOR":
                String hex = rightData;
                color = hex;
                break;
            
        }
    }
}
