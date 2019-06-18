import basis.*;
import basis.swing.*;
import basis.util.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.lang.*;
import javax.imageio.*;
import java.io.*;

/**
 * Die Klasse Result steuert das Verhalten des rechten Fensters, das hauptsächlich die basis-Bibliothek verwendet und das gezeichnete Bild zeichnet.
 * Methoden wie drawLine(int x1, int y1, int x2, int y2, boolean r) oder drawCircle(int px, int y, int r) zeichnen mit einem basis.IgelStift Elemente
 * in ein basis.Bild.
 * Außerdem wird hier mit basis.swing.Picture der Verlauf in einer ArrayList<Picture> gespeichert und es wird auf Eingaben des Nutzers,
 * beispielsweise durch die Maus reagiert.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */

public class Result implements MausLauscherStandard, MausLauscherErweitert, TastenLauscher
{
    public Fenster f;
    public IgelStift s;
    public IgelStift demo; //Als zusätzlicher Cursor, der z.B. beim Ziehen einer Linie den Startpunkt markiert.
    public Bild b1;
    
    private int mouseXStart = 0;
    private int mouseYStart = 0;
    private int completeXStart = 0;
    private int completeYStart = 0;
    private boolean firstClick = true;
    private boolean neu;
    
    public ArrayList<Picture> history; //Verlauf
    
    public boolean rightClick;
    public int textX;
    public int textY;
    public boolean newText;
    
    /**
     * Konstruktormethode der Klasse Result. Hier werden Fenster, IgelStifte und Co erzeugt, das Fenster angepasst und zahlreiche Attribute initialisiert.
     */
    public Result(){
        f = new Fenster();
        s = new IgelStift();
        demo = new IgelStift();
        
        history = new ArrayList<Picture>();
        
        int w = (int)(Manager.w / 2)-20;
        int h = (int)(Manager.h * 0.915);
        
        f.setzeGroesse(w+25, h);
        f.setzePosition(w, 0);
        f.setzeTitel("Zeichnen");
        b1 = new Bild(12, (int)(Manager.h * 0.48), (int)(Manager.w * 0.5)-20, (int)(Manager.h * 0.48));
        b1.setzeHintergrundFarbe(Farbe.WEISS);
        f.setzeHintergrundFarbe(Farbe.HELLGRAU);
        s.maleAuf(b1);
        demo.maleAuf(b1);
        demo.setzeBild("empty.png");
        b1.setzeMitMausVerschiebbar(false);
        
        f.setzeMausLauscherStandard(this);
        f.setzeMausLauscherErweitert(this);
        f.setzeTastenLauscher(this);
        
        //Setzt das Icon des Fensters
        try {
            File path = new File("logo.png");
            java.awt.Image icon = ImageIO.read(path);
            f.getMeinJFrame().setIconImage(icon);
        } catch (Exception e) {}
        
        backup();
    }
    
    /**
     * Fügt das aktuelle Bild zum Verlauf hinzu, setzt die aktuelle Position in diesem Verlauf und löscht ggf. Bilder aus dem Verlauf, die durch Rückgängig-Machen und neues Bearbeiten an älterer Stelle sinnlos geworden sind.
     */
    public void backup(){
        if(Manager.swap != history.size()-1){
            for (int i = history.size()-1; i > Manager.swap; i--) {
               history.remove(i);
            }
        }
        history.add(b1.holeBilddatenkopie());
        Manager.swap = history.size() -1;
        if(Manager.sf != null){
            Manager.sf.debugInfo.setText(String.valueOf(Manager.swap)+"; "+String.valueOf(history.size()));
        }
    }
    
    /**
     * Zeichnet eine Linie zwischen den angegebenen Punkten, wobei die Punkte selbst nicht ausgefüllt werden, sondern hier wirklich nur gestartet wird.
     * 
     * @param w1     der x-Wert des Startpunkts
     * @param h1     der y-Wert des Startpunkts
     * @param w2     der x-Wert des Endpunkts
     * @param h2     der y-Wert des Endpunkts
     * @param r     gibt an, ob die Linie abgerundet sein soll.
     */
    public void drawLine(int w1, int h1, int w2, int h2, boolean r){
        if(r){
            Vektor2D vInit = new Vektor2D(w1,h1,w2,h2);
            int i = Manager.sf.thick/2;
            if(w1 != w2 || h1 != h2){
                do{
                    Vektor2D v = new Vektor2D(vInit.getRichtung(), i);
                    drawPoint(w1+(int)v.getDx(), h1+(int)v.getDy(), r);
                    i++;
                }while(i < vInit.getLaenge()-Manager.sf.thick/2);
            }else{
                drawPoint(w1,h1,r);
            }
            setPos(w2,h2);
        }else{
            setPos(w1,h1);
            s.runter();
            s.bewegeBis(w2,h2);
            s.hoch();
        }
    }
    
