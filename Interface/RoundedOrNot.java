import java.awt.*;

/**
 * Die Klasse RoundedOrNot ist eigens für das Beispiel neben der Stiftgröße erstellt worden. Sie erbt von IFLabel, das wiederum von IFComponent erb, das wiederum von Panel erbt.
 * Anders als reguläre IFLabels erlaubt dieses IFLabel auch die Darstellung kreisförmiger Hintergründe.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */


public class RoundedOrNot extends IFLabel{
    private boolean r;
    private Color clr;
    
    /**
     * Konstruktormethode der Klasse RoundedOrNot: Initialisiert Attribute, ruft die Konstruktormethode der Parent Class auf, wobei die Größe und Position dieses Objekts anhand der Stiftdicke berechnet wird.
     * 
     * @param thick     aktuelle Stiftdicke und Größe des RoundedOrNot-Objekts
     * @param rounded     Gibt an, ob der Stift abgerundet ist und deswegen ein Kreis anstelle eines Quadrats gezeichnet werden muss.
     * @param color     aktuelle Stiftfarbe und Farbe des RoundedOrNot-Objekts
     */
    public RoundedOrNot(int thick, boolean rounded, Color color){
        super(thick, thick, 65+(30-thick)/2, 375+(30-thick)/2, "");
        
        r = rounded;
        clr = color;
    }
    
    /**
     * Überschreibt die Methode public void beforeText(Graphics2D g2) der Klasse IFLabel. So wird vor dem Zeichnen des Texts ein Kreis oder ein Rechteck als Hintergrund gezeichnet, je nach dem, ob das Attribut r true oder false ist.
     * 
     * @param g2     Das Graphics2D-Objekt, das die in dieser Methode gezeichneten Elemente zeichnet, wenn repaint() ausgeführt wird.
     */
    public void beforeText(Graphics2D g2){
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(clr);
        if(r){
            g2.fillOval(0,0,w,w);
        }else{
            g2.fillRect(0,0,w,w);
        }
    }
}