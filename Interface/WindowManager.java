import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class WindowManager extends WindowAdapter
{
    public WindowManager(){}
    
    public void windowClosing(WindowEvent e)
    {
      e.getWindow().dispose();                   // Fenster "killen"
      System.exit(0);                            // VM "killen" 
    }    	
}