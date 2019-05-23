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
    
    private IFButton decrease;
    private IFButton increase;
    private Label decreaseT;
    private Label increaseT;
    
    private Button save;
    private Button clear;
    private Label modeName;
    private Label waText;
    private TextField pathField;
    private TextField nameField;
    private IFButton panel;
    
    public Surface(Result result)
    {
        rs = result;
        Manager.refresh();
        
        f = new Frame("Werkzeuge und Eintellungen");
        clear = new Button("Clear");
        decreaseT = new Label("<");
        increaseT = new Label(">");
        decrease = new IFButton(30,30);
        increase = new IFButton(30,30);
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
        save.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){
                    boolean b = rs.b1.speichereBildUnter(pathField.getText()+nameField.getText()+".png");
            } 
        });
        
        MouseListener dML = new MouseListener(){
            public void mouseClicked(MouseEvent e){
                Manager.mode--;
                if(Manager.mode < 0){
                    Manager.mode = modeNames.length - 1;
                }
                modeName.setText(modeNames[Manager.mode]);
                Manager.refresh();
            }
            
            public void mouseExited(MouseEvent e){
                decrease.setColor(new Color(255,0,255));
                decreaseT.setBackground(new Color(255,0,255));
            }
            
            public void mousePressed(MouseEvent e){
                decrease.increaseCR(4, 0.3);
            }
            
            public void mouseEntered(MouseEvent e){
                decrease.setColor(new Color(150,0,150));
                decreaseT.setBackground(new Color(150,0,150));
            }
            
            public void mouseReleased(MouseEvent e){
                decrease.decreaseCR(4, 0.3);
            }
        };
        
        MouseListener iML = new MouseListener(){
            public void mouseClicked(MouseEvent e){
                Manager.mode++;
                if(Manager.mode >= modeNames.length){
                    Manager.mode = 0;
                }
                modeName.setText(modeNames[Manager.mode]);
                Manager.refresh();
            }
            
            public void mouseExited(MouseEvent e){
                increase.setColor(new Color(255,0,255));
                increaseT.setBackground(new Color(255,0,255));
            }
            
            public void mousePressed(MouseEvent e){
                increase.increaseCR(4, 0.3);
            }
            
            public void mouseEntered(MouseEvent e){
                increase.setColor(new Color(150,0,150));
                increaseT.setBackground(new Color(150,0,150));
            }
            
            public void mouseReleased(MouseEvent e){
                increase.decreaseCR(4, 0.3);
            }
        };
        
        decrease.addMouseListener(dML);
        decreaseT.addMouseListener(dML);
        increase.addMouseListener(iML);
        increaseT.addMouseListener(iML);
        
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
          
          decreaseT.setBounds(5,5,20,20);
          decreaseT.setFont(normal);
          decreaseT.setForeground(new Color(255,255,255));
          decreaseT.setBackground(new Color(255,0,255));
          decrease.add(decreaseT);
          
          decrease.setBounds(30,180,30,30);
          decrease.setCornerRadius(1);
          f.add(decrease);
          
          modeName.setBounds(65,180,100,30);
          modeName.setFont(normal);
          f.add(modeName);
          
          increaseT.setBounds(5,5,20,20);
          increaseT.setFont(normal);
          increaseT.setForeground(new Color(255,255,255));
          increaseT.setBackground(new Color(255,0,255));
          increase.add(increaseT);
              
          increase.setBounds(170,180,30,30);
          increase.setCornerRadius(1);
          f.add(increase);
          
          label.setBounds(30,30,250,40);
          f.add(label);
          label.setFont(heading);
          
          f.setSize(w, h);
          f.setVisible(true);
    }
}
