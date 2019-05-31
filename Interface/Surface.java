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
    private IFButton save;
    private IFButton load;
    private IFButton clear;
    private IFButton undo;
    private IFButton redo;
    
    private IFLabel modeName;
    private IFLabel waText;
    
    private TextField pathField;
    private TextField nameField;
    
    public Surface(Result result)
    {
        rs = result;
        Manager.refresh();
        
        f = new Frame("Werkzeuge und Einstellungen");
        
        clear = new IFButton(80,30,205,180,"Clear");
        undo = new IFButton(50,30,290,180,"UNDO");
        redo = new IFButton(50,30,345,180,"REDO");
        decrease = new IFButton(30,30,30,180, "<");
        increase = new IFButton(30,30,170,180, ">");
        save = new IFButton(100,20,440,85,"Bild speichern");
        load = new IFButton(100,20,545,85,"Bild laden");
        modeName = new IFLabel(100,30,65,180,modeNames[Manager.mode]);
        waText = new IFLabel(220,30,30,145,"Werkzeugauswahl");
        pathField = new TextField(pathD);
        nameField = new TextField(pathN);
        
        clear.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                rs.clearAll();
            }
            
            public void mouseExited(MouseEvent e){
                clear.setColor(new Color(255,0,0));
            }
            
            public void mousePressed(MouseEvent e){
                clear.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                clear.setColor(new Color(150,0,0));
            }
            
            public void mouseReleased(MouseEvent e){
                clear.animCR(1, -0.2);
            }
        });
        
        undo.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                Manager.swap--;
                if(Manager.swap < 0){
                    Manager.swap = 0;
                }
                setFromHistory();
            }
            
            public void mouseExited(MouseEvent e){
                undo.setColor(new Color(255,0,255));
            }
            
            public void mousePressed(MouseEvent e){
                undo.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                undo.setColor(new Color(150,0,150));
            }
            
            public void mouseReleased(MouseEvent e){
                undo.animCR(1, -0.2);
            }
        });
        
        redo.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                Manager.swap++;
                if(Manager.swap >= rs.history.size()){
                    Manager.swap = rs.history.size()-1;
                }
                setFromHistory();
            }
            
            public void mouseExited(MouseEvent e){
                redo.setColor(new Color(255,0,255));
            }
            
            public void mousePressed(MouseEvent e){
                redo.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                redo.setColor(new Color(150,0,150));
            }
            
            public void mouseReleased(MouseEvent e){
                redo.animCR(1, -0.2);
            }
        });
        
        save.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                boolean success = rs.b1.speichereBildUnter(getPath(false));
                if(success){
                    new Info("Das Bild wurde erfolgreich gespeichert.", false);
                }else{
                    new Info("Das Bild konnte nich gespeichert werden. Ein Fehler ist aufgetreten.", true);
                }
            }
            
            public void mouseExited(MouseEvent e){
                save.setColor(new Color(255,0,255));
            }
            
            public void mousePressed(MouseEvent e){
                save.animCR(3, 0.1);
            }
            
            public void mouseEntered(MouseEvent e){
                save.setColor(new Color(150,0,150));
            }
            
            public void mouseReleased(MouseEvent e){
                save.animCR(1, -0.1);
            }
        });
        
        load.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                if(new File(getPath(false)).exists()){
                    loadImage(false);
                }else if(new File(getPath(true)).exists()){
                    loadImage(true);
                }else{
                    new Info("Das Bild konnte unter diesem Pfad nicht gefunden werden:\n"+getPath(false), true);
                }
            }
            
            public void mouseExited(MouseEvent e){
                load.setColor(new Color(255,0,255));
            }
            
            public void mousePressed(MouseEvent e){
                load.animCR(3, 0.1);
            }
            
            public void mouseEntered(MouseEvent e){
                load.setColor(new Color(150,0,150));
            }
            
            public void mouseReleased(MouseEvent e){
                load.animCR(1, -0.1);
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
            }
            
            public void mousePressed(MouseEvent e){
                decrease.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                decrease.setColor(new Color(150,0,150));
            }
            
            public void mouseReleased(MouseEvent e){
                decrease.animCR(1, -0.2);
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
            }
            
            public void mousePressed(MouseEvent e){
                increase.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                increase.setColor(new Color(150,0,150));
            }
            
            public void mouseReleased(MouseEvent e){
                increase.animCR(1, -0.2);
            }
        };
        
        decrease.addMouseListener(dML);
        increase.addMouseListener(iML);
        
        f.addWindowListener(new WindowManager(true));
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
    
    public String getPath(boolean big){
        String pathTo = pathField.getText();
        if(pathTo.charAt(pathTo.length() - 1) != '\\'){
            pathTo += "\\";
        }
        String ending = ".png";
        if(big){
            ending = ".PNG";
        }
        return pathTo+nameField.getText()+ending;
    }
    
    public void loadImage(boolean big){
        try{
            rs.b1.ladeBild(getPath(big));
        }
        catch(Exception ex){
            new Info("Das Bild von diesem Pfad kann nicht geladen werden:\n"+getPath(big), true);
        }
    }
    
    public void setFromHistory(){
        if(Manager.swap < rs.history.size() && Manager.swap >= 0){
            rs.b1.setzeBilddaten(rs.history.get(Manager.swap));
        }
    }
    
    public void launchFrame() {
          int w = (int)(Manager.w / 2)-20;
          int h = (int)(Manager.h * 0.96);
          
          Font subHeading = new Font("Dosis", Font.PLAIN, 24);
          Font heading = new Font("Dosis", Font.BOLD, 30);
          Font normal = new Font("Dosis", Font.PLAIN, 18);
          Font small = new Font("Dosis", Font.PLAIN, 12);
          
          IFLabel label = new IFLabel(250,40,30,30,"Einstellungen");
          
          f.setLayout(null);
          
          pathField.setBounds(30, 85, 300, 20);
          f.add(pathField);
          
          nameField.setBounds(335, 85, 100, 20);
          f.add(nameField);
          
          waText.setFont(subHeading);
          waText.setPositionType(IFLabel.P_LEFT_CENTER);
          f.add(waText);
          
          clear.setFont(normal);
          clear.setCornerRadius(1);
          clear.setColor(new Color(255,0,0));
          f.add(clear);
          
          undo.setFont(normal);
          undo.setCornerRadius(1);
          f.add(undo);
          
          redo.setFont(normal);
          redo.setCornerRadius(1);
          f.add(redo);
          
          save.setFont(small);
          save.setCornerRadius(1);
          f.add(save);
          
          load.setFont(small);
          load.setCornerRadius(1);
          f.add(load);

          decrease.setCornerRadius(1);
          f.add(decrease);
          
          modeName.setFont(normal);
          f.add(modeName);

          increase.setCornerRadius(1);
          f.add(increase);
          
          f.add(label);
          label.setFont(heading);
          label.setUnderlining(true);
          label.setPositionType(IFLabel.P_LEFT_CENTER);
          
          f.setSize(w, h);
          f.setVisible(true);
    }
}