    /**
     * Zeichnet eine Linie zwischen den angegebenen Punkten, wobei die Punkte selbst ausgefüllt werden. Wenn die Stiftgröße 10 ist und r=true ist, ist hier beispielsweise der Startpunkt bereits Mittelpunkt des ersten Kreises.
     * 
     * @param w1     der x-Wert des Startpunkts
     * @param h1     der y-Wert des Startpunkts
     * @param w2     der x-Wert des Endpunkts
     * @param h2     der y-Wert des Endpunkts
     * @param r     gibt an, ob die Linie abgerundet sein soll.
     */
    public void drawLineEnding(int w1, int h1, int w2, int h2, boolean r){
        Vektor2D vInit = new Vektor2D(w1,h1,w2,h2);
        for(int i = 0; i < vInit.getLaenge(); i++){
            Vektor2D v = new Vektor2D(vInit.getRichtung(), i);
            drawPoint(w1+(int)v.getDx(), h1+(int)v.getDy(), r);
        }
        setPos(w2,h2);
    }
    
    /**
     * Leert das Bild, setzt die Hintergrundfarbe, sichert das aktuelle Bild.
     * 
     * @param c     Die Color, mit der das geleerte Bild gefüllt werden soll (Wenn man auf "Leeren" drückt, ist die Farbe weiß, wenn man auf "Als Hintergrundfarbe" klickt, ist die Farbe die aktuell ausgewählte Farbe).
     */
    public void clearAll(Color c){
        b1.loescheAlles();
        b1.setzeHintergrundFarbe(c);
        Manager.refresh();
        backup();
    }
    
    /**
     * Standardwerte bzw. Standardwert, wird bei Manager.refresh() aufgerufen.
     */
    public void defaults(){
        firstClick = true;
    }
    
    /**
     * Setzt die Position des IgelStifts s, ohne eine Spur zu hinterlassen.
     * 
     * @param x     x-Position des IgelStifts
     * @param y     y-Position des IgelStifts
     */
    public void setPos(int x, int y){
        s.hoch();
        s.bewegeBis(x, y);
    }
    
    /**
     * Setzt die Position des IgelStifts demo, ohne eine Spur zu hinterlassen.
     * 
     * @param x     x-Position des IgelStifts
     * @param y     y-Position des IgelStifts
     */
    public void setDemoPos(int x, int y){
        demo.hoch();
        demo.bewegeBis(x,y);
    }
    
    /**
     * Zeichnet an dem Punkt P(x|y) einen Punkt in der aktuellen Stiftdicke und als Kreis oder Quadrat.
     * 
     * @param x     x-Koordinate des Punkts
     * @param y     y-Koordinate des Punkts
     * 
     * @param r     gibt an, ob es sich bei dem Punkt um ein Kreis oder ein Quadrat handelt.
     */
    public void drawPoint(int x, int y, boolean r){
        setPos(x,y);
        if(r){
            s.setzeLinienBreite(Manager.sf.thick/2);
            s.zeichneKreis(Manager.sf.thick/4);
            s.setzeLinienBreite(Manager.sf.thick);
        }else{
            drawLine(x,y,x,y,false);
        }
    }
    
    /**
     * Zeichnet eine Kreis.
     * 
     * @param px     x-Position des Kreismittelpunkts
     * @param px     y-Position des Kreismittelpunkts
     * @param r     Radius des Kreises
     */
    public void drawCircle(int px, int py, int r){
        //Brensenham Algorhitmus mit Elementen aus http://www-lehre.informatik.uni-osnabrueck.de/~cg/2000/skript/3_4_Kreis.html
        int x,y,d,dx,dxy;
        x=0; y=r; d=1-r;
        dx=3; dxy=-2*r+5;
        while (y>=x)
        {
           drawPoint(px+x, py+y, true);
           drawPoint(px+y, py+x, true);
           drawPoint(px+y, py-x, true);
           drawPoint(px+x, py-y, true);
           drawPoint(px-x, py-y, true);
           drawPoint(px-y, py-x, true);
           drawPoint(px-y, py+x, true);
           drawPoint(px-x, py+y, true);

           if (d<0) { d=d+dx;  dx=dx+2; dxy=dxy+2; x++; }
           else { d=d+dxy; dx=dx+2; dxy=dxy+4; x++; y--; }
        }
    }
    
