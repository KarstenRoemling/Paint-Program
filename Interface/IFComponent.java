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
    
    public static int mixOf(double value1, double value2, double preference){
        return (int) ((value2 - value1) * preference + value1);
    }
    
    public static Color mixOfColors(Color c1, Color c2, double pref){
        return new Color(mixOf((double)c1.getRed(), (double)c2.getRed(), pref),mixOf((double)c1.getGreen(), (double)c2.getGreen(), pref),mixOf((double)c1.getBlue(), (double)c2.getBlue(), pref));
    }
    
    public void roundedBg(double cr, Color clr){
        int color = clr.getRGB();
        for(int by = 0; by < h; by++){
            for(int bx = 0; bx < w; bx++){
                if(by <= h/2){
                    if(bx <= w/2){
                        //PROBIEREN DIE DIE ANDEREN MÖGLICHKEITEN GERNE AUCH AUS! TW. AMÜSANT.
                        //double num = cr*(1-Math.sin(0.5 * Math.PI *((double)by / (double)cr)));
                        //double num = cr*(Math.pow(((double)cr-(double)by) / (double)cr, 2));
                        double num = cr*(Math.pow((double)by / cr, -1));
                        if(cr <= 0){
                            num = 0;
                        }
                        if(bx > (int)num){
                            b.setRGB(bx, by, color);
                        }
                        else if(bx == (int)num){
                            b.setRGB(bx, by, mixOfColors(Color.WHITE, clr, (double)(Math.floor(num)+1.0)-(double)num).getRGB());
                        }
                        else{
                            b.setRGB(bx, by, Color.WHITE.getRGB());
                        }
                        
                        final double num2 = cr*(Math.pow((double)bx / cr, -1));
                        if(by == (int)num2){
                            b.setRGB(bx, by, mixOfColors(new Color(b.getRGB(bx,by)), clr, (double)(Math.floor(num2)+1.0)-(double)num2).getRGB());
                        }
                    }
                    else{
                        b.setRGB(bx, by, b.getRGB(w-bx-1, by));
                    }
                }
                else{
                    b.setRGB(bx, by, b.getRGB(bx, h-by-1));
                }
            }
        }
    }
    
    public void roundedBgWithBorder(double cr, Color clr, double border, Color bclr){
        int color = clr.getRGB();
        int bcolor = bclr.getRGB();
        for(int by = 0; by < h; by++){
            for(int bx = 0; bx < w; bx++){
                if(by <= h/2){
                    if(bx <= w/2){
                        double num = cr*(Math.pow((double)by / cr, -1));
                        double numBorder = cr*(Math.pow(((double)by - (double)border) / (cr+1), -1)) + border;
                        if(cr <= 0){
                            num = 0;
                        }
                        if(bx > (int)num){
                            if(bx > (int)numBorder && by>border && bx>border){
                                b.setRGB(bx, by, bcolor);
                            }else if(bx == (int)numBorder && by>border && bx>border){
                                b.setRGB(bx, by, mixOfColors(clr, bclr, (double)(Math.floor(numBorder)+1.0)-(double)numBorder).getRGB());
                            }else{
                                b.setRGB(bx, by, color);
                            }
                        }
                        else if(bx == (int)num){
                            b.setRGB(bx, by, mixOfColors(Color.WHITE, clr, (double)(Math.floor(num)+1.0)-(double)num).getRGB());
                        }
                        else{
                            b.setRGB(bx, by, Color.WHITE.getRGB());
                        }
                        
                        final double num2 = cr*(Math.pow((double)bx / cr, -1));
                        final double num2Border = cr*(Math.pow(((double)bx - (double)border) / (cr+1), -1)) + border;
                        if(by == (int)num2){
                            b.setRGB(bx, by, mixOfColors(new Color(b.getRGB(bx,by)), clr, (double)(Math.floor(num2)+1.0)-(double)num2).getRGB());
                        }
                        if(by == (int)num2Border && bx>border && by>border){
                           b.setRGB(bx, by, mixOfColors(new Color(b.getRGB(bx,by)), bclr, (double)(Math.floor(num2Border)+1.0)-(double)num2Border).getRGB());
                        }
                    }
                    else{
                        b.setRGB(bx, by, b.getRGB(w-bx-1, by));
                    }
                }
                else{
                    b.setRGB(bx, by, b.getRGB(bx, h-by-1));
                }
            }
        }
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
