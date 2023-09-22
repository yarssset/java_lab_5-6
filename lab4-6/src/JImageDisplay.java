import javax.swing.JComponent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.image.RenderedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class JImageDisplay extends JComponent {
    private BufferedImage image;

    public JImageDisplay(int width,int height) {
         image = new BufferedImage(width, height,TYPE_INT_RGB);
         super.setPreferredSize(new Dimension(width,height));
    }
    //метод рисующий изображение
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        g.drawImage (image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    //метод делающий пиксели чёрными
    public void clearImage() {
        for (int i = 0; i < this.image.getWidth(); ++i) {
            for (int j = 0; j < this.image.getHeight(); ++j) {
                image.setRGB(i, j, 0);
            }
        }
    }

    public java.awt.image.BufferedImage getBufferedImage() {
        return image;
    }

    //устанавливает пиксель в опр. цвет
    public void drawPixel (int x, int y, int rgbColor){
        image.setRGB(x, y, rgbColor);
    }

}

