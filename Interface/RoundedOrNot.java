import java.awt.*;

public class RoundedOrNot extends IFLabel{
    private boolean r;
    private Color clr;
    
    public RoundedOrNot(int thick, boolean rounded, Color color){
        super(thick, thick, 65+(30-thick)/2, 375+(30-thick)/2, "");
        
        r = rounded;
        clr = color;
    }
    
    public void beforeText(Graphics2D g2){
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(clr);
        if(r){
            g2.fillOval(0,0,w,w);
        }else{
            g2.fillRect(0,0,w,w);
        }
    }
}