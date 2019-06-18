import java.awt.*;
import java.util.*;
import basis.*;

/**
 * Die Manager-Klasse Kontrolliert und instanziiert andere Klassen wie Surface und Result.
 * In ihrer Methode public static void main(String[] args) wird das Programm gestarten
 * Außerdem werden hier statische Variablen und Methoden gespeichert, damit man von allen anderen Klassen darauf zugreifen kann.
 * 
 * @Jonathan Hölzer & Karsten Römling
 * @18.06.2019
 */

public class Manager
{
    public static Result result;
    public static Surface sf;
    public static int w;
    public static int h;
    public static int mode = 0;
    public static int swap = 0;
    public static boolean swapping = false;
    
    /**
     * Diese Methode startet das Programm. Es werden die Höhe und die Breite des Bildschirms erfasst, die beiden großen Fenster und hauptsächlichen Klassen Surface und Result instanziiert und Standardwerte gesetzt.
     */
    public static void main(String[] args){
        w = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
        h = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
        result = new Result();
        sf = new Surface(result);
        refresh();
    }
    
    /**
     * Wird aufgerufen, wenn das Werkzeug mit dem "<"- bzw. ">"-Button geändert wird. Aktualisiert das Bild der IgelStift-Objekte, setzt Standardwerte.
     */
    public static void refresh(){
        result.rightClick = false;
        result.b1.setzeMitMausVerschiebbar(false);
        switch(mode){
            case 0:
                result.s.setzeBild("pinsel.png");
                break;
            case 5:
            case 4:
            case 1:
            case 3:
            case 6:
                result.s.setzeBild("kreuz.png");
                break;
            case 7:
                result.s.setzeBild("eraser.png");
                break;
            case 8:
                result.s.setzeBild("fill.png");
                break;
            case 9:
                result.s.setzeBild("getColor.png");
                break;
        }
        result.demo.setzeBild("empty.png");
        sf.refresh();
        result.defaults();
    }
}
