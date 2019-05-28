import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class WindowManager extends WindowAdapter
{
    private boolean exitSystem;
    
    public WindowManager(boolean eS){
        exitSystem = eS;
    }
    
    public void windowClosing(WindowEvent e)
    {
        e.getWindow().dispose();
        if(exitSystem){
            System.exit(0);
        }
    }
}