import basis.*;
import java.util.*;
import java.awt.*;

public class Result
{
    public Fenster f;
    public IgelStift s;
    public Bild b1;

    public Result(){
        f = new Fenster();
        s = new IgelStift();
        int w = (int)(Manager.w / 2)-20;
        int h = (int)(Manager.h * 0.915);
        f.setzeGroesse(w+25, h);
        f.setzePosition(w, 0);
        f.setzeTitel("Resultat");
        b1 = new Bild(12, (int)(Manager.h * 0.48), (int)(Manager.w * 0.5)-20, (int)(Manager.h * 0.48));
        b1.setzeHintergrundFarbe(Farbe.WEISS);
        f.setzeHintergrundFarbe(Farbe.HELLGRAU);
        s.maleAuf(b1);
        b1.setzeMitMausVerschiebbar(true);
    }
    
    public void drawLine(int w1, int h1, int w2, int h2){
        s.hoch();
        s.bewegeBis(w1, h1);
        s.runter();
        s.bewegeBis(w2, h2);
    }
    
    public void clearAll(){
        b1.loescheAlles();
        b1.setzeHintergrundFarbe(Farbe.WEISS);
    }
    
    public void setPos(int x, int y){
        s.hoch();
        s.bewegeBis(x, y);
    }
    
    public void drawPoint2(int x, int y){
        s.hoch();
        s.bewegeBis(x, y);
        s.runter();
        s.bewegeUm(1);
        s.dreheUm(90);
        s.bewegeUm(1);
        s.dreheUm(90);
        s.bewegeUm(1);
        s.dreheUm(90);
        s.bewegeUm(1);
        s.dreheUm(90);
        s.hoch();
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
    
}