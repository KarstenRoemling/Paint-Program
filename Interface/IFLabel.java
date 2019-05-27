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
    public static int P_CENTER_TOP = 3;
    public static int P_LEFT_TOP = 4;
    public static int P_RIGHT_TOP = 5;
    public static int P_CENTER_BOTTOM = 6;
    public static int P_LEFT_BOTTOM = 7;
    public static int P_RIGHT_BOTTOM = 8;
    
    public IFLabel(int width, int height, int x, int y, String txt){
        super(width, height, x, y);
        
        clr = Color.WHITE;
        foregroundColor = Color.BLACK;
        underliningColor = Color.BLACK;
        font = new Font("Dosis", Font.BOLD, 20);
        text = txt;
        
        paintST();
    }
    
    
    public void paintST(){
        fillCanvas(clr);
        repaint();
    }
    
    public void setText(String txt){
        text = txt;
        repaint();
    }
    
    public void setColor(Color c){
        clr = c;
        repaint();
    }
    
    public void setForegroundColor(Color c){
        foregroundColor = c;
        repaint();
    }
    
    public void setUnderliningColor(Color c){
        underliningColor = c;
        repaint();
    }
    
    public void setFont(Font fnt){
        font = fnt;
        repaint();
    }
    
    public void setBaselineAddition(int bla){
        baselineAddition = bla;
        repaint();
    }
    
    public void setPositionType(int pt){
        positionType = pt;
        repaint();
    }
    
    public void setUnderlining(boolean ul){
        underlining = ul;
        repaint();
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
        
        int x = 0;
        int y = 0;
        
        switch(positionType)
        {
            case 0:
            case 3:
            case 6:
                x = (w - strWidth)/2;
                break;
            case 1:
            case 4:
            case 7:
                x = 0;
                break;
            case 2:
            case 5:
            case 8:
                x = w - strWidth;
                break;
        }
        switch(positionType)
        {
            case 0:
            case 1:
            case 2:
                y = (h - strHeight)/2 + strHeight + baselineAddition;
                break;
            case 3:
            case 4:
            case 5:
                y = strHeight;
                break;
            case 6:
            case 7:
            case 8:
                y = h - strHeight;
                break;
        }
        
        if(underlining){
            g2.setPaint(underliningColor);
            g2.drawLine(x,y+2,x+strWidth,y+2);
        }
        
        g2.setPaint(foregroundColor);
        g2.drawString(text, x, y);
    }
}