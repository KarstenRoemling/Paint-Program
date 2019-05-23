import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.font.*;

public class IFComponent extends Panel
{
    public int w;
    public int h;
    public int x;
    public int y;
    public BufferedImage b;

    public IFComponent(int width, int height, int xx, int yy)
    {
        w = width;
        h = height;
        x = xx;
        y = yy;
        b = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        setBounds(x,y,w,h);
        setLayout(null);
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(b.getWidth(), b.getHeight());
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        
        beforeImage(g2);
        
        g2.drawImage(b, null, null);
        
        afterImage(g2);
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
    
    public void afterImage(Graphics2D g2){}
    
    public void beforeImage(Graphics2D g2){}
}
