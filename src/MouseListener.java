import javax.swing.JFrame;


public class MouseListener {
	public static void main(String[] args) {
		JFrame window = new JFrame("MouseListener");
	//	window.getContentPane().addMouseListener(new MouseClickListner());
		Panel panel = new Panel();
		window.setContentPane(panel);
		panel.addMouseListener(new MouseClickListner(panel));
		window.setSize(500,500);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
	}

}
