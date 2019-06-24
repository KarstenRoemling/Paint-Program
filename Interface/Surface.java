import basis.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.util.*;
import java.io.*;
import basis.swing.*;

/**
 * Die Klasse Surface kontrolliert hauptsächlich die Benutzeroberfläche und hält das Einstellungsfenster.
 * Hier werden Buttons, Texte, TextFields etc. erstellt, ihnen Eigenschaften zugeteilt und MouseListener etc. hinzugefügt.
 * Auch wichtige Funktionen des Programms wie das Abspeichern von Bildern finden hier statt.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */

public class Surface
{
    private Frame f;
    private IgelStift stift;
    private Result rs;
    
    private String[] modeNames = {"Pinsel", "Linie", "Text", "Kreis", "Rechteck", "Vieleck", "Linienkreis", "Radierer","Fläche füllen","Farbe kriegen"};
    private String savePath = "..\\Images\\bild.png";
    private String pathD = "..\\Images\\";
    private String pathN = "name";
    
    //Buttons
    private IFButton decrease;
    private IFButton increase;
    private IFButton save;
    private IFButton load;
    private IFButton clear;
    private IFButton undo;
    private IFButton redo;
    private IFButton farbanzeige;
    private IFButton bgClrSetter;
    private IFButton confirmBG;
    
    //Labels
    private IFLabel modeName;
    private IFLabel waText;
    private IFLabel stText;
    private IFLabel dtText;
    
    //Links
    private IFLink wikiLink;
    
    private RoundedOrNot ex;
    
    //Checkboxes
    private IFCheckbox cRound;
    
    public IFLabel debugInfo;
    
    //TextFields
    private IFTextField pathField;
    private IFTextField nameField;
    private IFTextField r;
    private IFTextField g;
    private IFTextField b;
    private IFTextField sThickness;
    private IFTextField BGX;
    private IFTextField BGY;
    
    //Farbwerte
    public int oldR = 0;
    public int oldG = 0;
    public int oldB = 0;
    
    //Einstellungen
    public int thick = 10;
    public Color bgColor;
    public boolean rounded = true;
    public boolean eraseWhite = false;
    
    //Standard-Fonts
    public Font subHeading = new Font("Dosis", Font.BOLD, 24);
    public Font heading = new Font("Dosis", Font.BOLD, 35);
    public Font normal = new Font("Dosis", Font.PLAIN, 18);
    public Font small = new Font("Dosis", Font.PLAIN, 12);
    
    //Liste der Elemente, die wieder entfernt werden, wenn ein neues Werkzeug gewählt wird.
    public ArrayList<IFComponent> infos;
    
    
    /**
     * Konstruktormethode der Klasse Surface: Hier werden KeyListener, MouseListener etc. zugeteilt, Attribute initialisiert, die Benutzeroberfläche wird geladen.
     * 
     * @param result     Das Result-Objekt, über das das Bild gestaltet wird.
     */
    public Surface(Result result)
    {
        rs = result;
        
        f = new Frame("Werkzeuge und Einstellungen");
        
        //Buttons
        clear = new IFButton(80,30,205,500,"Leeren");
        undo = new IFButton(50,30,290,500,"UNDO");
        redo = new IFButton(50,30,345,500,"REDO");
        decrease = new IFButton(30,30,30,500, "<");
        increase = new IFButton(30,30,170,500, ">");
        save = new IFButton(100,20,440,180,"Bild speichern");
        load = new IFButton(100,20,545,180,"Bild laden");
        farbanzeige = new IFButton(30,30,135,340, "");
        bgClrSetter = new IFButton(160,30,170,340, "Als Hintergrundfarbe");
        confirmBG = new IFButton(135,30,140,205, "Bildgröße setzen");
        
        //Labels
        modeName = new IFLabel(100,30,65,500,modeNames[Manager.mode]);
        dtText = new IFLabel(350,30,30,135, "Datei (Dateipfad, Bildgröße)");
        waText = new IFLabel(300,30,30,455,"Werkzeugauswahl");
        stText = new IFLabel(300,30,30,295, "Stift (Farbe, Stiftdicke)");
        debugInfo = new IFLabel(100,30,650,180, "0; 0");
        
        //Links
        wikiLink = new IFLink(100,15,30,90,"Bedienungsanleitung","https://github.com/KarstenRoemling/Paint-Program/wiki");
        
        cRound = new IFCheckbox(160,30,100,375,rounded, "Abgerundet");
        
        //TextFields
        pathField = new IFTextField(300,20,30,180,IFTextField.T_TEXT);
        pathField.setText(pathD);
        nameField = new IFTextField(100,20,335,180,IFTextField.T_TEXT);
        nameField.setText(pathN);
        r = new IFTextField(30,30,30,340,IFTextField.T_NUMBER);
        r.setText("0");
        g = new IFTextField(30,30,65,340,IFTextField.T_NUMBER);
        g.setText("0");
        b = new IFTextField(30,30,100,340,IFTextField.T_NUMBER);
        b.setText("0");
        sThickness = new IFTextField(30,30,30,375,IFTextField.T_NUMBER);
        sThickness.setText("10");
        BGX = new IFTextField(50,30,30,205,IFTextField.T_NUMBER);
        BGY = new IFTextField(50,30,85,205,IFTextField.T_NUMBER);
        
        updateExample();
        rs.s.setzeLinienBreite(thick);
        
        bgColor = Color.WHITE;
        
        infos = new ArrayList<IFComponent>();
        
        //KeyListerners
        r.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent k){}
            public void keyReleased(KeyEvent k){}
            
