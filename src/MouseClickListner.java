import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MouseClickListner extends MouseAdapter {
	private Panel panel ;
	
	public MouseClickListner(Panel panel) {
		super();
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	//	super.mouseClicked(arg0);
		panel.addCircle(new Circle(e.getX(), e.getY(), 24, Color.blue));
		System.out.println(e.getX()+" "+e.getY());
	}

}
