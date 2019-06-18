import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.util.*;

/**
 * Die Klasse IFLabel erbt von IFComponent und ist ein wichtiges Element unserer Benutzeroberfläche.
 * Sie ist darauf spezialisiert, Texte unterschiedlich anzuzeigen. So verfügt sie beispielsweise über 9 verschiede Positionierungen des Texts innerhalb des IFLabels, macht Unterstreichungen in unterschiedlichen Farben möglich et cetera.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */


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
    
    /**
     * Konstruktormethode der Klasse IFLabel: Initialisiert Attribute, ruft die Konstruktormethode der Parent Class auf, zeichnet das Label...
     * 
     * @param width     Breite
     * @param height     Höhe
     * @param x     x-Position im Frame
     * @param y     y-Position im Frame
     * @param txt     Text des Labels
     */
    public IFLabel(int width, int height, int x, int y, String txt){
        super(width, height, x, y);
        
        clr = Color.WHITE;
        foregroundColor = Color.BLACK;
        underliningColor = Color.BLACK;
        font = new Font("Dosis", Font.BOLD, 20);
        text = txt;
        
        paintST();
    }
    
    /**
     * Füllt den Hintergrund des IFLabels mit der Hintergrundfarbe. Zeichnet das IFLabel neu.
     */
    public void paintST(){
        fillBI(clr);
        repaint();
    }
    
    /**
     * Definiert, welchen Text das IFLabel haben soll. Zeichnet das IFLabel neu.
     * 
     * @param txt     Der String gibt an, welchen Text das IFLabel haben soll.
     */
    public void setText(String txt){
        text = txt;
        repaint();
    }
    
    /**
     * Definiert, welche Hintergrundfarbe das Label haben soll. Zeichnet das Label neu.
     * 
     * @param c     Der Farbwert gibt an, welche Hintergrundfarbe das Label haben soll.
     */
    public void setColor(Color c){
        clr = c;
        paintST();
    }
    
    /**
     * Definiert, welche Schriftfarbe das Label. Zeichnet das Label neu.
     * 
     * @param c     Der Farbwert gibt an, welche Schriftfarbe das Label haben soll.
     */
    public void setForegroundColor(Color c){
        foregroundColor = c;
        repaint();
    }
    
    /**
     * Definiert, welche unterstreichende Farbe das Label. Zeichnet das Label neu.
     * 
     * @param c     Der Farbwert gibt an, welche unterstreichende Farbe das Label haben soll.
     */
    public void setUnderliningColor(Color c){
        underliningColor = c;
        repaint();
    }
    
    /**
     * Definiert, welche Schriftart, Schriftdicke und welcher Schrifttyp (plain, bold etc.) der Text des Labels haben soll. Zeichnet das Label neu.
     * 
     * @param f     Das Font-Objekt gibt an, welche Schriftart, Schriftdicke und welcher Schrifttyp (plain, bold etc.) der Text des Labels haben soll.
     */
    public void setFont(Font fnt){
        font = fnt;
        repaint();
    }
    
    /**
     * Definiert, wie sehr der Text des Labels nach unten oder oben verschoben werden soll. Zeichnet das Label neu.
     * 
     * @param bla     Der Integer gibt an, wie sehr der Text des Labels nach unten oder oben verschoben werden soll.
     */
    public void setBaselineAddition(int bla){
        baselineAddition = bla;
        repaint();
    }
    
    /**
     * Definiert, wie der Text innerhalb des Labels positioniert werden soll. Mögliche Parameter sind als statische Attribute der Klasse verfügbar, beispielsweise IFLabel.P_CENTER. Zeichnet das Label neu.
     * 
     * @param pt     Der Integer gibt an, wie der Text innerhalb des Labels positioniert werden soll. Mögliche Parameter sind als statische Attribute der Klasse verfügbar, beispielsweise IFLabel.P_CENTER.
     */
    public void setPositionType(int pt){
        positionType = pt;
        repaint();
    }
    
    /**
     * Definiert, ob der Text unterstrichen werden soll. Zeichnet das Label neu.
     * 
     * @param pt     Der boolean gibt an, ob der Text unterstrichen werden soll.
     */
    public void setUnderlining(boolean ul){
        underlining = ul;
        repaint();
    }
    
    /**
     * Gibt die Länge eines Textes in Pixeln aus.
     * 
     * @param str     Der String stellt den Text dar, der auf seine Länge in Pixeln geprüft werden soll.
     * @param frc     Das FontRenderContext-Objekt ist das Objekt, mit dem der Text gerendert wird.
     * 
     * @return     Die Länge des Textes in Pixeln als Integer.
     */
    public int getStringWidth(String str, FontRenderContext frc){
        GlyphVector gv = font.createGlyphVector(frc, str);
        return (int)gv.getPixelBounds(null, 2, 2).getWidth();
    }
    
    /**
     * Überschreibt die Methode public void beforeImage(Graphics2D g2) der Klasse IFComponent, die mit repaint() vor dem Zeichnen des BufferdImage ausgeführt wird.
     * 
     * @param g2     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet (Ok, hier wird nichts gezeichnet...), wenn repaint() ausgeführt wird.
     */
    public void beforeImage(Graphics2D g2){
    }
    
    /**
     * Kann von einer Unterklasse überschrieben werden.
     * 
     * @param g2     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet, wenn repaint() ausgeführt wird.
     */
    public void beforeText(Graphics2D g2){ 
    }
    
    /**
     * Überschreibt die Methode public void afterImage(Graphics2D g2) der Klasse IFComponent, die mit repaint() nach dem Zeichnen des BufferdImage ausgeführt wird. Zeichnet den Text gemäß den Attributen. Unterteilt mit "\n" abgetrennte Textteile,
     * zeichnet sie seperat und erlaubt so Zeilenumbrüche. Zeichnet ggf. einen Unterstrich.
     * 
     * @param g2     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet, wenn repaint() ausgeführt wird.
     */
    public void afterImage(Graphics2D g2){
        beforeText(g2);
        
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);
        
        ArrayList<String> texts = new ArrayList<String>();
        
        int index = text.indexOf('\n');
        int oldI = 0;
        while(index >= 0) {
             texts.add(text.substring(oldI, index));
             oldI = index;
             index = text.indexOf('\n', index+1);
        }
        texts.add(text.substring(oldI, text.length()));
        int lines = texts.size();
        
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, "Clear");
        int strHeight = (int)gv.getPixelBounds(null, 2, 2).getHeight() * lines + (lines-1)*14;
        
        
        
        ArrayList<Integer> xs = new ArrayList<Integer>();
        int y = 0;
        
        for(int i = 0; i < lines; i++){
            int strWidth = getStringWidth(texts.get(i), frc);
            int x = 0;
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
            xs.add(x);
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
        
        
        for(int i = 0; i < lines; i++){
            int currentY = y-(strHeight/lines)*(lines-i-1);
            
            g2.setPaint(foregroundColor);
            g2.drawString(texts.get(i), xs.get(i), currentY);
            if(underlining){
                g2.setPaint(underliningColor);
                g2.drawLine(xs.get(i),currentY+2,xs.get(i)+getStringWidth(texts.get(i), frc),currentY+2);
            }
        }
    }
}