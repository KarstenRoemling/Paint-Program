import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Die Klasse IFTextField hätte besser IFInputField heißen soll, denn anders als das TextField von Java AWT verfügt sie auch über den Typ "Nummer", der mit besonderen Features ausgestattet ist.
 * So kann man besipielsweise mit der Methode public int getNumber() auch die eingegebene Zahl bekommen, wenn der Typ auf "Nummer" ist und wenn man auf "+" oder "-" auf der Tastatur drückt, wird die im
 * TextField enthaltene Zahl um eins erhöht oder verringert.
 * Zusätzlich kann man Texte aus der Zwischenablage einfügen, indem man Strg+C drückt.
 * IFTextField erbt von der Klasse IFComponent und soll die Funktionen eines richtigen TextFields enthalten, bis dahin ist aber noch ein kleiner Weg, den wir zeitlich wohl nicht mehr beschreiten können werden.
 * Beispielsweise kann man noch nicht in der Mitte des TextFields Texte einfügen, sondern muss immer am Ende schreiben.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */


public class IFTextField extends IFComponent{
    public Color clr;
    public String text;
    public boolean focused;
    private int type;
    private boolean lineVisible;
    public double cr;
    public double border;
    public int positionType;
    public Font font;
    public Color foregroundColor;
    public Color focusColor;
    public Color standardColor;
    
