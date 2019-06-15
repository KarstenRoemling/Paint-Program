import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class WindowManager extends WindowAdapter
{
    private boolean exitSystem;
    private boolean withDialog;
    
    public WindowManager(boolean eS, boolean dialog){
        exitSystem = eS;
        withDialog = dialog;
    }
    
    public void windowClosing(WindowEvent e)
    {
        if(withDialog){
            windowClosingWithDialog(e);
        }else{
            final Window w = e.getWindow();
            w.dispose();
            if(exitSystem){
                System.exit(0);
            }
        }
    }
    
    private void windowClosingWithDialog(WindowEvent e){
        final Window w = e.getWindow();
        IFButton cancel = new IFButton(100,40,0,0,"Abbrechen");
        cancel.setFont(new Font("Dosis", Font.BOLD, 18));
        cancel.setCornerRadius(1);
        cancel.addMouseListener(new MouseListener()
        {        
            public void mouseClicked(MouseEvent e){}
            
            public void mouseExited(MouseEvent e){
                cancel.setColor(new Color(10,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                cancel.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                cancel.setColor(new Color(40,50,140));
            }
            
            public void mouseReleased(MouseEvent e){
                cancel.animCR(1, -0.2);
            }
        });
        
        IFButton accept = new IFButton(100,40,0,0,"Beenden");
        accept.setFont(new Font("Dosis", Font.BOLD, 18));
        accept.setCornerRadius(1);
        accept.setColor(new Color(100,30,100));
        accept.addMouseListener(new MouseListener()
        {        
            public void mouseClicked(MouseEvent e){
                w.dispose();
                if(exitSystem){
                    System.exit(0);
                }
            }
            
            public void mouseExited(MouseEvent e){
                accept.setColor(new Color(100,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                accept.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                accept.setColor(new Color(140,50,140));
            }
            
            public void mouseReleased(MouseEvent e){
                accept.animCR(1, -0.2);
            }
        });
        
        IFDialog d = new IFDialog("Wollen Sie wirklich beenden?\nNicht gespeicherte Änderungen verfallen.",false,250,500);
        d.setButtons(new IFButton[]{accept,cancel});
    }
}