import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class FractalExplorer extends JFrame {
    public static void main(String[] args) {

        var fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
    }


    private JButton reset_button = new JButton();//кнопка ресета
    private JButton save_button = new JButton();

    private int rowsRemaining = 0;
    private JComboBox chooseFractal;
    private int screenSize;//размер экрана
    private FractalGenerator fractalGenerator;//ссылка на класс FractalGenerator
    private Rectangle2D.Double fractal;//окно комплексной плоскости
    private JImageDisplay image;//поле управляет изображением

    //конструктор класса
    public FractalExplorer(int ScreenSize){
        screenSize = ScreenSize;
        fractal = new Rectangle2D.Double();
        fractalGenerator = new Mandelbrot();
        fractalGenerator.getInitialRange(fractal);
    }

    public void enableUI(Boolean state) {//включение и выключение кнопок
        save_button.setEnabled(state);
        chooseFractal.setEnabled(state);
        reset_button.setEnabled(state);
    }

    //размещение элементов в контейнере frame
    public void createAndShowGUI (){
        JFrame frame = new JFrame();
        JPanel panel_south = new JPanel();
        JPanel panel_north = new JPanel();
        JLabel chooseFractalLabel = new JLabel("Fractal:");
        chooseFractal = new JComboBox();
        chooseFractal.addItem(new Mandelbrot());
        chooseFractal.addItem(new Tricorn());
        chooseFractal.addItem(new BurningShip());
        chooseFractal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractalGenerator = (FractalGenerator) chooseFractal.getSelectedItem();
                fractalGenerator.getInitialRange(fractal);
                drawFractal();
            }
        });

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        fileChooser.setAcceptAllFileFilterUsed(false);


        image = new JImageDisplay(screenSize,screenSize);

        image.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (rowsRemaining != 0)
                    return;
                var xCoord = FractalGenerator.getCoord(
                        fractal.x,
                        fractal.x + fractal.width,
                        screenSize, e.getX());
                var yCoord = FractalGenerator.getCoord(
                        fractal.y,
                        fractal.y + fractal.width,
                        screenSize, e.getY());
                double CLICK_NEW_SCALE;
                fractalGenerator.recenterAndZoomRange(fractal, xCoord, yCoord, 0.5);

                drawFractal();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                                       
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        reset_button = new JButton("RESET");
        reset_button.setAction(null);
        reset_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractalGenerator.getInitialRange(fractal);
                drawFractal();
            }
        });

        save_button = new JButton("SAVE");
        save_button.setAction(null);
        save_button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(frame)) {

                    try {
                        ImageIO.write(image.getBufferedImage(), "png", fileChooser.getSelectedFile());//сохранение

                    } catch (Exception exception) {

                        JOptionPane.showMessageDialog(frame, "Cannot save image " + exception.getMessage(), "Fractal explorer",
                                JOptionPane.ERROR_MESSAGE);//обработка в случае неправильного типа файла
                    }
                }
            }

        });

        panel_north.add(chooseFractalLabel);
        panel_north.add(chooseFractal);

        panel_south.add(save_button);
        panel_south.add(reset_button);

        frame.add(image,BorderLayout.CENTER);
        frame.add(panel_south,BorderLayout.SOUTH);
        frame.add(panel_north,BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack ();
        frame.setVisible (true);
        frame.setResizable (false);





    }

    //реализация фрактала
//    private void drawFractal() {
//        for (int x = 0; x < screenSize; x++) {
//            for (int y = 0; y < screenSize; y++) {
//                var color = 0;
//                var xCoord = FractalGenerator.getCoord(fractal.x, fractal.x + fractal.width, screenSize, x);
//                var yCoord = FractalGenerator.getCoord(fractal.y, fractal.y + fractal.height, screenSize, y);
//                int i = fractalGenerator.numIterations(xCoord, yCoord);
//                if (i != -1)
//                    image.drawPixel(x,y, Color.HSBtoRGB(0.7f + (float) i / 200f, 1f, 1f));
//                else
//                    image.drawPixel(x,y,color);
//            }
//        }
//        image.repaint();
//    }
    private void drawFractal() {//отключение кнопок и задать кол-ва строк
        rowsRemaining = screenSize;
        enableUI(false);
        for (int y = 0; y < screenSize; y++) {
            new FractalWorker(y).execute();
        }
    }

    class FractalWorker extends SwingWorker<Object, Object> {

        private int lineY;//координата Y или строка
        private int[] lineColors;//хранение значение RGB

        public FractalWorker(int lineY) {//конструктор
            this.lineY = lineY;
        }

        @Override
        protected Object doInBackground() {// рисование фрактала фоновым процессом
            lineColors = new int[screenSize];
            for (int x = 0; x < screenSize; x++) {

                var xCoord = FractalGenerator.getCoord(
                        fractal.x,
                        fractal.x + fractal.width,
                        screenSize, x);
                var yCoord = FractalGenerator.getCoord(
                        fractal.y,
                        fractal.y + fractal.width,
                        screenSize, lineY);
                var iterations = fractalGenerator.numIterations(xCoord, yCoord);

                var color = iterations == -1 ? 0
                        : Color.HSBtoRGB(0.7f + (float) iterations / 200f, 1f, 1f);
                lineColors[x] = color;
            }
            return null;
        }

        @Override
        protected void done() {//построчное приближение изображения
            for (int x = 0; x < screenSize; x++) {
                image.drawPixel(x, lineY, lineColors[x]);
            }
            image.repaint(0, lineY, screenSize, 1);
            rowsRemaining--;
            if (rowsRemaining == 0)
                enableUI(true);
        }
    }

}