    public static int T_TEXT = 0;
    public static int T_NUMBER = 1;
    public static int P_LEFT = 1;
    public static int P_CENTER = 0;
    public static String numbers = "0123456789";
    
    
    /**
     * Konstruktormethode der Klasse IFTextField: Initialisiert Attribute, fügt KeyListener und FocusListener hinzu, startet die Animation der Linie, die anzeigt, wo der text gerade bearbeitet wird...
     * 
     * @param width     Breite
     * @param height     Höhe
     * @param x     x-Position im Frame
     * @param y     y-Position im Frame
     * @param typeP     Typ des TextFields: Numerisch oder Text? - Lässt sich über die statischen Attribute T_TEXT und T_NUMBER zuordnen.
     */
    public IFTextField(int width, int height, int x, int y, int typeP){
        super(width,height,x,y);
        
        clr = new Color(10,30,100);
        standardColor = clr;
        foregroundColor = new Color(0,0,0);
        focusColor = new Color(100,30,100);
        font = new Font("Dosis", Font.BOLD, 20);
        text = "";
        type = typeP;
        border = 3;
        
        setCursor(new Cursor(Cursor.TEXT_CURSOR));
        
        paintST();
        
        final int typef = type;
        
        addKeyListener(new KeyListener(){
            public void keyReleased(KeyEvent k){
                
            }
            
            public void keyPressed(KeyEvent k){
                
            }
            
            public void keyTyped(KeyEvent k){
                if(focused){
                    if((int)k.getKeyChar() == 8){
                        if(text.length() != 0){
                            text = text.substring(0, text.length() - 1);
                            repaint();
                        }
                    }else if((int)k.getKeyChar() == 22){
                        if(type == 0){
                            try{
                                String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                                text += data;
                                repaint();
                            }catch(Exception e){}
                        }
                    }else{
                        switch(typef){
                            case 0:
                                if(!k.isActionKey()){
                                    text += k.getKeyChar();
                                    repaint();
                                }
                                break;
                            case 1:
                                if(numbers.contains(Character.toString(k.getKeyChar()))){
                                    text += k.getKeyChar();
                                    repaint();
                                }else if('+' == k.getKeyChar()){
                                    text = String.valueOf(getNumber() + 1);
                                    repaint();
                                }else if('-' == k.getKeyChar()){
                                    text = String.valueOf(getNumber() - 1);
                                    repaint();
                                }
                                break;
                        }
                    }
                }
            }
        });
        
        addFocusListener(new FocusListener(){
            public void focusLost(FocusEvent f){
                focused = false;
                setColor(standardColor);
            }
            
            public void focusGained(FocusEvent f){
                focused = true;
                setColor(focusColor);
            }
        });
        
        final ScheduledExecutorService eS = Executors.newSingleThreadScheduledExecutor();
        eS.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(focused){
                    lineVisible = !lineVisible;
                    repaint();
                }else if(lineVisible){
                    lineVisible = false;
                    repaint();
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Malt die Umrandung des TextFields in der Umrandungsfarbe. Zeichnet das TextField neu.
     */
    public void paintST(){
        roundedBgWithBorder(cr, clr, border, Color.WHITE);
        repaint();
    }
    
    /**
     * Definiert, welche Hintergrundfarbe das TextField haben soll (es handelt sich um die Farbe der Umrandung). Zeichnet das TextField neu.
     * 
     * @param color     Der Farbwert gibt an, welche Hintergrundfarbe das TextField haben soll (es handelt sich um die Farbe der Umrandung).
     */
    public void setColor(Color color){
        clr = color;
        paintST();
    }
    
    /**
     * Definiert, welche Hintergrundfarbe das TextField standardmäßig haben soll (es handelt sich um die Farbe der Umrandung). Zeichnet das TextField neu.
     * 
     * @param color     Der Farbwert gibt an, welche Hintergrundfarbe das TextField standardmäßig haben soll (es handelt sich um die Farbe der Umrandung).
     */
    public void setStandardColor(Color color){
        standardColor = color;
        clr = color;
        paintST();
    }
    
    /**
     * Definiert, welche Hintergrundfarbe das TextField haben soll (es handelt sich um die Farbe der Umrandung), wenn das TextField ausgewählt (fokussiert) ist. Zeichnet das TextField neu.
     * 
     * @param color     Der Farbwert gibt an, welche Hintergrundfarbe das TextField haben soll (es handelt sich um die Farbe der Umrandung), wenn das TextField ausgewählt (fokussiert) ist.
     */
    public void setFocusColor(Color color){
        focusColor = color;
        paintST();
    }
    
    /**
     * Definiert, welche Schriftfarbe das TextField haben soll. Zeichnet das TextField neu.
     * 
     * @param fgClr     Der Farbwert gibt an, welche Schriftfarbe das TextField haben soll.
     */
    public void setForegroundColor(Color fgClr){
        foregroundColor = fgClr;
        repaint();
    }
    
    /**
     * Definiert, wie stark die Ecken des TextFields abgerundet sein sollen. Zeichnet das TextField neu.
     * 
     * @param ncr     Der Integer gibt an, wie stark die Ecken des TextFields abgerundet sein sollen.
     */
    public void setCornerRadius(double ncr){
        cr = ncr;
        paintST();
    }
    
    /**
     * Definiert, wie dick die Umrandung des TextFields sein soll. Zeichnet das TextField neu.
     * 
     * @param nb     Der Integer gibt an, wie dick die Umrandung des TextFields sein soll (in Pixeln).
     */
    public void setBorder(double nb){
        border = nb;
        paintST();
    }
    
    /**
     * Definiert, welchen Text das TextField haben soll. Zeichnet den Button neu.
     * 
     * @param txt     Der String gibt an, welchen Text das TextField haben soll.
     */
    public void setText(String txt){
        text = txt;
        repaint();
    }
    
    /**
     * Definiert, welche Schriftart, Schriftdicke und welcher Schrifttyp (plain, bold etc.) der Text des TextFields haben soll. Zeichnet das TextField neu.
     * 
     * @param fnt     Das Font-Objekt gibt an, welche Schriftart, Schriftdicke und welcher Schrifttyp (plain, bold etc.) der Text des TextFields haben soll.
     */
    public void setFont(Font fnt){
        font = fnt;
        repaint();
    }
    
    /**
     * Definiert, wie der Text innerhalb des TextFields positioniert werden soll. Mögliche Parameter sind als statische Attribute der Klasse verfügbar, beispielsweise IFTextField.P_CENTER. Zeichnet das TextField neu.
     * 
     * @param pt     Der Integer gibt an, wie der Text innerhalb des TextFields positioniert werden soll. Mögliche Parameter sind als statische Attribute der Klasse verfügbar, beispielsweise IFTextField.P_CENTER.
     */
    public void setPositionType(int pt){
        positionType = pt;
        repaint();
    }
    
    /**
     * Gibt den Text des TextFields als String aus.
     * 
     * @return     Der Text des TextFields als String.
     */
    public String getText(){
        return text;
    }
    
    /**
     * Gibt die Zahl des TextFields als Integer aus.
     * 
     * @return     Die Zahl des TextFields als Integer. Ist das TextField leer oder nicht auf numerisch gestellt, wird 0 ausgegeben.
     */
    public int getNumber(){
        if(type == 1 && text.length() > 0){
            return Integer.parseInt(text);
        }else{
            return 0;
        }
    }
    
    /**
     * Überschreibt die Methode public void afterImage(Graphics2D g2) der Klasse IFComponent, die mit repaint() nach dem Zeichnen des BufferdImage ausgeführt wird. Zeichnet den Text gemäß den Attributen. Zeichnet ggf. einen Cursor, der die aktuelle
     * Position beim Bearbeiten des Texts darstellt.
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
        
        int x = 0;
        if(positionType == 0){
            x = (w - strWidth)/2;
        }else{
            x = (int)border+1;
        }
        int y = (h - strHeight)/2 + strHeight;
        g2.setPaint(foregroundColor);
        g2.drawString(text, x, y);
        if(lineVisible){
            g2.drawLine(x+strWidth, y-strHeight, x+strWidth, y);
        }
    }
    
    /**
     * Überschreibt die Methode public void beforeImage(Graphics2D g2) der Klasse IFComponent, die mit repaint() vor dem Zeichnen des BufferdImage ausgeführt wird.
     * 
     * @param g2     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet (Ok, hier wird nichts gezeichnet...), wenn repaint() ausgeführt wird.
     */
    public void beforeImage(){
        
    }
}
