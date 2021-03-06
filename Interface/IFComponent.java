import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.font.*;

/**
 * Die Klasse IFComponent ist die Superklasse wichtiger UI-Objekte wie dem eigens erstellten IFButton oder dem IFLabel.
 * Sie erbt vom java.awt.panel und überschreibt einige dessen Methoden wie public void paint(Graphics g) oder public Dimension getPreferredSize().
 * In erster Methode wird das BufferedImage, dass individuell in den Unterklassen gestaltet wird gezeichnet.
 * Methoden wie beforeImage() und afterImage() werden von den Unterklassen überschrieben, um zum Beispiel einen Text einzufügen.
 * Algorithmen wie public void roundedBg(double cr, Color clr) oder public void roundedBgWithBorder(double cr, Color clr, double border, Color bclr), auf die
 * wir sehr stolz sind, da wir sie selbst erstellt haben, sorgen für eine angenehme und einigermaßen moderne Benutzeroberfläche,
 * die auch abgerundete Ecken enthält.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */

public class IFComponent extends Panel
{
    public int w;
    public int h;
    public int x;
    public int y;
    public BufferedImage b;
    
    /**
     * Konstruktormethode der Klasse IFComponent. Initialisiert die Attribute mit den Parametern. Erstellt ein neues BufferedImage. Nutzt die von Panel vererbten Methoden, um die Position zu setzen und das Layout auf null zu setzen (Damit es unabhängig bearbeitet werden kann).
     * 
     * @param width     Breite des IFComponents
     * @param height     Höhe des IFComponents
     * @param xx     x-Position des IFComponents
     * @param yy     y-Position des IFComponents
     */
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
    
    /**
     * Gibt eine Zahl zwischen den eingegebenen Werten value1 und value2 aus, wobei die Gewichtung des zweiten Werts mit dem Parameter preference bestimmt wird.
     * 
     * @param  value1     der erste Wert, der für den Mischwert benötigt wird.
     * @param  value2     der zweite Wert, der für den Mischwert benötigt wird.
     * @param  preference     die Gewichtung des zweiten Wertes (eine Zahl zwischen 0 und 1)
     * @return        der Mischwert zwischen value1 und value2 mit der Gewichtung preference als Integer
     */
    public static int mixOf(double value1, double value2, double preference){
        return (int) ((value2 - value1) * preference + value1);
    }
    
    /**
     * Gibt eine Farbe zwischen den eingegebenen Farben c1 und c2 aus, wobei die Gewichtung des zweiten Werts mit dem Parameter pref bestimmt wird.
     * 
     * @param  c1     die erste Farbe, die für die Mischfarbe benötigt wird.
     * @param  c2     die zweite Farbe, die für die Mischfarbe benötigt wird.
     * @param  pref     die Gewichtung der zweiten Farbe (eine Zahl zwischen 0 und 1)
     * @return        die Mischfarbe zwischen c1 und c2 mit der Gewichtung pref als Color-Objekt
     */
    public static Color mixOfColors(Color c1, Color c2, double pref){
        return new Color(mixOf((double)c1.getRed(), (double)c2.getRed(), pref),mixOf((double)c1.getGreen(), (double)c2.getGreen(), pref),mixOf((double)c1.getBlue(), (double)c2.getBlue(), pref));
    }
    
    /**
     * Zeichnet auf dem BufferedImage b eine Farbfläche mit abgerundete Ecken
     * 
     * @param  cr     der Faktor, der die Stärke der Abrundung bestimmt.
     * @param  clr     die Hintergrundfarbe für die abgerundete Fläche.
     */
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
    
    /**
     * Zeichnet auf dem BufferedImage b eine abgerundete Umrandung
     * 
     * @param  cr     der Faktor, der die Stärke der Abrundung bestimmt.
     * @param  clr     die HUmrandungsfarbe
     * @param border     die Dicke der Umrandung
     * @param bclr     die Farbe innerhalb der Umrandung
     */
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
    
    /**
     * Wird von Panel geerbt und hier überschrieben. Bestimmt die Höhe und Breite des Panels. Sie wird hier auf die Höhe und Breite des BufferedImage gesetzt.
     * 
     * @return     Dimension-Objekt, das die Höhe und Breite des BufferedImage enthält.
     */
    public Dimension getPreferredSize() {
        return new Dimension(b.getWidth(), b.getHeight());
    }
    
    /**
     * Wird von Panel geerbt und hier überschrieben. Wird bei repaint() ausgeführt und zeichnet das BufferedImage b sowie - je nach Überschreibung der Methoden public void beforeImage(Graphics2D g2) und public void afterImage(Graphics2D g2)
     * in den Unterklassen - Text und weitere in den Unterklassen definierte Elemente.
     * 
     * @return     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet, wenn repaint() ausgeführt wird.
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        
        beforeImage(g2);
        
        g2.drawImage(b, null, null);
        
        afterImage(g2);
    }
    
    /**
     * Füllt das BufferedImage b mit der Farbe c
     * 
     * @param c     Color-Objekt, mit der das BufferedImage gefüllt werden soll
     */
    public void fillBI(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < b.getWidth(); x++) {
            for (int y = 0; y < b.getHeight(); y++) {
                b.setRGB(x, y, color);
            }
        }
        repaint();
    }
    
    /**
     * Kann von einer Unterklasse überschrieben werden.
     * 
     * @param g2     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet, wenn repaint() ausgeführt wird.
     */
    public void afterImage(Graphics2D g2){}
    
    /**
     * Kann von einer Unterklasse überschrieben werden.
     * 
     * @param g2     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet, wenn repaint() ausgeführt wird.
     */
    public void beforeImage(Graphics2D g2){}
}
