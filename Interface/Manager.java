import java.awt.*;
import java.util.*;
import basis.*;

public class Manager
{
    public static Result result;
    public static DrawSimulator ds;
    public static Surface sf;
    public static int w;
    public static int h;
    public static int mode = 0;

    public static void main(String[] args){
        w = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
        h = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
        result = new Result();
        sf = new Surface(result);
        ds = new DrawSimulator(sf, result);
    }
}
