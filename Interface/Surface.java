import basis.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.applet.Applet;
import java.io.*;
import basis.swing.*;

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
    private IFButton farbanzeige;
    private IFButton bgClrSetter;
    
    private IFLabel modeName;
    private IFLabel waText;
    private IFLabel faText;
    
    public IFLabel debugInfo;
    
    private TextField pathField;
    private TextField nameField;
    private TextField r;
    private TextField g;
    private TextField b;
    
    private int oldR = 0;
    private int oldG = 0;
    private int oldB = 0;
    
    private ArrayList<IFLabel> infos;
    
    public Surface(Result result)
    {
        rs = result;
        
        f = new Frame("Werkzeuge und Einstellungen");
        
        clear = new IFButton(80,30,205,180,"Clear");
        undo = new IFButton(50,30,290,180,"UNDO");
        redo = new IFButton(50,30,345,180,"REDO");
        decrease = new IFButton(30,30,30,180, "<");
        increase = new IFButton(30,30,170,180, ">");
        save = new IFButton(100,20,440,85,"Bild speichern");
        load = new IFButton(100,20,545,85,"Bild laden");
        farbanzeige = new IFButton(30,30,505,180, "");
        bgClrSetter = new IFButton(160,30,540,180, "Als Hintergrundfarbe");
        
        modeName = new IFLabel(100,30,65,180,modeNames[Manager.mode]);
        waText = new IFLabel(220,30,30,145,"Werkzeugauswahl");
        faText = new IFLabel(220,30,400,145, "Farbauswahl");
        debugInfo = new IFLabel(100,30,705,180, "0; 0");
        
        pathField = new TextField(pathD);
        nameField = new TextField(pathN);
        r = new TextField("0");
        g = new TextField("0");
        b = new TextField("0");
        
        infos = new ArrayList<IFLabel>();
        
        r.addTextListener(new TextListener(){
            public void textValueChanged(TextEvent e){
                if(validText(r.getText())){
                    oldR = Integer.parseInt(r.getText());
                    farbanzeige.setColor(new Color(oldR, oldG, oldB));
                    rs.s.setzeFarbe(new Color(oldR, oldG, oldB));
                }else{
                    r.setText(String.valueOf(oldR));
                }
            }
        });
        
        g.addTextListener(new TextListener(){
            public void textValueChanged(TextEvent e){
                if(validText(g.getText())){
                    oldG = Integer.parseInt(g.getText());
                    farbanzeige.setColor(new Color(oldR, oldG, oldB));
                    rs.s.setzeFarbe(new Color(oldR, oldG, oldB));
                }else{
                    g.setText(String.valueOf(oldG));
                }
            }
        });
        
        b.addTextListener(new TextListener(){
            public void textValueChanged(TextEvent e){
                if(validText(b.getText())){
                    oldB = Integer.parseInt(b.getText());
                    farbanzeige.setColor(new Color(oldR, oldG, oldB));
                    rs.s.setzeFarbe(new Color(oldR, oldG, oldB));
                }else{
                    b.setText(String.valueOf(oldB));
                }
            }
        });
        
        clear.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                rs.clearAll(Color.WHITE);
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
        
        bgClrSetter.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                rs.clearAll(new Color(oldR, oldG, oldB));
            }
            
            public void mouseExited(MouseEvent e){
                bgClrSetter.setColor(new Color(255,0,255));
            }
            
            public void mousePressed(MouseEvent e){
                bgClrSetter.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                bgClrSetter.setColor(new Color(150,0,150));
            }
            
            public void mouseReleased(MouseEvent e){
                bgClrSetter.animCR(1, -0.2);
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
    
    public boolean validText(String str){
        boolean valid = true;
        try{
            int i = Integer.parseInt(str);
            if(i < 0 || i > 255){
                valid = false;
            }
        }catch(Exception e){
            valid = false;
        }
        return valid;
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
            rs.b1.loescheAlles();
            rs.b1.setzeBilddaten(rs.history.get(Manager.swap).getClone());
        }
        debugInfo.setText(String.valueOf(Manager.swap)+"; "+String.valueOf(rs.history.size()));
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
          
          r.setBounds(400,180,30,30);
          f.add(r);
          g.setBounds(435,180,30,30);
          f.add(g);
          b.setBounds(470,180,30,30);
          f.add(b);
          
          waText.setFont(subHeading);
          waText.setPositionType(IFLabel.P_LEFT_CENTER);
          f.add(waText);
          
          faText.setFont(subHeading);
          faText.setPositionType(IFLabel.P_LEFT_CENTER);
          f.add(faText);
          
          debugInfo.setFont(normal);
          f.add(debugInfo);
          debugInfo.setVisible(false);
          
          farbanzeige.setCornerRadius(1);
          farbanzeige.setColor(new Color(0,0,0));
          f.add(farbanzeige);
          
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
          
          bgClrSetter.setFont(normal);
          bgClrSetter.setCornerRadius(1);
          f.add(bgClrSetter);

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
          
          IFLabel i = new IFLabel(30,50,30,215, "i");

          f.setSize(w, h);
          f.setVisible(true);
    }
    
    public void refresh(){
        for(int i = 0; i < infos.size(); i++){
            f.remove(infos.get(i));
        }
        switch(Manager.mode){
            case 0:
                createInfoBox(215, "Freihandzeichnen: Klicke und ziehe mit der Maus in jede Richtung.\nHöre auf zu drücken, wenn du nicht mehr zeichnen möchtst.", new Color(140,140,255));
                break;
            case 5:
                createInfoBox(215, "Mit diesem Werkzeug kannst du Vielecke erzeugen.\nKlicke auf ein das Bild, um den Startpunkt auszuwählen. Klicke danach mehrmals, um eine Form zu erzeugen.", new Color(140,140,255));
                createInfoBox(270, "Wenn du \"z\" drückst, wird das Vieleck vervollständigt.", new Color(140,140,255));
                createInfoBox(325, "Mit der Taste \"c\" wird das Erstellen abgebrochen.", new Color(140,140,255));
                break;
        }
    }
    
    public void createInfoBox(int y, String txt, Color bgColor){
        int w = (int)(Manager.w / 2)-110;
        
        IFLabel i = new IFLabel(30,50,30,y, "i");
        IFLabel infoText = new IFLabel(w,50, 60, y, txt);
        Font ifont = new Font("Monospaced", Font.PLAIN, 24);
        Font infoTextfont = new Font("Dosis", Font.PLAIN, 15);
        
        i.setFont(ifont);
        infoText.setFont(infoTextfont);
        
        infoText.setPositionType(IFLabel.P_LEFT_CENTER);
        
        infoText.setForegroundColor(Color.WHITE);
        i.setForegroundColor(Color.WHITE);
        
        infoText.setColor(bgColor);
        i.setColor(bgColor);
        
        infos.add(i);
        infos.add(infoText);
        
        f.add(i);
        f.add(infoText);
    }
}
