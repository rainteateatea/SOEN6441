import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JPanel;


public class Panel extends JPanel {

	private LinkedList<Circle> circles = new LinkedList<Circle>();
	
	public void addCircle(Circle circle) {
		circles.add(circle);
		this.repaint();
		
	}
	
	@Override
	public void paint(Graphics g) {
		for(Circle c: circles){
			c.draw(g);
		}
	}
}
