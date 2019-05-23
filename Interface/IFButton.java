import java.awt.*;
import java.awt.image.*;
import java.util.concurrent.*;

//Für diese Klasse habe ich zwei ganze Tage gebraucht...

public class IFButton extends IFComponent
{
    public Color clr;
    public double cr;//cornerRadius
    public int pl;//paddingLeft
    public int pt;//paddingTop
    private int exPermission = 0;
    
    public IFButton(int width, int height){
        super(width,height);
        clr = new Color(255,0,255);
        cr = 10;
        paintST();
    }
    
    public void paintST(){
        fillCanvas(new Color(255,255,255));
        int color = clr.getRGB();
        for(int by = 0; by < h; by++){
            for(int bx = 0; bx < w; bx++){
                if(by <= h/2){
                    if(bx <= w/2){
                        //double num = cr*(1-Math.sin(0.5 * Math.PI *((double)by / (double)cr)));
                        //double num = cr*(Math.pow(((double)cr-(double)by) / (double)cr, 2));
                        double num = cr*(Math.pow((double)by / cr, -1));
                        if(bx > (int)num){
                            b.setRGB(bx, by, color);
                        }
                        else if(bx == (int)num){
                            b.setRGB(bx, by, mixOfColors(new Color(255,255,255), clr, (double)(Math.floor(num)+1.0)-(double)num).getRGB());
                        }
                        
                        double num2 = cr*(Math.pow((double)bx / cr, -1));
                        if(by == (int)num2){
                            b.setRGB(bx, by, mixOfColors(new Color(b.getRGB(bx,by)), clr, (double)(Math.floor(num2)+1.0)-(double)num2).getRGB());
                        }
                    }
                    else{
                        b.setRGB(bx, by, b.getRGB(w-bx, by));
                    }
                }
                else{
                    b.setRGB(bx, by, b.getRGB(bx, h-by));
                }
            }
        }
        repaint();
    }
    
    public int mixOf(double value1, double value2, double preference){
        return (int) ((value2 - value1) * preference + value1);
    }
    
    public Color mixOfColors(Color c1, Color c2, double pref){
        return new Color(mixOf((double)c1.getRed(), (double)c2.getRed(), pref),mixOf((double)c1.getGreen(), (double)c2.getGreen(), pref),mixOf((double)c1.getBlue(), (double)c2.getBlue(), pref));
    }
    
    public void setColor(Color color){
        clr = color;
        paintST();
    }
    
    public void setCornerRadius(double ncr){
        cr = ncr;
        paintST();
    }
    
    public void increaseCR(double change, double changePE){
        exPermission = 1;
        double oldCR = cr;
        final ScheduledExecutorService eS = Executors.newSingleThreadScheduledExecutor();
        eS.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(cr >= oldCR + change || !(exPermission == 1)){
                    eS.shutdown();
                }else{
                    crPlusMinus(true, changePE);
                }
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void crPlusMinus(boolean plus, double changePE) {
        if(plus){
            cr += changePE;
        }else{
            cr -= changePE;
        }
        paintST();
    }
    
    public void decreaseCR(double change, double changePE){
        exPermission = -1;
        double oldCR = cr;
        final ScheduledExecutorService eS = Executors.newSingleThreadScheduledExecutor();
        eS.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(cr <= oldCR - change || !(exPermission == -1)){
                    eS.shutdown();
                }else{
                    crPlusMinus(false, changePE);
                }
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }
}
