import edu.princeton.cs.algs4.StdDraw;

public class SolidRect {
    
    private double xLeft, xRight, yTop, yBottom;
    
    public void draw() {
        
        double x = (xLeft + xRight) / 2;
        double y = (yBottom + yTop) / 2;
        
        StdDraw.filledRectangle(x, y, x - xLeft, y - yBottom);
    }
    
    public static void main(String[] args) {
        
        SolidRect rect = new SolidRect();
        
    }
}