    /**
     * Zeichnet ein Rechteck mit einer Diagonale, dessen Start- und Endpunkt durch die Parameter festgelegt wird.
     * 
     * @param x1     x-Position des Startpunkts der Diagonale des Rechtecks
     * @param y1     y-Position des Startpunkts der Diagonale des Rechtecks
     * @param x2     x-Position des Endpunkts der Diagonale des Rechtecks
     * @param y2     y-Position des Endpunkts der Diagonale des Rechtecks
     */
    public void drawRectangle(int x1, int y1, int x2, int y2){
        s.hoch();
        s.bewegeBis(x1, y1);
        s.runter();
        s.dreheBis(0);
        s.bewegeUm(x2-x1);
        s.dreheUm(270);
        s.bewegeUm(y2-y1);
        s.dreheUm(270);
        s.bewegeUm(x2-x1);
        s.dreheUm(270);
        s.bewegeUm(y2-y1);
        s.dreheUm(270);
        s.hoch();
        s.bewegeBis(x2, y2);
    }
    
    /**
     * Kontrolliert, ob ein Punkt auf dem Bild liegt.
     * 
     * @param b     das Bild, auf dem der Punkt liegen soll.
     * @param x     x-Koordinate des Punkts
     * @param y     y-Koordinate des Punkts
     * 
     * @return     Gibt ein boolean aus, das angibt, ob der mit x und y dargestellte Punkt auf dem Bild b liegt.
     */
    public boolean matches(Bild b, int x, int y){
        return x >= b.hPosition() && x <= b.hPosition()+b.breite() && y >= b.vPosition() && y <= b.vPosition()+b.hoehe();
    }
    
    /**
     * Gibt die Position einer x-Koordinate aus der Perspektive des Bildes b aus.
     * 
     * @param b     das Bild, von dem aus die x-Koordinate betrachtet wird.
     * @param x     die x-Koordinate
     * 
     * @return     gibt ein Integer aus, das die x-Koordinate aus der Perspektive des Bildes angibt.
     */
    public int getRealX(Bild b, int x){
        return x - (int)b.hPosition();
    }
    
    /**
     * Gibt die Position einer y-Koordinate aus der Perspektive des Bildes b aus.
     * 
     * @param b     das Bild, von dem aus die x-Koordinate betrachtet wird.
     * @param y     die y-Koordinate
     * 
     * @return     gibt ein Integer aus, das die y-Koordinate aus der Perspektive des Bildes angibt.
     */
    public int getRealY(Bild b, int y){
        return y - (int)b.vPosition();
    }
    
    /**
     * Durch das Interface MausLauscherStandard eingefügt. Reagiert auf Mausbewegung.
     * 
     * @param o     keine Ahnung, was das für ein Objekt ist. In der Dokumentation der basis-Bibliothek wird man darüber leider auch nicht aufgeklärt.
     * @param x     x-Position der Maus
     * @param y     y-Position der Maus
     */
    public void bearbeiteMausBewegt(java.lang.Object o, int x, int y){
        if(!matches(b1,x,y)){
            return;
        }
        x = getRealX(b1, x);
        y = getRealY(b1, y);
        setPos(x, y);
        switch(Manager.mode){
            case 2:
                s.setzeBild("newText.png");
                break;
        }
    }
    
