public class Complex {
    private double x;
    private double y;

    public Complex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // |Z|^2
    public double ModulePow2() {
        return this.x * this.x + this.y * this.y;
    }

    // GET/SET
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    // ADDICTION
    public void Add(Complex xy) {
        this.x += xy.getX();
        this.y += xy.getY();
    }
    
    public void InSquare() {
        double tempX = this.x;
        double tempY = this.y;
        this.x = tempX * tempX - tempY * tempY;
        this.y = 2 * tempX * tempY;
    }

    public void InSquare2() {
        double tempX = this.x;
        double tempY = this.y;
        this.x = tempX * tempX - tempY * tempY;
        this.y = -2 * tempX * tempY;
    }

    public void InSquare3() {
        double tempX = this.x;
        double tempY = this.y;
        this.x = tempX * tempX - tempY * tempY;
        this.y = Math.abs(2 * tempX * tempY);
    }

    // COMPARE
    public int compareModulePow2(double number) {
        return Double.compare(this.ModulePow2(), number);
    }


}
