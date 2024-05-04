import java.lang.Math;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Program {

    private double x;
    private double y;
    private int angle;
    private double cosAngle;
    private double sinAngle;
    private Boolean angleChanged;
    private String color;
    private Boolean drawing;
    private DecimalFormat df;

    ParseTree parseTree;

    public Program(ParseTree parseTree) {
        this.parseTree = parseTree;
        x = 0;
        y = 0; 
        angle = 0;
        cosAngle = 0;
        sinAngle = 0;
        angleChanged = true;
        color = "#0000FF";
        drawing = false;
        df = new DecimalFormat("#0.0000");
        df.setRoundingMode(RoundingMode.HALF_UP);
    }

    public void execute() {
        execute(parseTree);
    }

    private void execute(ParseTree parseTree) {
        if (parseTree == null) {
            return;
        }
        ParseTree left = parseTree.getLeft();
        ParseTree right = parseTree.getRight();
        Boolean rep = false;
        if (left != null) {
            if (left.getLeft() != null) {
                if (left.getLeft().getData() == "REP") {
                    int reps = Integer.parseInt(left.getRight().getData());
                    for (int i = 0; i < reps; i++) {
                    execute(right);
                    rep = true;
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
                    if (!rep) {
                    execute(right);
                    }
                }
            }
        }
    }
    
    private void run(String leftData, String rightData) {
        switch (leftData) {
            case "FORW":
                int forward = Integer.parseInt(rightData);
                double oldXfw = x;
                double oldYfw = y;
                if (angleChanged) {
                    cosAngle = Math.cos(Math.PI*angle/180);
                    sinAngle = Math.sin(Math.PI*angle/180);
                    angleChanged = false;
                }
                x += forward * cosAngle;
                y += forward * sinAngle;
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
                if (x > -0.00005 && x < 0) { x = 0; }
                if (y > -0.00005 && y < 0) { y = 0; }
                if (drawing) {
                    System.out.println(color + " " + df.format(oldXbw) + " " + df.format(oldYbw) + " " + df.format(x) + " " + df.format(y));
                }
                break;
            case "LEFT":
                int left = Integer.parseInt(rightData);
                angle += left;
                angleChanged = true;
                break;
            case "RIGHT":
                int right = Integer.parseInt(rightData);
                angle -= right;
                angleChanged = true;
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
