import java.awt.event.*;
import java.util.*;
import java.awt.*; 
import basis.*;
import javax.imageio.*;
import java.io.*;

/**
 * Die Klasse Info erbt von IFDialog und ist eine Warn- bzw. Infomeldung, ein vordefinierter
 * IFDialog mit einem einzigen Button, der OK anzeigt.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */
    
public class Info extends IFDialog
{
    private IFButton ok;
    
    /**
     * Konstruktormethode und einzige Methode der Klasse Info. Erstellt einen IFButton und nutzt die vererbten Methoden, um eine Warn- oder Infomeldung zu erstellen.
     * 
     * @param text     Die Nachricht in der Warn- oder Infomeldung
     * @param warning     Gibt an, ob es sich um eine Warnmeldung handelt und die Schriftart deswegen rot sein muss.
     */
    public Info(String text, boolean warning)
    {
          super(text, warning, 250, 500);
          
          try {
            File path;
            if(warning){
                path = new File("warning.png");
            }else{
                path = new File("info.png");
            }
            java.awt.Image icon = ImageIO.read(path);
            f.setIconImage(icon);
          } catch (Exception e) {}
          
          ok = new IFButton(100,40,200,125,"Ok");
          ok.setFont(new Font("Dosis", Font.BOLD, 18));
          ok.setCornerRadius(1);
          
          ok.addMouseListener(new MouseListener()
          {        
                public void mouseClicked(MouseEvent e){}
                
                public void mouseExited(MouseEvent e){
                    ok.setColor(new Color(10,30,100));
                }
                
                public void mousePressed(MouseEvent e){
                    ok.animCR(3, 0.2);
                }
                
                public void mouseEntered(MouseEvent e){
                    ok.setColor(new Color(40,50,140));
                }
                
                public void mouseReleased(MouseEvent e){
                    ok.animCR(1, -0.2);
                }
          });
          
          setButtons(new IFButton[]{ok});
          
          if(warning){
              f.setTitle("Warnung!");
          }else{
              f.setTitle("Info"); 
          }
    }
}