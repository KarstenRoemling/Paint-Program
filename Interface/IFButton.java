import java.awt.*;
import java.awt.image.*;
import java.util.concurrent.*;
import java.awt.font.*;

/**
 * Der IFButton erbt von der Klasse IFComponent.
 * Als ein Button ist er ein wichtiges Element unserer Benutzeroberfläche.
 * Die Schaltfläche, die hier durch das Ausführen der Konstruktormethode entsteht kann über abgerundete Ecken verfügen, und lässt sich mit der Methode public void animCR(double result, double changePE) im Hinblick auf die Stärke der
 * Rundung animieren.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */

public class IFButton extends IFComponent
{
    public Color clr;
    public double cr = 0.0;//cornerRadius
    public int baselineAddition = 0;
    private int exPermission = 0;
    public String text;
    public Font font;
    public Color foregroundColor;
    
    /**
     * Konstruktormethode der Klasse IFButton: Initialisiert Attribute, setzt Cursor, ruft die Konstruktormethode der Parent Class auf, zeichnet den Button...
     * 
     * @param width     Breite
     * @param height     Höhe
     * @param x     x-Position im Frame
     * @param y     y-Position im Frame
     * @param txt     Text des Buttons
     */
    public IFButton(int width, int height, int x, int y, String txt){
        super(width,height, x, y);
        
        clr = new Color(10,30,100);
        foregroundColor = Color.WHITE;
        font = new Font("Dosis", Font.BOLD, 20);
        text = txt;
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        paintST();
    }
    
    /**
     * Malt den Hintergrund des Buttons mit der Hintergrundfarbe und abgerundeten Ecken. Zeichnet den Button neu.
     */
    public void paintST(){
        roundedBg(cr, clr);
        repaint();
    }
    
    /**
     * Definiert, welche Hintergrundfarbe der Button haben soll. Zeichnet den Button neu.
     * 
     * @param color     Der Farbwert gibt an, welche Hintergrundfarbe der Button haben soll.
     */
    public void setColor(Color color){
        clr = color;
        paintST();
    }
    
    /**
     * Definiert, welche Schriftfarbe der Button haben soll. Zeichnet den Button neu.
     * 
     * @param fgClr     Der Farbwert gibt an, welche Schriftfarbe der Button haben soll.
     */
    public void setForegroundColor(Color fgClr){
        foregroundColor = fgClr;
        repaint();
    }
    
    /**
     * Definiert, wie stark die Ecken des Buttons abgerundet sein sollen. Zeichnet den Button neu.
     * 
     * @param ncr     Der Integer gibt an, wie stark die Ecken des Buttons abgerundet sein sollen.
     */
    public void setCornerRadius(double ncr){
        cr = ncr;
        paintST();
    }
    
    /**
     * Definiert, welchen Text der Button haben soll. Zeichnet den Button neu.
     * 
     * @param txt     Der String gibt an, welchen Text der Button haben soll.
     */
    public void setText(String txt){
        text = txt;
        repaint();
    }
    
    /**
     * Definiert, welche Schriftart, Schriftdicke und welcher Schrifttyp (plain, bold etc.) der Text des Buttons haben soll. Zeichnet den Button neu.
     * 
     * @param fnt     Das Font-Objekt gibt an, welche Schriftart, Schriftdicke und welcher Schrifttyp (plain, bold etc.) der Text des Buttons haben soll.
     */
    public void setFont(Font fnt){
        font = fnt;
        repaint();
    }
    
    /**
     * Definiert, wie sehr der Text des Buttons nach unten oder oben verschoben werden soll. Zeichnet den Button neu.
     * 
     * @param bslnAddtn     Der Integer gibt an, wie sehr der Text des Buttons nach unten oder oben verschoben werden soll.
     */
    public void setBaselineAddition(int bslnAddtn){
        baselineAddition = bslnAddtn;//Das sind Variabelnamen... (-:
        repaint();
    }
    
    /**
     * Animiert die abgerundeten Ecken des Buttons.
     * 
     * @param result     Der Wert, bei dem die Animation abgeschlossen ist.
     * @param changePE     Der Wert, um den bei jeder Ausführung (alle 10 Milisekunden) die abgerundeten Ecken geändert werden.
     */
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
    
    /**
     * Überschreibt die Methode public void afterImage(Graphics2D g2) der Klasse IFComponent, die mit repaint() nach dem Zeichnen des BufferdImage ausgeführt wird. Zeichnet den Text gemäß den Attributen des IFButton-Objekts mittig.
     * 
     * @param g2     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet, wenn repaint() ausgeführt wird.
     */
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
