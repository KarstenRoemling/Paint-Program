import java.awt.*;
import java.util.*;
import basis.*;

public class Manager
{
    public static Result result;
    public static Surface sf;
    public static int w;
    public static int h;
    public static int mode = 0;
    public static int swap = 0;
    public static boolean swapping = false;

    public static void main(String[] args){
        w = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
        h = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
        result = new Result();
        sf = new Surface(result);
        Manager.refresh();
    }
    
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
        }
        Manager.sf.refresh();
        result.defaults();
    }
}
