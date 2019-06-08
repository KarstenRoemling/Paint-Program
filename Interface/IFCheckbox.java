import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.util.concurrent.*;

public class IFCheckbox extends IFComponent{
    public Color clr;
    public int border;
    public double cr;
    public boolean checked;
    public String text;
    public Font font;
    public Color textColor;
    
    private int exPermission = 0;
    
    public IFCheckbox(int width, int height, int x, int y, boolean check, String txt){
        super(width,height,x,y);
        b = new BufferedImage(height, height, BufferedImage.TYPE_INT_RGB);
        
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        text = txt;
        checked = check;
        clr = new Color(10,30,100);
        border = 3;
        textColor = Color.BLACK;
        font = new Font("Dosis", Font.PLAIN, 20);
        
        paintST();
    }
    
    public void handleClick(){
        setChecked(!checked);
    }
    
    public boolean getChecked(){
        return checked;
    }
    
    public void setChecked(boolean check){
        checked = check;
        repaint();
    }
    
    public void setCornerRadius(int value){
        cr = value;
        paintST();
    }
    
    public void setColor(Color c){
        clr = c;
        paintST();
    }
    
    public void setBorderWidth(int b){
        border = b;
        paintST();
    }
    
    public void setText(String txt){
        text = txt;
        repaint();
    }
    
    public void setFont(Font f){
        font = f;
        repaint();
    }
    
    public void paintST(){
        int oldW = w;
        w = h;
        roundedBgWithBorder(cr, clr, border, Color.WHITE);
        repaint();
        w = oldW;
    }
    
    public void beforeImage(Graphics2D g2){
        g2.setBackground(Color.WHITE);
    }
    
    public void animCR(double result, double changePE){
        final double myPerm;
        if(result < cr){
            exPermission = -1;
            myPerm = -1;
        }else{
            exPermission = 1;
            myPerm = 1;
        }
        final double oldCR = cr;
        final ScheduledExecutorService eS = Executors.newSingleThreadScheduledExecutor();
        eS.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                boolean reached;
                if(myPerm == 1){
                    reached = cr >= result;
                }else{
                    reached = cr <= result;
                }
                if(reached || !(exPermission == myPerm)){
                    cr = result;
                    eS.shutdown();
                }else{
                    cr += changePE;
                    paintST();
                }
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }
    
    public void afterImage(Graphics2D g2){
        Font font2 = font;
        if(checked){
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
                
            g2.setPaint(clr);
            g2.fillOval(5,16,4,4);
            g2.fillPolygon(new int[]{9,5,10,25,23,10},new int[]{16,20,28,9,4,20}, 6);
            g2.fillOval(22,4,4,4);
            
            //font2 = new Font(font.getName(), Font.BOLD, font.getSize());
            //Funktioniert auch (macht den Text fett, wenn checked true ist), haben uns aber aus Design-Gründen dagegen entschieden.
        }
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        
        g2.setFont(font2);
        
        int w2 = w-10-h;
        
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, "Clear");
        int strHeight = (int)gv.getPixelBounds(null, 2, 2).getHeight();
        gv = g2.getFont().createGlyphVector(frc, text);
        int strWidth = (int)gv.getPixelBounds(null, 2, 2).getWidth();
        
        int x = 10+h;
        int y = (h - strHeight)/2 + strHeight;
        g2.setPaint(textColor);
        g2.drawString(text, x, y);
    }
}