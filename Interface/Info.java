import java.awt.event.*;
import java.util.*;
import java.awt.*; 
import basis.*;
    
public class Info
    {
    private Frame f;
    private IFLabel label;
    private IFButton ok;
    public Info(String text, boolean warning)
    {
      f = new Frame("Info");
      f.addWindowListener(new WindowManager());
      label = new IFLabel(250,50,30,30, text);
      f.add(label);
      f.setLayout(null);
      f.setSize(500, 250);
      f.setVisible(true);
      f.setLocation(350,250);
      ok = new IFButton(100,20,100,100,"ok");
      ok.setCornerRadius(2);
      f.add(ok);
      
      if(warning){
          label.setForegroundColor(new Color(255,0,0));
        }
      
      ok.addMouseListener(new MouseListener()
        {        
            public void mouseClicked(MouseEvent e){
            }
            
            public void mouseExited(MouseEvent e){
                ok.setColor(new Color(150,0,255));
            }
            
            public void mousePressed(MouseEvent e){
                ok.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                ok.setColor(new Color(150,0,255));
            }
            
            public void mouseReleased(MouseEvent e){
                ok.animCR(1, -0.2);
            }
        });
    }
}