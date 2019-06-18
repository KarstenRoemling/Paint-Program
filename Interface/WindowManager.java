import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 * Der WindowManager erbt von WindowAdapter und überschreibt dabei die public void windowClosing(WindowEvent e)
 * Jedes Fenster im Programm enthält ein Objekt des WindowManager, um zu kontrollieren, was passiert,
 * wenn man auf das X am oberen rechten Rand des Fensters klickt, um das Fenster zu schließen.
 * Je nach den Parametern in der Konstruktormethode wird zudem ein Dialog angezeigt, ob man das Programm wirklich schließen möchte
 * bzw. das Programm wird ganz geschlossen oder nur das Fenster.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */

public class WindowManager extends WindowAdapter
{
    private boolean exitSystem;
    private boolean withDialog;
    
    /**
     * Konstruktormethode der Klasse WindowManager. Erstellt einen WindowManager und initialisiert die Attribute exitSystem und withDialog mit den Parametern eS und dialog.
     * 
     * @param eS     Gibt an, ob das ganze Programm beendet werden soll, wenn sich das Fenster schließt.
     * @param dialog     Gibt an, ob erst ein Dialog geöffnet werden soll, mit dem der Nutzer entscheiden kann ob er wirklich beenden oder das Fenster schließen möchte.
     */
    public WindowManager(boolean eS, boolean dialog){
        exitSystem = eS;
        withDialog = dialog;
    }
    
    /**
     * Überschreibt die Methode der Klasse WindowsAdapter, von der WindowManager erbt. Wird ausgelöst, wenn man das Fenster mit dem "X" am rechten oberen Fensterrand schließen möchte.
     * Wenn das Attribut withDialog true ist, wird das Fenster nur geschlossen, wenn das in einem Dialog vom Nutzer explizit gewünscht ist.
     * Das Fenster schließt sich ansonsten direkt. Wenn auch das Attribut exitSystem true ist, wird das gesamte Programm geschlossen.
     * 
     * @param e     Das WindowEvent-Objekt, das Informationen darüber liefert, wie das Window geschlossen wurde etc. Beispielsweise enthält es die Methode getWindow(), mit der man das Window bokommen kann und so auch mit der Methode dispose() schließen kann.
     */
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
    
    /**
     * Wird nur von public void windowClosing(WindowEvent e) ausgelöst.
     * Öffnet erst einmal einen Dialog mit den zuvor erstellten Buttons und einem IFDialog-Objekt, bevor, falls "Beenden" gewählt wird, das Fenster geschlossen und ggf. das Programm beendet wird.
     * 
     * @param e     Das WindowEvent-Objekt, das Informationen darüber liefert, wie das Window geschlossen wurde etc. Beispielsweise enthält es die Methode getWindow(), mit der man das Window bokommen kann und so auch mit der Methode dispose() schließen kann.
     */
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