    /**
     * Durch das Interface MausLauscherStandard eingefügt. Reagiert auf das Drücken der Maustaste.
     * 
     * @param o     keine Ahnung, was das für ein Objekt ist. In der Dokumentation der basis-Bibliothek wird man darüber leider auch nicht aufgeklärt.
     * @param x     x-Position der Maus
     * @param y     y-Position der Maus
     */
    public void bearbeiteMausDruck(java.lang.Object o, int x, int y){
        if(!matches(b1,x,y)){
            return;
        }
        x = getRealX(b1, x);
        y = getRealY(b1, y);
        if(!rightClick){
            switch(Manager.mode){
                case 0:
                    neu = true;
                    break;
                case 7:
                    s.setzeFarbe(Manager.sf.bgColor);
                    if(Manager.sf.eraseWhite){
                        s.setzeFarbe(Color.WHITE);
                    }
                    neu = true;
                    break;
                case 6:
                    s.setzeBild("pointer.png");
                    mouseXStart = x;
                    mouseYStart = y;
                    break;
                case 4:
                case 3:
                case 1:
                    setDemoPos(x,y);
                    demo.setzeBild("start.png");
                    s.setzeBild("kreuzDragging.png");
                    mouseXStart = x;
                    mouseYStart = y;
                    break;
            }
        }
    }
    
    /**
     * Durch das Interface MausLauscherStandard eingefügt. Reagiert auf das Drücken der rechten Maustaste.
     * 
     * @param o     keine Ahnung, was das für ein Objekt ist. In der Dokumentation der basis-Bibliothek wird man darüber leider auch nicht aufgeklärt.
     * @param x     x-Position der Maus
     * @param y     y-Position der Maus
     */
    public void bearbeiteMausDruckRechts(java.lang.Object o, int x, int y) {}
    
    /**
     * Durch das Interface MausLauscherStandard eingefügt. Reagiert auf das Loslassen der rechten Maustaste.
     * 
     * @param o     keine Ahnung, was das für ein Objekt ist. In der Dokumentation der basis-Bibliothek wird man darüber leider auch nicht aufgeklärt.
     * @param x     x-Position der Maus
     * @param y     y-Position der Maus
     */
    public void bearbeiteMausLosRechts(java.lang.Object o, int x, int y){}
    
    /**
     * Durch das Interface MausLauscherStandard eingefügt. Reagiert auf das Ziehen der Maus.
     * 
     * @param o     keine Ahnung, was das für ein Objekt ist. In der Dokumentation der basis-Bibliothek wird man darüber leider auch nicht aufgeklärt.
     * @param x     x-Position der Maus
     * @param y     y-Position der Maus
     */
    public void bearbeiteMausGezogen(java.lang.Object o, int x, int y){
        if(!matches(b1,x,y)){
            return;
        }
        x = getRealX(b1, x);
        y = getRealY(b1, y);
        setPos(x,y);
        if(!rightClick){
            switch(Manager.mode){
                case 7:
                case 0:
                    if(neu){
                        drawPoint(x,y,Manager.sf.rounded);
                        neu = false;
                    }else{
                        drawLineEnding(mouseXStart, mouseYStart, x, y, Manager.sf.rounded);
                    }
                    mouseXStart = x;
                    mouseYStart = y;
                    break;
                case 1:
                    break;
                case 6:
                    drawLine(mouseXStart, mouseYStart, x, y, Manager.sf.rounded);
                    break;
            }
        }
    }
    
    /**
     * Durch das Interface MausLauscherStandard eingefügt. Reagiert auf das Loslassen der Maustaste.
     * 
     * @param o     keine Ahnung, was das für ein Objekt ist. In der Dokumentation der basis-Bibliothek wird man darüber leider auch nicht aufgeklärt.
     * @param x     x-Position der Maus
     * @param y     y-Position der Maus
     */
    public void bearbeiteMausLos(java.lang.Object o, int x, int y){
        x = getRealX(b1, x);
        y = getRealY(b1, y);
        if(!rightClick){
            switch(Manager.mode){
                case 4:
                    s.setzeBild("kreuz.png");
                    drawRectangle(mouseXStart, mouseYStart, x, y);
                    demo.setzeBild("empty.png");
                    backup();
                    break;
                case 1:
                    s.setzeBild("kreuz.png");
                    drawLine(mouseXStart, mouseYStart, x, y, Manager.sf.rounded);
                    demo.setzeBild("empty.png");
                    backup();
                    break;
                case 6:
                    s.setzeBild("kreuz.png");
                    backup();
                    break;
                case 3:
                    s.setzeBild("kreuz.png");
                    int rad = (int)Math.sqrt((double)Math.pow(mouseXStart - x, 2)+Math.pow(mouseYStart - y, 2));
                    drawCircle(mouseXStart,mouseYStart,rad);
                    s.hoch();
                    s.bewegeBis(x, y);
                    demo.setzeBild("empty.png");
                    backup();
                    break;
                case 0:
                    backup();
                    break;
                case 7:
                    backup();
                    s.setzeFarbe(Manager.sf.getFgColor());
                    break;
            }
        }
    }
    
