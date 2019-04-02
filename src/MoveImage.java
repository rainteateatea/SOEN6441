import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;




import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

public class MoveImage {

    public static void main(String[] args) {
        new MoveImage();
    }

    public MoveImage() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                }

                JFrame frame = new JFrame("Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new AnimationPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

        });
    }

    public class AnimationPane extends JPanel {

        private BufferedImage boat;
        private int xPos = 0;
        private int direction = 1;

        public AnimationPane() {
            try {
                boat = ImageIO.read(new File(System.getProperty("user.dir")+"\\resource\\tower.png"));
               Timer timer = new Timer(40, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	
                        xPos += direction;
                        if (xPos + boat.getWidth() > getWidth()) {
                            xPos = getWidth() - boat.getWidth();
                            direction *= -1;
                        } else if (xPos < 0) {
                            xPos = 0;
                            direction *= -1;
                        }
                        repaint();
                    }

                });
                timer.setRepeats(true);
                timer.setCoalesce(true);
                timer.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return boat == null ? super.getPreferredSize() : new Dimension(boat.getWidth() * 4, boat.getHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int y = getHeight() - boat.getHeight();
            g.drawImage(boat, xPos, y, this);

        }

    }

}