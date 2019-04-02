package View;

import java.awt.Dimension;
import java.awt.EventQueue;


import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;




public class Tournament {

	public static void main(String[] args) {
		new Tournament();
	}
	/**
	 * It is a constructor.
	 */
	public Tournament(){
		new TourMode();

}
	/**
	 * It is a panel to show buttons
	 */
	public class TourMode{
		
		public TourMode(){
			showTournament();
			
	}
		}
	private void showTournament(){
		JPanel panel = new JPanel(new GridLayout(0, 6));
	
		JLabel M = new JLabel("M: ");
		panel.add(M);
		ButtonGroup group = new ButtonGroup();
		JRadioButton map1 = new JRadioButton("world");
		JRadioButton map2 = new JRadioButton("America");
		JRadioButton map3 = new JRadioButton("Tamril");
		JRadioButton map4 = new JRadioButton("Map4");
		JRadioButton map5 = new JRadioButton("Map5");
		group.add(map1);
		group.add(map2);
		group.add(map3);
		group.add(map4);
		group.add(map5);
		panel.add(map1);
		panel.add(map2);
		panel.add(map3);
		panel.add(map4);
		panel.add(map5);
		
		JLabel P = new JLabel("P: ");
		panel.add(P);
		ArrayList<JRadioButton> computers = new ArrayList<>();
		JRadioButton com1 = new JRadioButton("Aggresive");
		JRadioButton com2 = new JRadioButton("Benevolent");
		JRadioButton com3 = new JRadioButton("Random");
		JRadioButton com4 = new JRadioButton("Cheater");
		computers.add(com1);
		computers.add(com2);
		computers.add(com3);
		computers.add(com4);
		panel.add(com1);
		panel.add(com2);
		panel.add(com3);
		panel.add(com4);
		JLabel nothing = new JLabel();
		panel.add(nothing);
		
		JLabel G = new JLabel("G: ");
		panel.add(G);
		ButtonGroup gamegroup = new ButtonGroup();
		JRadioButton game1 = new JRadioButton("1");
		JRadioButton game2 = new JRadioButton("2");
		JRadioButton game3 = new JRadioButton("3");
		JRadioButton game4 = new JRadioButton("4");
		JRadioButton game5 = new JRadioButton("5");
		gamegroup.add(game1);
		gamegroup.add(game2);
		gamegroup.add(game2);
		gamegroup.add(game3);
		gamegroup.add(game4);
		gamegroup.add(game5);
		panel.add(game1);
		panel.add(game2);
		panel.add(game3);
		panel.add(game4);
		panel.add(game5);
		final JLabel D = new JLabel("D: ");
		panel.add(D);
		
		JSlider framesPerSecond = new JSlider(10, 50);
		framesPerSecond.setMinorTickSpacing(1);
		framesPerSecond.setPaintTicks(true);
		framesPerSecond.setPaintLabels(true);
		framesPerSecond.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		Font font = new Font("Serif", Font.ITALIC, 15);
		framesPerSecond.setFont(font);
		framesPerSecond.addChangeListener(new ChangeListener() {

			/**
			 * This is stateChanged function.
			 */
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				int fps = (int) source.getValue();
				D.setText("D: " + String.valueOf(fps));

			}
		});
		
		panel.add(framesPerSecond);
		int result =  JOptionPane.showConfirmDialog(null, panel, "Tournament Mode", JOptionPane.DEFAULT_OPTION);
		String map;
		ArrayList<String> playerList = new ArrayList<>();
		if (result == 0 ) {
			//select map
			for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();

				if (button.isSelected()) {
					map = button.getText();
					System.out.println("you choose " + button.getText() + " map");
				}
			}
			//select computer players
			for (int i = 0; i < computers.size(); i++) {
				if (computers.get(i).isSelected()) {
					playerList.add(computers.get(i).getText());
					System.out.println("you select computer player "+ computers.get(i).getText());
				}
			}
			//select how many times to play
			for (Enumeration<AbstractButton> buttons = gamegroup.getElements(); buttons.hasMoreElements();) {
				AbstractButton button = buttons.nextElement();

				if (button.isSelected()) {
					map = button.getText();
					System.out.println("you want to play " + button.getText() + " times for this map");
				}
			}
			
			//how many turns
			int turns = framesPerSecond.getValue();
			System.out.println(turns+" turns");
			
		}
		
	}
	//return correct map
	private void fileList() {
		
		File file = new File("mapfile/");
		File[] files = file.listFiles();
		ArrayList<String> listfile = new ArrayList<>();
		
		for (int i = 0; i < files.length; i++) {
			
		}
	}
	

}
