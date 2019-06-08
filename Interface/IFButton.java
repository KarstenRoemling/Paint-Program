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
        
        clr = new Color(10,30,100);
        foregroundColor = Color.WHITE;
        font = new Font("Dosis", Font.BOLD, 20);
        text = txt;
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        paintST();
    }
    
    public void paintST(){
        roundedBg(cr, clr);
        repaint();
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
        int y = (h - strHeight)/2 + strHeight + baselineAddition;
        g2.setPaint(foregroundColor);
        g2.drawString(text, x, y);
    }
}
