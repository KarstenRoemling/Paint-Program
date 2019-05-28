import java.awt.event.*;
import java.util.*;
import java.awt.*; 
import basis.*;
    
public class Info
{
    private Frame f;
    private IFLabel label;
    private IFButton ok;
    private int width = 500;
    private int height = 250;
    
    public Info(String text, boolean warning)
    {
          f = new Frame("Info");
          f.addWindowListener(new WindowManager(false));
          label = new IFLabel(width,50,0,50, text);
          label.setFont(new Font("Dosis", Font.PLAIN, 18));
          f.add(label);
          f.setLayout(null);
          f.setSize(width, height);
          f.setVisible(true);
          f.setLocation((Manager.w - width)/2,250);
          ok = new IFButton(100,40,200,125,"Ok");
          ok.setFont(new Font("Dosis", Font.BOLD, 18));
          ok.setCornerRadius(1);
          f.add(ok);
          
          if(warning){
              label.setForegroundColor(new Color(255,0,0));
          }
          
          ok.addMouseListener(new MouseListener()
          {        
                public void mouseClicked(MouseEvent e){
                    f.dispose();
                }
                
                public void mouseExited(MouseEvent e){
                    ok.setColor(new Color(255,0,255));
                }
                
                public void mousePressed(MouseEvent e){
                    ok.animCR(3, 0.2);
                }
                
                public void mouseEntered(MouseEvent e){
                    ok.setColor(new Color(150,0,150));
                }
                
                public void mouseReleased(MouseEvent e){
                    ok.animCR(1, -0.2);
                }
          });
    }
}