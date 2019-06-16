import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.util.concurrent.*;

public class IFTextField extends IFComponent{
    public Color clr;
    public String text;
    public boolean focused;
    private int type;
    private boolean lineVisible;
    public double cr;
    public double border;
    public int positionType;
    public Font font;
    public Color foregroundColor;
    public Color focusColor;
    public Color standardColor;
    
    public static int T_TEXT = 0;
    public static int T_NUMBER = 1;
    public static int P_LEFT = 1;
    public static int P_CENTER = 0;
    public static String numbers = "0123456789";
    
    public IFTextField(int width, int height, int x, int y, int typeP){
        super(width,height,x,y);
        
        clr = new Color(10,30,100);
        standardColor = clr;
        foregroundColor = new Color(0,0,0);
        focusColor = new Color(100,30,100);
        font = new Font("Dosis", Font.BOLD, 20);
        text = "";
        type = typeP;
        border = 3;
        
        setCursor(new Cursor(Cursor.TEXT_CURSOR));
        
        paintST();
        
        final int typef = type;
        
        addKeyListener(new KeyListener(){
            public void keyReleased(KeyEvent k){
                
            }
            
            public void keyPressed(KeyEvent k){
                
            }
            
            public void keyTyped(KeyEvent k){
                if(focused){
                    if((int)k.getKeyChar() == 8){
                        if(text.length() != 0){
                            text = text.substring(0, text.length() - 1);
                            repaint();
                        }
                    }else if((int)k.getKeyChar() == 22){
                        if(type == 0){
                            try{
                                String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                                text += data;
                                repaint();
                            }catch(Exception e){}
                        }
                    }else{
                        switch(typef){
                            case 0:
                                if(!k.isActionKey()){
                                    text += k.getKeyChar();
                                    repaint();
                                }
                                break;
                            case 1:
                                if(numbers.contains(Character.toString(k.getKeyChar()))){
                                    text += k.getKeyChar();
                                    repaint();
                                }else if('+' == k.getKeyChar()){
                                    text = String.valueOf(getNumber() + 1);
                                    repaint();
                                }else if('-' == k.getKeyChar()){
                                    text = String.valueOf(getNumber() - 1);
                                    repaint();
                                }
                                break;
                        }
                    }
                }
            }
        });
        
        addFocusListener(new FocusListener(){
            public void focusLost(FocusEvent f){
                focused = false;
                setColor(standardColor);
            }
            
            public void focusGained(FocusEvent f){
                focused = true;
                setColor(focusColor);
            }
        });
        
        final ScheduledExecutorService eS = Executors.newSingleThreadScheduledExecutor();
        eS.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if(focused){
                    lineVisible = !lineVisible;
                    repaint();
                }else if(lineVisible){
                    lineVisible = false;
                    repaint();
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }
    
    public void paintST(){
        roundedBgWithBorder(cr, clr, border, Color.WHITE);
        repaint();
    }
    
    public void setColor(Color color){
        clr = color;
        paintST();
    }
    
    public void setStandardColor(Color color){
        standardColor = color;
        clr = color;
        paintST();
    }
    
    public void setFocusColor(Color color){
        focusColor = color;
        paintST();
    }
    
    public void setForegroundColor(Color fgClr){
        foregroundColor = fgClr;
        repaint();
    }
    
    public void setCornerRadius(double ncr){
        cr = ncr;
        paintST();
    }
    
    public void setBorder(double nb){
        border = nb;
        paintST();
    }
    
    public void setText(String txt){
        text = txt;
        repaint();
    }
    
    public void setFont(Font fnt){
        font = fnt;
        repaint();
    }
    
    public void setPositionType(int pt){
        positionType = pt;
        repaint();
    }
    
    public String getText(){
        return text;
    }
    
    public int getNumber(){
        if(type == 1 && text.length() > 0){
            return Integer.parseInt(text);
        }else{
            return 0;
        }
    }
    
    public void afterImage(Graphics2D g2){
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setFont(font);
        
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector gv = g2.getFont().createGlyphVector(frc, "Clear");
        int strHeight = (int)gv.getPixelBounds(null, 2, 2).getHeight();
        gv = g2.getFont().createGlyphVector(frc, text);
        int strWidth = (int)gv.getPixelBounds(null, 2, 2).getWidth();
        
        int x = 0;
        if(positionType == 0){
            x = (w - strWidth)/2;
        }else{
            x = (int)border+1;
        }
        int y = (h - strHeight)/2 + strHeight;
        g2.setPaint(foregroundColor);
        g2.drawString(text, x, y);
        if(lineVisible){
            g2.drawLine(x+strWidth, y-strHeight, x+strWidth, y);
        }
    }
    
    public void beforeImage(){
        
    }
}
