import java.awt.*;
import java.awt.event.*;
import java.net.*;

/**
 * Die Klasse IFLink erbt von IFLabel und ist damit Teil der von uns erstellten Designelementen für die Benutzeroberfläche.
 * Neben einem Text verfügt diese Klasse vor allem auch über eine URL als Parameter der Konstruktormethode, die im vom Nutzer festgelegten Standard-Browser geöffnet wird, wenn man auf den Link klickt.
 * Wie die Links in HTML verfügt auch dieser Link über unterschiedliche Farben, wenn man ihn anklickt, die Maus über ihm drüber bewegt oder gar nichts mit ihm macht.
 * Die Default-Farbwerte sind ebenfalls dem standardmäßigem HTML-Stil angepasst.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */

public class IFLink extends IFLabel{
    public Color standardClr;
    public Color hoverClr;
    public Color activeClr;
    public String url;
    
    /**
     * Konstruktormethode der Klasse IFLink: Initialisiert Attribute, setzt Cursor, ruft die Konstruktormethode der Parent Class auf, fügt MouseListener hinzu...
     * 
     * @param width     Breite
     * @param height     Höhe
     * @param x     x-Position im Frame
     * @param y     y-Position im Frame
     * @param txt     Text des Links
     * @param url     URL, die mit Klick auf dem Link geöffnet werden soll.
     */
    public IFLink(int width, int height, int x, int y, String text, String url){
        super(width,height,x,y,text);
        
        foregroundColor = new Color(0,0,255);
        hoverClr = new Color(100,50,255);
        activeClr = new Color(255,0,0);
        standardClr = foregroundColor;
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForegroundColor(foregroundColor);
        setUnderlining(true);
        setUnderliningColor(foregroundColor);
        
        addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e){
                try{
                    Desktop.getDesktop().browse(URI.create(url));
                }catch(Exception exc){}
                //Dieses try{} - catch{} ist nur hinzugefügt worden, um der BlueJ-Umgebung gerecht zu werden. BlueJ meckert ansonsten immer mit einer IOException, die angeblich auftritt, (was aber in Realität
                //gar nicht der Fall ist) und ich kann die Klasse nicht compilen. Der Befehl in den try{}-Klammern funktioniert aber einwandfrei.
            }
            
            public void mouseExited(MouseEvent e){
                setForegroundColor(standardClr);
                setUnderliningColor(standardClr);
            }
            
            public void mousePressed(MouseEvent e){
                setForegroundColor(activeClr);
                setUnderliningColor(activeClr);
            }
            
            public void mouseEntered(MouseEvent e){
                setForegroundColor(hoverClr);
                setUnderliningColor(hoverClr);
            }
            
            public void mouseReleased(MouseEvent e){
                setForegroundColor(standardClr);
                setUnderliningColor(standardClr);
            }
        });
    }
    
    /**
     * Setzt die Farbe, die der Link annimmt, wenn man mit der Maus über ihn fährt.
     * 
     * @param c     Das Farbobjekt, das die Farbe definiert, die der Link annimmt, wenn man mit der Maus über ihn fährt.
     */
    public void setHoverColor(Color c){
        hoverClr = c;
    }
    
    /**
     * Setzt die Farbe, die der Link annimmt, wenn man ihn mit der Maus anklickt.
     * 
     * @param c     Das Farbobjekt, das die Farbe definiert, die der Link annimmt, wenn man ihn mit der Maus anklickt.
     */
    public void setActiveColor(Color c){
        activeClr = c;
    }
    
    /**
     * Setzt die Farbe, die der Link annimmt, wenn man weder ihn mit der Maus anklickt noch mit der Maus über ihn fährt.
     * 
     * @param c     Das Farbobjekt, das die Farbe definiert, die der Link annimmt, wenn man weder ihn mit der Maus anklickt noch mit der Maus über ihn fährt.
     */
    public void setStandardColor(Color c){
        standardClr = c;
        setForegroundColor(c);
        setUnderliningColor(c);
    }
}