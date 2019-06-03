import basis.*;
import basis.swing.*;
import java.util.*;
import java.awt.*;


public class Result implements MausLauscherStandard, MausLauscherErweitert, TastenLauscher
{
    public Fenster f;
    public IgelStift s;
    public Bild b1;
    
    private int mouseXStart = 0;
    private int mouseYStart = 0;
    private int completeXStart = 0;
    private int completeYStart = 0;
    private boolean firstClick = true;
    private boolean neu;
    
    public ArrayList<Picture> history;
    
    public boolean rightClick;
    

    public Result(){
        f = new Fenster();
        s = new IgelStift();
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
        b1.setzeMitMausVerschiebbar(false);
        
        f.setzeMausLauscherStandard(this);
        f.setzeMausLauscherErweitert(this);
        f.setzeTastenLauscher(this);
        
        backup();
    }
    
    private void backup(){
        if(Manager.swap != history.size()-1){
            for (int i = history.size()-1; i > Manager.swap; i--) {
               history.remove(i);
            }
        }
        history.add(b1.holeBilddaten());
        Manager.swap = history.size() -1;
    }
    
    public void drawLine(int w1, int h1, int w2, int h2){
        s.hoch();
        s.bewegeBis(w1, h1);
        s.runter();
        s.bewegeBis(w2, h2);
    }
    
    public void clearAll(Color c){
        b1.loescheAlles();
        b1.setzeHintergrundFarbe(c);
        Manager.refresh();
        backup();
    }
    
    public void defaults(){
        firstClick = true;
    }
    
    public void setPos(int x, int y){
        s.hoch();
        s.bewegeBis(x, y);
    }
    
    public void drawPoint(int x, int y){
        s.hoch();
        s.bewegeBis(x, y);
        s.runter();
        s.bewegeBis(x, y);
        s.hoch();
    }
    
    public void drawCircle(int px, int py, int r){
        //Brensenham Algorhitmus mit Elementen aus http://www-lehre.informatik.uni-osnabrueck.de/~cg/2000/skript/3_4_Kreis.html
        int x,y,d,dx,dxy;
        x=0; y=r; d=1-r;
        dx=3; dxy=-2*r+5;
        while (y>=x)
        {
           drawPoint(px+x, py+y);
           drawPoint(px+y, py+x);
           drawPoint(px+y, py-x);
           drawPoint(px+x, py-y);
           drawPoint(px-x, py-y);
           drawPoint(px-y, py-x);
           drawPoint(px-y, py+x);
           drawPoint(px-x, py+y);

           if (d<0) { d=d+dx;  dx=dx+2; dxy=dxy+2; x++; }
           else { d=d+dxy; dx=dx+2; dxy=dxy+4; x++; y--; }
        }
    }
    
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
    
    public boolean matches(Bild b, int x, int y){
        return x >= b.hPosition() && x <= b.hPosition()+b.breite() && y >= b.vPosition() && y <= b.vPosition()+b.hoehe();
    }
    
    public int getRealX(Bild b, int x){
        return x - (int)b.hPosition();
    }
    
    public int getRealY(Bild b, int y){
        return y - (int)b.vPosition();
    }
    
    public void bearbeiteMausBewegt(java.lang.Object o, int x, int y){
        if(!matches(b1,x,y)){
            return;
        }
        x = getRealX(b1, x);
        y = getRealY(b1, y);
        setPos(x, y);
        switch(Manager.mode){
            case 1:
                break;
        }
    }
    
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
                case 6:
                    s.setzeBild("pointer.png");
                    mouseXStart = x;
                    mouseYStart = y;
                    break;
                case 4:
                case 3:
                case 1:
                    s.setzeBild("kreuzDragging.png");
                    mouseXStart = x;
                    mouseYStart = y;
                    break;
            }
        }
    }
    
    public void bearbeiteMausDruckRechts(java.lang.Object o, int x, int y) {}
    
    public void bearbeiteMausLosRechts(java.lang.Object o, int x, int y){}
    
    public void bearbeiteMausGezogen(java.lang.Object o, int x, int y){
        if(!matches(b1,x,y)){
            return;
        }
        x = getRealX(b1, x);
        y = getRealY(b1, y);
        setPos(x,y);
        if(!rightClick){
            switch(Manager.mode){
                case 0:
                    if(neu){
                        drawPoint(x,y);
                        neu = false;
                    }else{
                        drawLine(mouseXStart, mouseYStart, x, y);
                    }
                    mouseXStart = x;
                    mouseYStart = y;
                    break;
                case 1:
                    break;
                case 6:
                    drawLine(mouseXStart, mouseYStart, x, y);
                    break;
            }
        }
    }
    
    public void bearbeiteMausLos(java.lang.Object o, int x, int y){
        x = getRealX(b1, x);
        y = getRealY(b1, y);
        if(!rightClick){
            switch(Manager.mode){
                case 4:
                    s.setzeBild("kreuz.png");
                    drawRectangle(mouseXStart, mouseYStart, x, y);
                    backup();
                    break;
                case 1:
                    s.setzeBild("kreuz.png");
                    drawLine(mouseXStart, mouseYStart, x, y);
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
                    backup();
                    break;
                case 0:
                    backup();
                    break;
            }
        }
    }
    
    public void bearbeiteDoppelKlick(java.lang.Object o, int x, int y){}
    
    public void bearbeiteMausKlick(java.lang.Object o, int x, int y){
        if(!matches(b1,x,y)){
            return;
        }
        x = getRealX(b1, x);
        y = getRealY(b1, y);
        if(!rightClick){
            switch(Manager.mode){
                case 0:
                    drawPoint(x, y);
                    backup();
                    break;
                case 5:            
                    if(!firstClick){
                        drawLine(mouseXStart, mouseYStart, x, y);
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
            }
        }
    }
    
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
    
    public void bearbeiteTaste(Komponente sender, char t){
        if(rightClick){
            return;
        }
        if(!rightClick){
            switch(Manager.mode){
                case 5:
                    switch(t){
                        case 'z':
                            drawLine(mouseXStart, mouseYStart, completeXStart, completeYStart);
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