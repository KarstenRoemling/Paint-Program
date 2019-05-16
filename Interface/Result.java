import basis.*;
import java.util.*;
import java.awt.*;

public class Result
{
    public Fenster f;
    public IgelStift s;
    private Bild b1;

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
    
}