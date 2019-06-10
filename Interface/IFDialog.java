import java.awt.event.*;
import java.util.*;
import java.awt.*; 
import basis.*;
    
public class IFDialog
{
    public Frame f;
    private IFLabel label;
    public IFButton[] btns;
    private int width;
    private int height;
    public int buttonLength;
    
    public IFDialog(String text, boolean warning, int h, int w)
    {
          height = h;
          width = w;
          f = new Frame("Dialog");
          f.addWindowListener(new WindowManager(false));
          label = new IFLabel(width,50,0,50, text);
          label.setFont(new Font("Dosis", Font.PLAIN, 18));
          f.add(label);
          f.setLayout(null);
          f.setSize(width, height);
          f.setVisible(true);
          f.setLocation((Manager.w - width)/2,250);

          if(warning){
              label.setForegroundColor(new Color(255,0,0));
          }
    }
    
    public void setButtons(IFButton[] buttons){
        btns = buttons;
        
        for(int i = 0; i<btns.length; i++){
            buttonLength += btns[i].w+5;
        }
        buttonLength -= 5;
        
        createButtons();
    }
    
    private void createButtons(){
        int x = (width - buttonLength)/2;
        for(int i = 0; i<btns.length; i++){
              btns[i].setBounds(x,125,btns[i].w,btns[i].h);
              btns[i].addMouseListener(new MouseListener()
              {        
                    public void mouseClicked(MouseEvent e){
                        f.dispose();
                    }
                    
                    public void mouseExited(MouseEvent e){}
                    
                    public void mousePressed(MouseEvent e){}
                    
                    public void mouseEntered(MouseEvent e){}
                    
                    public void mouseReleased(MouseEvent e){}
              });
              f.add(btns[i]);
              x += btns[i].w+5;
        }
    }
}