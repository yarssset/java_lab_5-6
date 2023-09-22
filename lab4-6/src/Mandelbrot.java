import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {

    public static final int MAX_ITERATIONS = 2000;

    //метод устанавливает начальный диапозон  (-2 - 1.5i) - (1 + 1.5i)
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x=-2;
        range.y=-1.5;
        range.width  = 3;
        range.height = 3;
    }

    //метод итеративной функции для фрактала Мандельброта  zn = z(n-1)^2 + c
    @Override
    public int numIterations(double x, double y) {
        var c = new Complex(x,y);
        var z0 = new Complex (0,0);

        for (var iteration = 0;iteration< MAX_ITERATIONS;iteration++){
            if (z0.compareModulePow2(4)>=0) {
                return iteration;
            }
            z0.InSquare();
            z0.Add(c);
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Mandelbrot";
    }
}
