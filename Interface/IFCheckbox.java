import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.util.concurrent.*;

/**
 * Die Klasse IFCheckbox erbt von IFComponent und ist ein Element der Benutzeroberfläche, das die Darstellung und Auswahl von zwei Zuständen erlaubt: Wahr oder falsch beziehungsweise true oder false.
 * IFCheckbox kommt beispielsweise zum Einsatz, wenn es darum geht, ob der Radierer weiß oder in der Hintergrundfarbe zeichnen soll oder ob abgerundet oder eckig gezeichnet werden soll.
 * Die Klasse IFCheckbox stellt eine quadratische Box mit einem Häkchen (wenn sie aktiviert ist) dar, links neben ihr befindet sich mit etwas Abstand ein Text, der die Funktion der Checkbox knapp erklärt.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */


public class IFCheckbox extends IFComponent{
    public Color clr;
    public int border;
    public double cr;
    public boolean checked;
    public String text;
    public Font font;
    public Color textColor;
    
    private int exPermission = 0;
    
    /**
     * Konstruktormethode der Klasse IFCheckbox: Initialisiert Attribute, setzt Cursor, ruft die Konstruktormethode der Parent Class auf, zeichnet die Checkbox und macht das BufferedImage quadratisch und lässt es nicht die volle Breite der Checkbox haben.
     * 
     * @param width     Breite
     * @param height     Höhe
     * @param x     x-Position im Frame
     * @param y     y-Position im Frame
     * @param check     Gibt an, ob die Checkbox zu Anfang gecheckt ist
     * @param txt     Text der Checkbox
     */
    public IFCheckbox(int width, int height, int x, int y, boolean check, String txt){
        super(width,height,x,y);
        b = new BufferedImage(height, height, BufferedImage.TYPE_INT_RGB);
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        text = txt;
        checked = check;
        clr = new Color(10,30,100);
        border = 3;
        textColor = Color.BLACK;
        font = new Font("Dosis", Font.PLAIN, 20);
        
        paintST();
    }
    
    /**
     * Wird ausgeführt, um zu definieren, was passieren soll, wenn die IFCheckbox angeklickt wird: der Wert von checked soll sich ändern und das Objekt wird neu gezeichnet.
     */
    public void handleClick(){
        setChecked(!checked);
    }
    
    /**
     * Gibt aus, ob die IFCheckbox aktiviert ist oder nicht (ob sie "gecheckt" ist)
     * 
     * @return     boolean - Ist die Box gecheckt: true, sonst: false
     */
    public boolean getChecked(){
        return checked;
    }
    
    /**
     * Definiert, ob die Checkbox gecheckt ist oder nicht. Zeichnet die Checkbox danach neu.
     * 
     * @param check     Der Wahrheitswert (boolean), der entscheidet, ob die Checkbox gecheckt ist oder nicht.
     */
    public void setChecked(boolean check){
        checked = check;
        repaint();
    }
    
    /**
     * Definiert, wie stark die Ecken der Checkbox abgerundet sein sollen. Zeichnet die Checkbox neu.
     * 
     * @param value     Der Integer gibt an, wie stark die Ecken der Checkbox abgerundet sein sollen.
     */
    public void setCornerRadius(int value){
        cr = value;
        paintST();
    }
    
    /**
     * Definiert, welche Hintergrundfarbe die Checkbox haben soll (es handelt sich um die Farbe der Umrandung). Zeichnet die Checkbox neu.
     * 
     * @param c     Der Farbwert gibt an, welche Hintergrundfarbe die Checkbox haben soll (es handelt sich um die Farbe der Umrandung).
     */
    public void setColor(Color c){
        clr = c;
        paintST();
    }
    
    /**
     * Definiert, wie dick die Umrandung der Checkbox sein soll. Zeichnet die Checkbox neu.
     * 
     * @param b     Der Integer gibt an, wie dick die Umrandung der Checkbox sein soll.
     */
    public void setBorderWidth(int b){
        border = b;
        paintST();
    }
    
    /**
     * Definiert, welchen Begleittext die Checkbox haben soll. Zeichnet die Checkbox neu.
     * 
     * @param txt     Der String gibt an, welchen Begleittext die Checkbox haben soll.
     */
    public void setText(String txt){
        text = txt;
        repaint();
    }
    
    /**
     * Definiert, welche Schriftart, Schriftdicke und welcher Schrifttyp (plain, bold etc.) der Begleittext der Checkbox haben soll. Zeichnet die Checkbox neu.
     * 
     * @param f     Das Font-Objekt gibt an, welche Schriftart, Schriftdicke und welcher Schrifttyp (plain, bold etc.) der Begleittext der Checkbox haben soll.
     */
    public void setFont(Font f){
        font = f;
        repaint();
    }
    
    /**
     * Malt eine quadratische, umrandete Box auf das BufferedImage. Zeichnet die Checkbox neu.
     */
    public void paintST(){
        int oldW = w;
        w = h;
        roundedBgWithBorder(cr, clr, border, Color.WHITE);
        repaint();
        w = oldW;
    }
    
    /**
     * Überschreibt die Methode public void beforeImage(Graphics2D g2) der Klasse IFComponent, die mit repaint() vor dem Zeichnen des BufferdImage ausgeführt wird. Setzt den Hintergrund auf weiß.
     * 
     * @param g2     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet, wenn repaint() ausgeführt wird.
     */
    public void beforeImage(Graphics2D g2){
        g2.setBackground(Color.WHITE);
    }
    
    /**
     * Animiert die abgerundeten Ecken der Checkbox.
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
     * Überschreibt die Methode public void afterImage(Graphics2D g2) der Klasse IFComponent, die mit repaint() nach dem Zeichnen des BufferdImage ausgeführt wird. Zeichnet ggf. das Häkchen und den Begleittext.
     * 
     * @param g2     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet, wenn repaint() ausgeführt wird.
     */
    public void afterImage(Graphics2D g2){
        Font font2 = font;
        if(checked){
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
                
            g2.setPaint(clr);
            g2.fillOval(5,16,4,4);
            g2.fillPolygon(new int[]{9,5,10,25,23,10},new int[]{16,20,28,9,4,20}, 6);
            g2.fillOval(22,4,4,4);
            
            //font2 = new Font(font.getName(), Font.BOLD, font.getSize());
            //Funktioniert auch (macht den Text fett, wenn checked true ist), haben uns aber aus Design-Gründen dagegen entschieden.
        }
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        
        g2.setFont(font2);
        
        int w2 = w-10-h;
        
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, "Clear");
        int strHeight = (int)gv.getPixelBounds(null, 2, 2).getHeight();
        gv = g2.getFont().createGlyphVector(frc, text);
        int strWidth = (int)gv.getPixelBounds(null, 2, 2).getWidth();
        
        int x = 10+h;
        int y = (h - strHeight)/2 + strHeight;
        g2.setPaint(textColor);
        g2.drawString(text, x, y);
    }
}