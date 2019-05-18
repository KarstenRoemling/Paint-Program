import basis.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.Applet;

public class Surface
{
    private Frame f;
    private IgelStift stift;
    private Button clear;
    private Label waText;
    private Result rs;
    private String[] modeNames = {"Pinsel", "Linie", "Text", "Kreis", "Rechteck", "eigene Formen", "Linienkreis"};
    private Button decrease;
    private Button increase;
    private Label modeName;
    
    public Surface(Result result)
    {
        rs = result;
        f = new Frame("Werkzeuge und Eintellungen");
        clear = new Button("Clear");
        decrease = new Button("<");
        increase = new Button(">");
        modeName = new Label(modeNames[Manager.mode]);
        waText = new Label("Werkzeugauswahl");
        clear.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                    rs.clearAll();
            } 
        });
        decrease.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                    Manager.mode--;
                    if(Manager.mode < 0){
                        Manager.mode = modeNames.length - 1;
                    }
                    modeName.setText(modeNames[Manager.mode]);
            } 
        });
        increase.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                    Manager.mode++;
                    if(Manager.mode >= modeNames.length){
                        Manager.mode = 0;
                    }
                    modeName.setText(modeNames[Manager.mode]);
            } 
        });
        f.addWindowListener(new WindowManager());
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
      
      waText.setBounds(30, 100, 150, 30);
      f.add(waText);
      
      clear.setBounds(205,150,80,30);
      f.add(clear);
      
      decrease.setBounds(30,150,30,30);
      f.add(decrease);
      
      modeName.setBounds(65,150,100,30);
      f.add(modeName);
      
      increase.setBounds(170,150,30,30);
      f.add(increase);
      
      label.setBounds(30,30,200,30);
      f.add(label);
      
      f.setSize(w, h);
      f.setVisible(true);
    }
}
