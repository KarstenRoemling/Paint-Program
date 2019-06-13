import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class IFLink extends IFLabel{
    public Color standardClr;
    public Color hoverClr;
    public Color activeClr;
    public String url;
    
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
    
    public void setHoverColor(Color c){
        hoverClr = c;
    }
    
    public void setActiveColor(Color c){
        activeClr = c;
    }
    
    public void setStandardColor(Color c){
        standardClr = c;
        setForegroundColor(c);
        setUnderliningColor(c);
    }
}