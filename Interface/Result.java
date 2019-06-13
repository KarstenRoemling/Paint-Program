import basis.*;
import basis.swing.*;
import basis.util.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.lang.*;


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
    public ArrayList<BufferedImage> history2;
    
    public boolean rightClick;
    

    public Result(){
        f = new Fenster();
        s = new IgelStift();
        history = new ArrayList<Picture>();
        history2 = new ArrayList<BufferedImage>();
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
        history.add(b1.holeBilddatenkopie());
        Manager.swap = history.size() -1;
        if(Manager.sf != null){
            Manager.sf.debugInfo.setText(String.valueOf(Manager.swap)+"; "+String.valueOf(history.size()));
        }
    }
    
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
    
    public void drawLineEnding(int w1, int h1, int w2, int h2, boolean r){
        Vektor2D vInit = new Vektor2D(w1,h1,w2,h2);
        for(int i = 0; i < vInit.getLaenge(); i++){
            Vektor2D v = new Vektor2D(vInit.getRichtung(), i);
            drawPoint(w1+(int)v.getDx(), h1+(int)v.getDy(), r);
        }
        setPos(w2,h2);
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
                    drawLine(mouseXStart, mouseYStart, x, y, Manager.sf.rounded);
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
                case 7:
                    backup();
                    s.setzeFarbe(Manager.sf.getFgColor());
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