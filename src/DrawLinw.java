/* Draw1Line -- Draw lines. 
   
  */
 

 import java.awt.event.MouseEvent; 
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import java.awt.Point; 
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Graphics; 
import javax.swing.BorderFactory;
import javax.swing.JFrame;
 
import javax.swing.JPanel;
 
 public class DrawLinw extends JPanel implements MouseListener, MouseMotionListener { 
        Point sPoint = new Point(-1, -1); 
        Point ePoint = new Point(-1, -1);  
                public DrawLinw(){ 
                    
              
    init();
 }
                public void init() {
               
            setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    /// Dimension d = getSize();
        setPreferredSize(new Dimension(100,100));
        addMouseListener(this);
        addMouseMotionListener(this);
        repaint();
                 
    }
  public void mouseDragged(MouseEvent e) {  
           
         
  }  
 
  public void mousePressed(MouseEvent e) {  
    
     e.consume();  
     sPoint.x = e.getX();  
     sPoint.y = e.getY();  
  }  
 
  public void mouseReleased(MouseEvent e) { 
       
     e.consume();  
     ePoint.x = e.getX();  
     ePoint.y = e.getY();   
    repaint();  
          
  }  
 
  public void mouseMoved(MouseEvent e) {    }  
 
  public void mouseEntered(MouseEvent e) {    }  
 
  public void mouseExited(MouseEvent e) {    }  
 
  public void mouseClicked(MouseEvent e) {    }  
 
  public void paint(Graphics g) {  
     /* Draw current line.*/
       super.paint(g);
	 
     g.drawLine(sPoint.x, sPoint.y, ePoint.x, ePoint.y); 
    
    
  }
  
   public static void main(String arg[])
  {
      DrawLinw line= new DrawLinw();
        JFrame f= new JFrame();
               f.setSize(new Dimension(300,300));
                 f.getContentPane().add(line);
               f.setVisible(true);
       }
}