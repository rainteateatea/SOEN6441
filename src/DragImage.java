import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class DragImage {
	   JFrame frame = new JFrame("Testing");

    public static void main(String[] args) {
        new DragImage();
    }

    public DragImage() {
    	
    	
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

        //        JFrame frame = new JFrame("Testing");
             
//                JButton b = new JButton("click");
//                b.setBounds(50, 150, 100, 30);
//                frame.add(b);
//               
//                b.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						JOptionPane.showMessageDialog(null, "the button was pressed");
//					
//						
//						
//					}
//				});
                
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
              
            }
        });
    }
    

    public class TestPane extends JLayeredPane {

        public TestPane() {
            File[] images = new File(System.getProperty("user.dir")+"\\resource").listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String name = pathname.getName().toLowerCase();
                    return name.endsWith(".png") || 
                                    name.endsWith(".jpg") || 
                                    name.endsWith(".bmp") ||
                                    name.endsWith(".gif");
                }
            });

            int x = 0;
            int y = 0;
            
            MouseHandler mh  = new MouseHandler();
            
            for (File imgFile : images) {

                try {
                    BufferedImage img = ImageIO.read(imgFile);
                    JLabel label = new JLabel(new ImageIcon(img));
                  
                   
                    
                //   JLabel label = new JLabel("pppp", new ImageIcon(img), JLabel.CENTER);
                  
                    label.setSize(label.getPreferredSize());
                    label.setLocation(x, y);
                    label.setText(imgFile.getName());
                    label.setName("343");
                 
                    label.setHorizontalTextPosition(JLabel.CENTER);
                    label.setVerticalTextPosition(JLabel.CENTER);
              
                
                    label.addMouseListener(mh);
                    label.addMouseMotionListener(mh);
                    add(label);
                    x += 100;
                    y += 100;
                    
                    
                    
                } catch (IOException exp) {
                    exp.printStackTrace();
                }

            }
   
        

         

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 800);
        }

        public class MouseHandler extends MouseAdapter {

            private Point offset;
          
            

			@Override
            public void mousePressed(MouseEvent e) {
            	
                JLabel label = (JLabel) e.getComponent();
                frame.remove(label);
                remove(label);
                revalidate();
                repaint();
//       label.setIcon(null);
//       label.revalidate();
                System.out.println( label.getName());
                ImageIcon imageIcon = (ImageIcon) label.getIcon();
                Image image = imageIcon.getImage();
                
                BufferedImage img = (BufferedImage) image;
              int width = img.getWidth();
              int height = img.getHeight();
              
              WritableRaster raster = img.getRaster();
              for (int xx = 0; xx < width; xx++) {
					for (int yy = 0; yy < height; yy++) {
						 int[] pixels = raster.getPixel(xx, yy, (int[]) null);
			                pixels[0] = 23;
			                pixels[1] = 211;
			                pixels[2] = 193;
			                raster.setPixel(xx, yy, pixels);
					}
					
				}
              
                
                moveToFront(label);
                offset = e.getPoint();
                
                
                
            
             
            }
			
		

           @Override
            public void mouseDragged(MouseEvent e) {
            //	System.out.println(e.getSource());
                int x = e.getPoint().x - offset.x;
                int y = e.getPoint().y - offset.y;
                Component component = e.getComponent();
                Point location = component.getLocation();
                location.x += x;
                location.y += y;
                component.setLocation(location);
                
            }
           
            
           

        }

    }

}