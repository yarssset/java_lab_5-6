import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x=-2;
        range.y=-2;
        range.width  = 3;
        range.height = 3;
    }

    @Override
    public int numIterations(double x, double y) {
        var c = new Complex(x,y);
        var z0 = new Complex (0,0);

        for (var iteration = 0;iteration< MAX_ITERATIONS;iteration++){
            if (z0.compareModulePow2(4)>=0) {
                return iteration;
            }
            z0.InSquare2();
            z0.Add(c);
        }
        return -1;
    }
    @Override
    public String toString() {
        return "Tricorn";
    }
}
