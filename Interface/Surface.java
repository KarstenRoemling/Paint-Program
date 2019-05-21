import basis.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.Applet;
import java.io.*;

public class Surface
{
    private Frame f;
    private IgelStift stift;
    private Result rs;
    
    private String[] modeNames = {"Pinsel", "Linie", "Text", "Kreis", "Rechteck", "Vieleck", "Linienkreis"};
    private String savePath = "..\\Images\\bild.png";
    private String pathD = "..\\Images\\";
    private String pathN = "name";
    
    private Button decrease;
    private Button increase;
    private Button save;
    private Button clear;
    private Label modeName;
    private Label waText;
    private TextField pathField;
    private TextField nameField;
    
    public Surface(Result result)
    {
        rs = result;
        Manager.refresh();
        
        f = new Frame("Werkzeuge und Eintellungen");
        clear = new Button("Clear");
        decrease = new Button("<");
        increase = new Button(">");
        save = new Button("Bild speichern");
        modeName = new Label(modeNames[Manager.mode]);
        waText = new Label("Werkzeugauswahl");
        pathField = new TextField(pathD);
        nameField = new TextField(pathN);
        
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
                    Manager.refresh();
            } 
        });
        increase.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                    Manager.mode++;
                    if(Manager.mode >= modeNames.length){
                        Manager.mode = 0;
                    }
                    modeName.setText(modeNames[Manager.mode]);
                    Manager.refresh();
            } 
        });
        save.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                    boolean b = rs.b1.speichereBildUnter(pathField.getText()+nameField.getText()+".png");
            } 
        });
        
        f.addWindowListener(new WindowManager());
        launchFrame();
        
        String name = "bild";
        int count = 1;
        while(new File("..\\Images\\"+name+".png").exists()){
            name = "bild" + String.valueOf(count);
            count++;
        }
        savePath = "..\\Images\\"+name+".png";
        pathD = "..\\Images\\";
        pathN = name;
        
        pathField.setText(pathD);
        nameField.setText(pathN);
    }
    
    public void launchFrame() {
          int w = (int)(Manager.w / 2)-20;
          int h = (int)(Manager.h * 0.48);
          
          Font subHeading = new Font("Calibri", Font.PLAIN, 24);
          Font heading = new Font("Calibri", Font.BOLD, 30);
          Font normal = new Font("Calibri", Font.PLAIN, 18);
          Font small = new Font("Calibri", Font.PLAIN, 12);
          
          Label label = new Label("Einstellungen");
          
          f.setLayout(null);
          
          pathField.setBounds(30, 85, 300, 20);
          f.add(pathField);
          
          nameField.setBounds(335, 85, 100, 20);
          f.add(nameField);
          
          waText.setBounds(30, 145, 220, 30);
          waText.setFont(subHeading);
          f.add(waText);
          
          clear.setBounds(205,180,80,30);
          clear.setFont(normal);
          clear.setForeground(new Color(255,0,0));
          clear.setFocusable(false);
          f.add(clear);
          
          save.setBounds(440,85,100,20);
          save.setFocusable(false);
          f.add(save);
          
          decrease.setBounds(30,180,30,30);
          decrease.setFocusable(false);
          decrease.setFont(normal);
          f.add(decrease);
          
          modeName.setBounds(65,180,100,30);
          modeName.setFont(normal);
          f.add(modeName);
          
          increase.setBounds(170,180,30,30);
          increase.setFocusable(false);
          increase.setFont(normal);
          f.add(increase);
          
          label.setBounds(30,30,250,40);
          f.add(label);
          label.setFont(heading);
          
          f.setSize(w, h);
          f.setVisible(true);
    }
}
