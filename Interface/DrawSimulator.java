import java.awt.event.*;
import java.awt.*;
import java.util.*;
import basis.*;

public class DrawSimulator implements MouseListener, MouseMotionListener
{
    private Surface sf;
    private Result rs;
    private Frame f;
    private int mouseXStart;
    private int mouseYStart;
    private boolean neu;

    public DrawSimulator(Surface s, Result r){
        sf = s;
        rs = r;
        f = new Frame("Zeichnen");
        f.setSize((int)(Manager.w / 2)-20, (int)(Manager.h * 0.48));
        f.setLocation(0, (int)(Manager.h * 0.48));
        f.setLayout(null);
        f.setVisible(true);
        f.addMouseListener(this);
        f.addMouseMotionListener(this);
        f.addWindowListener(new WindowManager());
    }
    
    public void mouseExited(MouseEvent e) {
    }
   
    public void mouseDragged(MouseEvent e) {
        rs.setPos(e.getX(),e.getY());
        switch(Manager.mode){
            case 0:
                if(neu){
                    rs.drawLine(e.getX(),e.getY(),e.getX()+1,e.getY());
                    neu = false;
                }else{
                    rs.drawLine(mouseXStart, mouseYStart, e.getX(), e.getY());
                }
                mouseXStart = e.getX();
                mouseYStart = e.getY();
                break;
            case 1:
                break;
            case 6:
                rs.drawLine(mouseXStart, mouseYStart, e.getX(), e.getY());
                break;
        }
    }
        
    public void mouseEntered(MouseEvent e) {
    }
   
    public void mouseMoved(MouseEvent e) {
        rs.setPos(e.getX(), e.getY());
        switch(Manager.mode){
            case 1:
                break;
        }
    }
      
   public void mousePressed(MouseEvent e) {
        switch(Manager.mode){
            case 0:
                neu = true;
                break;
            case 6:
            case 1:
                mouseXStart = e.getX();
                mouseYStart = e.getY();
                break;
        }
    }
      
    public void mouseClicked(MouseEvent e) {
    }
      
    public void mouseReleased(MouseEvent e) {
        switch(Manager.mode){
            case 1:
                rs.drawLine(mouseXStart, mouseYStart, e.getX(), e.getY());
                break;
        }
    }
}
