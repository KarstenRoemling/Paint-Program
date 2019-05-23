import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public class IFComponent extends Panel
{
    public int w;
    public int h;
    public BufferedImage b;

    public IFComponent(int width, int height)
    {
        w = width;
        h = height;
        b = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        setLayout(null);
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(b.getWidth(), b.getHeight());
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(b, null, null);
    }
    
    public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < b.getWidth(); x++) {
            for (int y = 0; y < b.getHeight(); y++) {
                b.setRGB(x, y, color);
            }
        }
        repaint();
    }
}