    /**
     * Durch das Interface MausLauscherErweitert eingefügt. Reagiert auf das einen Doppelklick.
     * 
     * @param o     keine Ahnung, was das für ein Objekt ist. In der Dokumentation der basis-Bibliothek wird man darüber leider auch nicht aufgeklärt.
     * @param x     x-Position der Maus
     * @param y     y-Position der Maus
     */
    public void bearbeiteDoppelKlick(java.lang.Object o, int x, int y){}
    
    /**
     * Durch das Interface MausLauscherErweitert eingefügt. Reagiert auf das einen Mausklick.
     * 
     * @param o     keine Ahnung, was das für ein Objekt ist. In der Dokumentation der basis-Bibliothek wird man darüber leider auch nicht aufgeklärt.
     * @param x     x-Position der Maus
     * @param y     y-Position der Maus
     */
    public void bearbeiteMausKlick(java.lang.Object o, int x, int y){
        if(!matches(b1,x,y)){
            return;
        }
        x = getRealX(b1, x);
        y = getRealY(b1, y);
        if(!rightClick){
            switch(Manager.mode){
                case 7:
                case 0:
                    drawPoint(x,y,Manager.sf.rounded);
                    backup();
                    break;
                case 5:            
                    if(!firstClick){
                        drawLineEnding(mouseXStart, mouseYStart, x, y, Manager.sf.rounded);
                        mouseXStart = x;
                        mouseYStart = y;
                    }else{
                        completeXStart = x;
                        completeYStart = y;
                        mouseXStart = x;
                        mouseYStart = y;
                        s.setzeBild("kreuzDragging.png");
                        firstClick = false;
                    }
                    break;
                case 8:
                    s.fuelleMitFarbeAn((double)x, (double)y,new Color (Manager.sf.oldR, Manager.sf.oldG, Manager.sf.oldB));
                    backup();
                    break;
                case 2:
                    setDemoPos(x,y);
                    demo.setzeBild("textStart.png");
                    textX = x;
                    textY = y;
                    newText = true;
                    IFTextField text = (IFTextField)Manager.sf.infos.get(1);
                    text.setText("");
                    break;
                case 9:
                    Color c = b1.farbeVon(x,y);
                    Manager.sf.oldR = c.getRed();
                    Manager.sf.oldG = c.getGreen();
                    Manager.sf.oldB = c.getBlue();
                    Manager.sf.updateColor();
                    break;
            }
        }
    }
    
    /**
     * Durch das Interface MausLauscherErweitert eingefügt. Reagiert auf einen Rechtsklick.
     * 
     * @param o     keine Ahnung, was das für ein Objekt ist. In der Dokumentation der basis-Bibliothek wird man darüber leider auch nicht aufgeklärt.
     * @param x     x-Position der Maus
     * @param y     y-Position der Maus
     */
    public void bearbeiteMausKlickRechts(java.lang.Object o, int x, int y){
        if(!matches(b1,x,y)){
            return;
        }
        b1.setzeMitMausVerschiebbar(!rightClick);
        rightClick = !rightClick;
        if(rightClick){
            s.setzeBild("drag.png");   
        }
        else{
            Manager.refresh();
        }
    }
    
    public void bearbeiteMausHeraus(java.lang.Object o, int x, int y){}
    
    public void bearbeiteMausHinein(java.lang.Object o, int x, int y){}
    
    /**
     * Durch das Interface TastenLauscher eingefügt. Reagiert auf Tastenbetätigung.
     * 
     * @param sender     Vermutlich die Komponente, von die Betätigung der Tastatur ausgeht.
     * @param t     Gedrückter Character, gedrückte Taste
     */
    public void bearbeiteTaste(Komponente sender, char t){
        if(rightClick){
            return;
        }
        if(!rightClick){
            switch(Manager.mode){
                case 5:
                    switch(t){
                        case 'z':
                            drawLineEnding(mouseXStart, mouseYStart, completeXStart, completeYStart, Manager.sf.rounded);
                            firstClick = true;
                            s.setzeBild("kreuz.png");
                            backup();
                            break;
                        case 'c':
                            firstClick = true;
                            s.setzeBild("kreuz.png");
                            backup();
                            break;
                    }
                break;
            }
        }
    }
}