import java.awt.*;
import java.awt.image.*;
import java.util.concurrent.*;
import java.awt.font.*;

//Für diese Klasse habe ich zwei ganze Tage gebraucht...

public class IFButton extends IFComponent
{
    public Color clr;
    public double cr = 0.0;//cornerRadius
    public int baselineAddition = 0;
    private int exPermission = 0;
    public String text;
    public Font font;
    public Color foregroundColor;
    
    public IFButton(int width, int height, int x, int y, String txt){
        super(width,height, x, y);
        
        clr = new Color(255,0,255);
        foregroundColor = Color.WHITE;
        font = new Font("Sans-Serif", Font.BOLD, 20);
        text = txt;
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        paintST();
    }
    
    public void paintST(){
        fillCanvas(new Color(255,255,255));
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
                            b.setRGB(bx, by, mixOfColors(new Color(b.getRGB(bx,by)), clr, (double)(Math.floor(num)+1.0)-(double)num).getRGB());
                        }
                        
                        double num2 = cr*(Math.pow((double)bx / cr, -1));
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
        repaint();
    }
    
    public int mixOf(double value1, double value2, double preference){
        return (int) ((value2 - value1) * preference + value1);
    }
    
    public Color mixOfColors(Color c1, Color c2, double pref){
        return new Color(mixOf((double)c1.getRed(), (double)c2.getRed(), pref),mixOf((double)c1.getGreen(), (double)c2.getGreen(), pref),mixOf((double)c1.getBlue(), (double)c2.getBlue(), pref));
    }
    
    public void setColor(Color color){
        clr = color;
        paintST();
    }
    
    public void setForegroundColor(Color fgClr){
        foregroundColor = fgClr;
        repaint();
    }
    
    public void setCornerRadius(double ncr){
        cr = ncr;
        paintST();
    }
    
    public void setText(String txt){
        text = txt;
        repaint();
    }
    
    public void setFont(Font fnt){
        font = fnt;
        repaint();
    }
    
    public void setBaselineAddition(int bslnAddtn){
        baselineAddition = bslnAddtn;//Das sind Variabelnamen... (-:
        repaint();
    }
    
    public void animCR(double result, double changePE){
        final double myPerm;
        if(result < cr){
            exPermission = -1;
            myPerm = -1;
        }else{
            exPermission = 1;
            myPerm = 1;
        }
        final double oldCR = cr;
        final ScheduledExecutorService eS = Executors.newSingleThreadScheduledExecutor();
        eS.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                boolean reached;
                if(myPerm == 1){
                    reached = cr >= result;
                }else{
                    reached = cr <= result;
                }
                if(reached || !(exPermission == myPerm)){
                    cr = result;
                    eS.shutdown();
                }else{
                    cr += changePE;
                    paintST();
                }
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }
    
    public void afterImage(Graphics2D g2){
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);
        
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, "Clear");
        int strHeight = (int)gv.getPixelBounds(null, 2, 2).getHeight();
        gv = g2.getFont().createGlyphVector(frc, text);
        int strWidth = (int)gv.getPixelBounds(null, 2, 2).getWidth();
        
        int x = (w - strWidth)/2;
        int y = (h - strHeight)/2 + h/2 + baselineAddition;
        g2.setPaint(foregroundColor);
        g2.drawString(text, x, y);
    }
}
