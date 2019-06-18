import java.awt.event.*;
import java.util.*;
import java.awt.*; 
import basis.*;
import javax.imageio.*;
import java.io.*;

/**
 * Die Klasse IFDialog erzeugt ein Dialogfenster mit mehreren Buttons, die man unabhängig erzeugen und mit der
 * Mathode public void setButtons(IFButton[] buttons) hinzufügen muss.
 * Jeder Button schließt das Fenster, wenn man auf ihn klickt, und bekommt eine Position zugeteilt.
 * Außerdem enthält der IFDialog ein IFLabel mit einer Nachricht.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */

public class IFDialog
{
    public Frame f;
    private IFLabel label;
    public IFButton[] btns;
    private int width;
    private int height;
    public int buttonLength;
    
    /**
     * Konstruktormethode der Klasse IFDialog. Erstellt ein neues Frame, initialisiert die Attribute...
     * 
     * @param text     Die Nachricht, die im Dialog-Fenster erscheint.
     * @param warning     Gibt an, ob es sich um eine Warnmeldung handelt und die Schriftart rot sein muss.
     * @param h     die Höhe des Dialog-Fensters
     * @param w     die Länge des Dialog-Fensters
     */
    public IFDialog(String text, boolean warning, int h, int w)
    {
          height = h;
          width = w;
          f = new Frame("Dialog");
          f.addWindowListener(new WindowManager(false, false));
          label = new IFLabel(width,50,0,50, text);
          label.setFont(new Font("Dosis", Font.PLAIN, 18));
          f.add(label);
          f.setLayout(null);
          f.setSize(width, height);
          f.setVisible(true);
          f.setLocation((Manager.w - width)/2,250);
          
          try {
            File path = new File("dialog.png");
            java.awt.Image icon = ImageIO.read(path);
            f.setIconImage(icon);
          } catch (Exception e) {}

          if(warning){
              label.setForegroundColor(new Color(255,0,0));
          }
    }
    
    /**
     * Definiert, welche Buttons in den Dialog aufgenommen werden sollen und bestimmt deren Gesamtlänge. Ruft private void createButtons() auf, um die Buttons zum Dialog hinzuzufügen, ihnen MouseListener zu überreichen und sie anzuordnen
     * 
     * @params buttons     Die Array an Objekten des Typs IFButton, die als Optionen zum Dialog hinzugefügt werden sollen.
     */
    public void setButtons(IFButton[] buttons){
        btns = buttons;
        
        for(int i = 0; i<btns.length; i++){
            buttonLength += btns[i].w+5;
        }
        buttonLength -= 5;
        
        createButtons();
    }
    
    /**
     * Wird nur von public void setButtons(IFButton[] buttons) aufgerufen. Ordnet die Buttons gemäß ihrer Position in der Array insgesamt mittig und auf einer Höhe von 125 Pixeln unterhalb des oberen Fensterrandes an.
     * Fügt zu jedem Button einen MouseListener hinzu, der das Dialogfenster schließt, wenn der Button angeklickt wird.
     */
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