            public void keyTyped(KeyEvent k){
                if(validText(r.getText())){
                    oldR = r.getNumber();
                    updateColor();
                }else{
                    r.setText(String.valueOf(oldR));
                }
            }
        });
        
        g.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent k){}
            public void keyReleased(KeyEvent k){}
            
            public void keyTyped(KeyEvent k){
                if(validText(g.getText())){
                    oldG = g.getNumber();
                    updateColor();
                }else{
                    g.setText(String.valueOf(oldG));
                }
            }
        });
        
        b.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent k){}
            public void keyReleased(KeyEvent k){}
            
            public void keyTyped(KeyEvent k){
                if(validText(b.getText())){
                    oldB = b.getNumber();
                    updateColor();
                }else{
                    b.setText(String.valueOf(oldB));
                }
            }
        });
        
        sThickness.addKeyListener(new KeyListener(){
            public void keyPressed(KeyEvent k){}
            public void keyReleased(KeyEvent k){}
            
            public void keyTyped(KeyEvent k){
                if(validTextNormal1(sThickness.getText())){
                    thick = sThickness.getNumber();
                    if(thick==0){
                        thick = 1;
                    }
                    else if(thick>1000){
                        thick = 1000;
                        sThickness.setText("1000");
                    }
                    rs.s.setzeLinienBreite(thick);
                    updateExample();
                }else{
                    sThickness.setText(String.valueOf(thick));
                }
            }
        });
        
        //MouseListeners
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
        
        confirmBG.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                if(BGX.getNumber()>0 && BGY.getNumber()>0){
                    rs.b1.setzeGroesse((double)BGX.getNumber(), (double)BGY.getNumber());
                    setFromHistory();
                }else{
                    new Info("Sowohl die Breite als auch die Höhe des Bildes muss über 0 sein!", true);
                }
            }
            
            public void mouseExited(MouseEvent e){
                confirmBG.setColor(new Color(10,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                confirmBG.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                confirmBG.setColor(new Color(40,50,140));
            }
            
            public void mouseReleased(MouseEvent e){
                confirmBG.animCR(1, -0.2);
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
                undo.setColor(new Color(10,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                undo.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                undo.setColor(new Color(40,50,140));
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
                redo.setColor(new Color(10,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                redo.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                redo.setColor(new Color(40,50,140));
            }
            
            public void mouseReleased(MouseEvent e){
                redo.animCR(1, -0.2);
            }
        });
        
        bgClrSetter.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                Color c = new Color(oldR, oldG, oldB);
                rs.clearAll(c);
                bgColor = c;
            }
            
            public void mouseExited(MouseEvent e){
                bgClrSetter.setColor(new Color(10,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                bgClrSetter.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                bgClrSetter.setColor(new Color(40,50,140));
            }
            
            public void mouseReleased(MouseEvent e){
                bgClrSetter.animCR(1, -0.2);
            }
        });
        
        save.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                savingProcedure(false);
            }
            
            public void mouseExited(MouseEvent e){
                save.setColor(new Color(10,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                save.animCR(3, 0.1);
            }
            
            public void mouseEntered(MouseEvent e){
                save.setColor(new Color(40,50,140));
            }
            
            public void mouseReleased(MouseEvent e){
                save.animCR(1, -0.1);
            }
        });
        
        cRound.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e){
                cRound.handleClick();
                rounded = cRound.getChecked();
                updateExample();
            }
            
            public void mouseExited(MouseEvent e){
                cRound.setColor(new Color(10,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                cRound.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                cRound.setColor(new Color(40,50,140));
            }
            
            public void mouseReleased(MouseEvent e){
                cRound.animCR(1, -0.2);
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
                load.setColor(new Color(10,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                load.animCR(3, 0.1);
            }
            
            public void mouseEntered(MouseEvent e){
                load.setColor(new Color(40,50,140));
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
                decrease.setColor(new Color(10,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                decrease.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                decrease.setColor(new Color(40,50,140));
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
                increase.setColor(new Color(10,30,100));
            }
            
            public void mousePressed(MouseEvent e){
                increase.animCR(3, 0.2);
            }
            
            public void mouseEntered(MouseEvent e){
                increase.setColor(new Color(40,50,140));
            }
            
            public void mouseReleased(MouseEvent e){
                increase.animCR(1, -0.2);
            }
        };
        
        decrease.addMouseListener(dML);
        increase.addMouseListener(iML);
        
        
        //Icon des Fensters wird gesetzt
        try {
            File path = new File("logo.png");
            java.awt.Image icon = ImageIO.read(path);
            f.setIconImage(icon);
        } catch (Exception e) {}
        
        //WindowsListener (true,true) -> das gesamte Programm wird geschlossen und ein Dialog wird davor angezeigt.
        f.addWindowListener(new WindowManager(true, true));
        launchFrame();
        
        //Standartwerte für Dateipfad und -name werden errechnet, indem nach nicht bereits belegten Dateinamen gesucht wird.
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
    
    /**
     * Nachdem die Farbe geändert wurde, sorgt diese Methode dafür, dass das RoundedOrNot-Objekt, das den Stift repräsentiert, die Eingabefelder für die Farbe und ggf. der gezeichnete Text mit dem Werkzeug "Text" auf die aktuelle 
     * Farbe aktualisiert werden.
     */
    public void updateColor(){
        //Setzt die Eingabefelder auf die aktuelle Farbe.
        r.setText(String.valueOf(oldR));
        g.setText(String.valueOf(oldG));
        b.setText(String.valueOf(oldB));
        
        //Setzt die Anzeigen für die Farbe und den IgelStift selbst auf die aktuelle Farbe.
        farbanzeige.setColor(new Color(oldR, oldG, oldB));
        rs.s.setzeFarbe(new Color(oldR, oldG, oldB));
        updateExample();
        
        //Zeichnet den Text mit der neuen Farbe neu.
        if(Manager.mode == 2){
            if(!rs.newText){
                Manager.swap--;
                setFromHistory();
            }else{
                rs.newText = false;
            }
            rs.setPos(rs.textX, rs.textY);
            IFTextField t = (IFTextField) infos.get(3);
            rs.s.schreibeText(t.getText());
            rs.backup();
        }
    }
    
    /**
     * Fragt den Nutzer, ob das Bild wirklich gespeichert werden soll, wenn an dem angegebenen Pfad bereits ein Bild gespeichert ist. Speichert ansonsten das Bild unter dem angegebenen Pfad und informiert den Nutzer, ob 
     * das Speichern erfolgreich geklappt hat.
     * 
     * @param accepted    Der boolean gibt an, ob der Nutzer bereits zugestimmt hat, das Bild zu speichern. Wenn dies der Fall ist, wird nicht erneut über einen Dialog gefragt, ob das Bild wirklich gespeichert werden soll.
     */
    public void savingProcedure(boolean accepted){
        if(new File(getPath(false)).exists() && !accepted){
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
            
            IFButton accept = new IFButton(100,40,0,0,"Fortfahren");
            accept.setFont(new Font("Dosis", Font.BOLD, 18));
            accept.setCornerRadius(1);
            accept.setColor(new Color(100,30,100));
            accept.addMouseListener(new MouseListener()
            {        
                public void mouseClicked(MouseEvent e){
                    savingProcedure(true);
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
            
            //Erstellt mit den kreierten Buttons einen Dialog.
            IFDialog d = new IFDialog("Unter dem angegebenen Pfad ist bereits ein Bild.\nWenn du fortfährst, wird das Bild überschrieben",false,250,500);
            d.setButtons(new IFButton[]{accept,cancel});
        }else{
            //Speichert das Bild und git über den Erfolg Auskunft.
            boolean success = rs.b1.speichereBildUnter(getPath(false));
            if(success){
                new Info("Das Bild wurde erfolgreich gespeichert.", false);
            }else{
                new Info("Das Bild konnte nicht gespeichert werden. Ein Fehler ist aufgetreten.", true);
            }
        }
    }
    
    /**
     * Diese Methode entfernt das RoundedOrNot-Objekt ex, das den Stift in seiner Größe, Farbe und Form repräsentieren soll, vom Frame und fügt es als neues Objekt mit aktualisierten Eigenschaften wieder hinzu.
     */
    public void updateExample(){
        if(ex != null){
            f.remove(ex);
        }
        ex = new RoundedOrNot(thick, rounded, new Color(oldR, oldG, oldB));
        f.add(ex);
    }
    
    /**
     * Diese Methode kontrolliert, ob der eingegebene Text als Farbwert korrekt ist.
     * 
     * @param str     Der Text, der auf seine Korrektheit kontrolliert werden soll.
     * 
     * @return     Das boolean, das angibt, ob der Text als Farbwert korrekt ist.
     */
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
        if(str.length() == 0){
            valid = true;
        }
        return valid;
    }
    
    /**
     * Diese Methode kontrolliert, ob der eingegebene Text als Wert für die Stiftdicke korrekt ist.
     * 
     * @param str     Der Text, der auf seine Korrektheit kontrolliert werden soll.
     * 
     * @return     Das boolean, das angibt, ob der Text als Wert für die Stiftdicke korrekt ist.
     */
    public boolean validTextNormal1(String str){
        boolean valid = true;
        try{
            int i = Integer.parseInt(str);
            if(i < 1){
                valid = false;
            }
        }catch(Exception e){
            valid = false;
        }
        if(str.length() == 0){
            valid = true;
        }
        return valid;
    }
    
    /**
     * Diese Methode gibt den Pfad einschließlich dem Dateinamen und der Dateiendung durch die Eingabefelder wieder. Wenn ein Backslash zwischen Dateipfad und Dateiname fehlt, wird er hinzugefügt.
     * 
     * @param big     Das boolean, das angibt, ob der die Dateiendung in Groß- oder Kleinbuchstaben angegeben werden soll.
     * 
     * @return     Gibt den gesamten Dateipfad als String aus.
     */
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
    
    /**
     * Diese Methode lädt das Bild aus dem angegebenen Dateipfad. Wenn es dabei einen Fehler gibt, erscheint eine Fehlermeldung.
     * 
     * @param big     Das boolean, das angibt, ob der die Dateiendung in Groß- oder Kleinbuchstaben verwendet werden soll.
     */
    public void loadImage(boolean big){
        try{
            rs.b1.ladeBild(getPath(big));
            BGX.setText(String.valueOf(rs.b1.breite()));
            BGY.setText(String.valueOf(rs.b1.hoehe()));
        }
        catch(Exception ex){
            new Info("Das Bild von diesem Pfad kann nicht geladen werden:\n"+getPath(big), true);
        }
    }
    
    /**
     * Lädt das aktuelle Bild aus dem Verlauf und setzt es als Bild.
     */
    public void setFromHistory(){
        if(Manager.swap < rs.history.size() && Manager.swap >= 0){
            rs.b1.loescheAlles();
            rs.b1.setzeBilddaten(rs.history.get(Manager.swap).getClone());
        }
        debugInfo.setText(String.valueOf(Manager.swap)+"; "+String.valueOf(rs.history.size()));
    }
    
    /**
     * Erstellt alle Elemente der Benutzeroberfläche, lädt ihre Eigenschaften und fügt sie zum Frame hinzu. Setzt die Höhe und Breite des Frames und macht es sichtbar.
     */
    public void launchFrame() {
          int w = (int)(Manager.w / 2)-20;
          int h = (int)(Manager.h * 0.96);
          
          IFLabel label = new IFLabel(250,50,30,45,"Einstellungen");
          
          f.setLayout(null);
          
          pathField.setCornerRadius(1);
          pathField.setPositionType(IFTextField.P_LEFT);
          pathField.setFont(small);
          pathField.setBorder(1);
          f.add(pathField);
          
          nameField.setCornerRadius(1);
          nameField.setPositionType(IFTextField.P_LEFT);
          nameField.setFont(small);
          nameField.setBorder(1);
          f.add(nameField);
          
          r.setCornerRadius(1);
          f.add(r);
          g.setCornerRadius(1);
          f.add(g);
          b.setCornerRadius(1);
          f.add(b);
          
          sThickness.setCornerRadius(1);
          f.add(sThickness);
          
          BGX.setCornerRadius(1);
          BGX.setText(String.valueOf(rs.b1.breite()));
          f.add(BGX);
          
          BGY.setCornerRadius(1);
          BGY.setText(String.valueOf(rs.b1.hoehe()));
          f.add(BGY);
          
          waText.setFont(subHeading);
          waText.setPositionType(IFLabel.P_LEFT_CENTER);
          f.add(waText);
          
          wikiLink.setFont(small);
          wikiLink.setPositionType(IFLabel.P_LEFT_CENTER);
          f.add(wikiLink);
          
          dtText.setFont(subHeading);
          dtText.setPositionType(IFLabel.P_LEFT_CENTER);
          f.add(dtText);
          
          stText.setFont(subHeading);
          stText.setPositionType(IFLabel.P_LEFT_CENTER);
          f.add(stText);
          
          debugInfo.setFont(normal);
          f.add(debugInfo);
          debugInfo.setVisible(false);
          
          farbanzeige.setCornerRadius(1);
          farbanzeige.setColor(new Color(0,0,0));
          f.add(farbanzeige);
          
          cRound.setCornerRadius(1);
          f.add(cRound);
          
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
          
          confirmBG.setFont(normal);
          confirmBG.setCornerRadius(1);
          f.add(confirmBG);

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
    
    /**
     * Wird durch Manager.refresh() ausgelöst, wenn der Nutzer das Werkzeug wechselt. Zeichnet mit den dafür erstellten Methoden "public void createInfoBox(int y, String txt, Color bgColor)", "public void createWET(int y)" und
     * "public void createWE(IFComponent we)" InfoBoxes und Werkzeugeinstellungen. Die davor auf dem Frame befindlichen, zu einem bestimmten Werkzeug gehörenden Komponenten werden von Frame entfernt.
     */
    public void refresh(){
        for(int i = 0; i < infos.size(); i++){
            f.remove(infos.get(i));
        }
        infos.clear();
        switch(Manager.mode){
            case 0:
                createInfoBox(535, "Freihandzeichnen: Klicke und ziehe mit der Maus in jede Richtung.\nHöre auf zu drücken, wenn du nicht mehr zeichnen möchtst.", new Color(140,140,255));
                break;
            case 1:
                createInfoBox(535, "Linien zeichnen: Suche dir einen Punkt aus, klicke und ziehe zu einem anderen Punkt und lasse los.\nZwischen Startpunkt und Endpunkt entsteht nun eine Linie.", new Color(140,140,255));
                break;
            case 2:
                createInfoBox(535, "Text erzeugen: Klicke auf einen Startpunkt im Bild und bearbeite Text,\nSchriftart und Schriftgröße in den Werkzeugeinstellungen.", new Color(140,140,255));
                
                //Werkzeugeinstellungen für den Text: erstellt drei IFTextFields. Wenn sich ihr Text verändert, wird der text mit den neuen Eigenschaften neu gezeichnet.
                rs.s.setzeSchriftArt("Dosis");
                rs.s.setzeSchriftGroesse(20);
                createWET(615);
                IFTextField text = new IFTextField(500,30,30,660,IFTextField.T_TEXT);
                text.setCornerRadius(1);
                text.setPositionType(IFTextField.P_LEFT);
                text.addKeyListener(new KeyListener(){
                    public void keyPressed(KeyEvent k){}
                    public void keyReleased(KeyEvent k){}
                    
                    public void keyTyped(KeyEvent k){
                        //Mit UNDO wird das Bild auf das vorherige Bild zurückgesetzt...
                        if(!rs.newText){
                            Manager.swap--;
                            setFromHistory();
                        }else{
                            rs.newText = false;
                        }
                        rs.setPos(rs.textX, rs.textY);
                        
                        //...neu gezeichnet...
                        rs.s.schreibeText(text.getText());
                        rs.s.setzeBild("textEditing.png");
                        
                        //...und neu zum Verlauf hinzugefügt.
                        rs.backup();
                    }
                });
                createWE(text);
                IFTextField textFont = new IFTextField(100,30,30,695,IFTextField.T_TEXT);
                textFont.setCornerRadius(1);
                textFont.setPositionType(IFTextField.P_LEFT);
                textFont.setText("Dosis");
                textFont.addKeyListener(new KeyListener(){
                    public void keyPressed(KeyEvent k){}
                    public void keyReleased(KeyEvent k){}
                    
                    public void keyTyped(KeyEvent k){
                        if(!rs.newText){
                            Manager.swap--;
                            setFromHistory();
                        }else{
                            rs.newText = false;
                        }
                        rs.s.setzeSchriftArt(textFont.getText());
                        rs.setPos(rs.textX, rs.textY);
                        rs.s.schreibeText(text.getText());
                        rs.s.setzeBild("textEditing.png");
                        rs.backup();
                    }
                });
                createWE(textFont);
                IFTextField textSize = new IFTextField(40,30,135,695,IFTextField.T_NUMBER);
                textSize.setCornerRadius(1);
                textSize.setPositionType(IFTextField.P_LEFT);
                textSize.setText("20");
                textSize.addKeyListener(new KeyListener(){
                    public void keyPressed(KeyEvent k){}
                    public void keyReleased(KeyEvent k){}
                    
                    public void keyTyped(KeyEvent k){
                        if(!rs.newText){
                            Manager.swap--;
                            setFromHistory();
                        }else{
                            rs.newText = false;
                        }
                        rs.s.setzeSchriftGroesse(textSize.getNumber());
                        rs.setPos(rs.textX, rs.textY);
                        rs.s.schreibeText(text.getText());
                        rs.s.setzeBild("textEditing.png");
                        rs.backup();
                    }
                });
                createWE(textSize);
                break;
            case 3:
                createInfoBox(535, "Kreise zeichnen: Klicke und ziehe soweit, wie du möchtest,\num die Größe des Kreises festzulegen.", new Color(140,140,255));
                createInfoBox(590, "Lasse los und der Kreis erscheint in der von dir ausgewählten Größe.", new Color(140,140,255));
                break;
            case 4:
                createInfoBox(535, "Rechtecke zeichnen: Klicke und ziehe soweit, wie du möchtest,\num die Größe des Rechtecks festzulegen.", new Color(140,140,255));
                createInfoBox(590, "Lasse los und das Rechteck erscheint in der von dir ausgewählten Größe.", new Color(140,140,255));
                break;
            case 5:
                createInfoBox(535, "Mit diesem Werkzeug kannst du Vielecke erzeugen.\nKlicke auf ein das Bild, um den Startpunkt auszuwählen. Klicke danach mehrmals, um eine Form zu erzeugen.", new Color(140,140,255));
                createInfoBox(590, "Wenn du \"z\" drückst, wird das Vieleck vervollständigt.", new Color(140,140,255));
                createInfoBox(645, "Mit der Taste \"c\" wird das Erstellen abgebrochen.", new Color(140,140,255));
                break;
            case 6:
                createInfoBox(535, "Linienkreise: Klicke auf einen Punkt und ziehe den Mauszeiger kreisförmig darum.\nVom Startpunkt bis zum Mauszeiger entstehen nun Linien.", new Color(140,140,255));
                break;
            case 7:
                createInfoBox(535, "Radierer: Mit diesem Werkzeug kannst du Gezeichnetes löschen. Klicke und ziehe über\netwas Gezeichnetes und es nimmt die Farbe des Hintergrunds an.", new Color(140,140,255));
                
                //Werkzeugeinstellungen für den Radierer: Soll er mit weiß zeichnen?
                createWET(615);
                IFCheckbox cb = new IFCheckbox(300,30,30,660,eraseWhite,"Mit weiß radieren");
                cb.setCornerRadius(1);
                cb.addMouseListener(new MouseListener()
                {
                    public void mouseClicked(MouseEvent e){
                        cb.handleClick();
                        eraseWhite = cb.getChecked();
                    }
                    
                    public void mouseExited(MouseEvent e){
                        cb.setColor(new Color(10,30,100));
                    }
                    
                    public void mousePressed(MouseEvent e){
                        cb.animCR(3, 0.2);
                    }
                    
                    public void mouseEntered(MouseEvent e){
                        cb.setColor(new Color(40,50,140));
                    }
                    
                    public void mouseReleased(MouseEvent e){
                        cb.animCR(1, -0.2);
                    }
                });
                createWE(cb);
                break;
            case 8:
                createInfoBox(535,"Fläche füllen: Klicke auf eine geschlossene Fläche und sie füllt sich mit der ausgewählten Farbe. ", new Color(140,140,255));
                break;
            case 9:
                createInfoBox(535, "Farbe kriegen: Klicke auf einen Punkt im Bild und die Farbe des Punkts wird als Stiftfarbe übernommen.", new Color(140,140,255));
                break;
        }
    }
    
    /**
     * Erstellt eine InfoBox, fügt sie zum Frame hinzu und zur ArrayList<IFComponent>, damit sie wieder gelöscht werden kann, wenn ein neues Werkzeug ausgewählt wird. Die InfoBox besteht aus 2 IFLabels.
     * 
     * @param y     y-Position, auf der die InfoBox erstellt werden soll.
     * @param txt     Der Text der InfoBox.
     * @param bgColor     Die Hintergrundfarbe der InfoBox.
     */
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
    
    /**
     * Erstellt ein IFLabel als Titel für die Werkzeugeinstellungen mit der Aufschrift "Werkzeugeinstellungen" und fügt es zum Frame hinzu und zur ArrayList<IFComponent>, damit es wieder gelöscht werden kann, wenn ein neues Werkzeug ausgewählt wird.
     * 
     * @param y     y-Position, auf der der Titel erstellt werden soll.
     */
    public void createWET(int y){
        IFLabel we = new IFLabel(300,30,30,y,"Werkzeugeinstellungen");
        we.setFont(subHeading);
        we.setPositionType(IFLabel.P_LEFT_CENTER);
        f.add(we);
        infos.add(we);
    }
    
    /**
     * Erstellt ein Werkzeug, indem es zur ArrayList<IFComponent>, damit es wieder gelöscht werden kann, wenn ein neues Werkzeug ausgewählt wird, und zum Frame hinzugefügt wird.
     * 
     * @param we     Das Werkzeug, das hinzugefügt wird (ein IFComponent)
     */
    public void createWE(IFComponent we){
        f.add(we);
        infos.add(we);
    }
    
    /**
     * Gibt aus den Werten für Rot, Grün und Blau, die in der Klasse Surface gespeichert sind, ein Color-Objekt zurück (Eigentlich eine sinnvolle Methode, ich glaube allerdings, dass ich sie nie verwendet habe).
     * 
     * @return     das Color-Objekt, das sich aus den Werten für Rot, Grün und Blau ergibt.
     */
    public Color getFgColor(){
        return new Color(oldR, oldG, oldB);
    }
}
