import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;

public class IFLabel extends IFComponent
{
    public Color clr;
    public String text;
    public boolean underlining;
    public Color underliningColor;
    public Color foregroundColor;
    public Font font;
    public int baselineAddition;
    public int positionType;
    
    //Mögliche positionTypes:
    public static int P_CENTER = 0;
    public static int P_LEFT_CENTER = 1;
    public static int P_RIGHT_CENTER = 2;
    public static int P_LEFT_TOP = 4;
    public static int P_RIGHT_TOP = 5;
    
    public IFLabel(int width, int height, int x, int y, String txt){
        super(width, height, x, y);
        
        clr = Color.WHITE;
        foregroundColor = Color.BLACK;
        font = new Font("Sans-Serif", Font.BOLD, 20);
        text = txt;
    }
    
    
    public void paintST(){
        fillCanvas(clr);
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