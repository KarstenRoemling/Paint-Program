import basis.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.Applet;

public class Surface
{
    private Frame f;
    private TextField tf;
    private IgelStift stift;
    private Button clear;
    private Label anzeige;
    public int number;
    private Result rs;
    private String[] modeNames = {"Pinsel", "Linie"};
    private Button decrease;
    private Button increase;
    private Label modeName;
    
    public Surface(Result result)
    {
        rs = result;
        f = new Frame("Werkzeuge und Eintellungen");
        tf = new TextField(30);
        clear = new Button("Clear");
        decrease = new Button("<");
        increase = new Button(">");
        modeName = new Label(modeNames[Manager.mode]);
        anzeige = new Label("0");
        number = 0;
        clear.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                    rs.clearAll();
            } 
        });
        launchFrame();
    }
   
   public void launchFrame() {
        int w = (int)(Manager.w / 2)-20;
        int h = (int)(Manager.h * 0.48);
        f.setSize(w, h);
        f.setVisible(true);
        dynamicFrameLauncher(w, h);
   }
   
   public void dynamicFrameLauncher(int w, int h){
      Label label = new Label("Klicken und ziehen");
      
      f.setLayout(null);
      
      clear.setBounds(30,100,80,30);
      f.add(clear);
      
      decrease.setBounds(30,150,30,30);
      f.add(decrease);
      
      modeName.setBounds(65,150,100,30);
      f.add(modeName);
      
      increase.setBounds(170,150,30,30);
      f.add(increase);
      
      label.setBounds(30,30,200,30);
      f.add(label);
      
      tf.setBounds(5, (h-30), w, 30);
      f.add(tf);
      
      anzeige.setBounds(30,300,80,30);
      f.add(anzeige);
      
      f.setSize(w, h);
      f.setVisible(true);
    }
}
