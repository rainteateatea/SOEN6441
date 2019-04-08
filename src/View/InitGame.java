package View;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Controller.InitGame_controller;
import Model.Checkmap;
import Model.Continent;
import Model.Country;
import Model.IO;
import Model.Message;
import View.SelectMap.selectPane;

/**
 * <h1>InitGame</h1> 
 * Input the player number and select map.
 *
 * @author chenwei_song
 * @version 3.0
 * @since 2019-03-01
 */
public class InitGame extends JFrame {

	int n;
	JFrame frame = new JFrame();

	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();

	/**
	 * It is a constructor that creates a thread of JFrame.
	 */
//	public static void main(String[] args) {
//		new InitGame();
//	}
	public InitGame() {

		EventQueue.invokeLater(new Runnable() {
			
			/**
			 * It is a thread.
			 */
			@Override
			public void run() {
				 try {
	                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
	                    ex.printStackTrace();
	                }
	                frame.add(new initPane());
	                frame.pack();
	                frame.setLocationRelativeTo(null);
	                frame.setVisible(true);
	             
			}
		});	


		
	}

	/**
	 * It is a initPane to add buttons on the JFrame.
	 *
	 */
	public class initPane extends JLayeredPane {
		
		/**
		 * It is a constructor to add buttons on the panel.
		 */
		public initPane() {

			JLabel palyernum = new JLabel("player :");
			palyernum.setBounds(20, 100, 100, 30);
			add(palyernum);
			@SuppressWarnings("rawtypes")
			final ArrayList<JComboBox> players = new ArrayList<>();
			JComboBox<String> p1=new JComboBox<String>();
			p1.addItem("Human");
			p1.addItem("Aggressive");
			p1.addItem("Benevolent");
			p1.addItem("Random");
			p1.addItem("Cheater");
			p1.setBounds(90, 100, 100, 30);
			add(p1);
			players.add(p1);
			
			JComboBox<String> p2=new JComboBox<String>();
			p2.addItem("Human");
			p2.addItem("Aggressive");
			p2.addItem("Benevolent");
			p2.addItem("Random");
			p2.addItem("Cheater");
			p2.setBounds(190, 100, 100, 30);
			add(p2);
			players.add(p2);
			
			JComboBox<String> p3=new JComboBox<String>();
			p3.addItem("null");
			p3.addItem("Human");
			p3.addItem("Aggressive");
			p3.addItem("Benevolent");
			p3.addItem("Random");
			p3.addItem("Cheater");
			p3.setBounds(290, 100, 100, 30);
			add(p3);
			players.add(p3);
			
			JComboBox<String> p4=new JComboBox<String>();
			p4.addItem("null");
			p4.addItem("Human");
			p4.addItem("Aggressive");
			p4.addItem("Benevolent");
			p4.addItem("Random");
			p4.addItem("Cheater");
			p4.setBounds(390, 100, 100, 30);
			add(p4);
			players.add(p4);
			
			JComboBox<String> p5=new JComboBox<String>();
			p5.addItem("null");
			p5.addItem("Human");
			p5.addItem("Aggressive");
			p5.addItem("Benevolent");
			p5.addItem("Random");
			p5.addItem("Cheater");
			p5.setBounds(490, 100, 100, 30);
			add(p5);
			players.add(p5);
			

			JButton select = new JButton("Select a map");
			add(select);
			select.setBounds(200, 200, 200, 100);

			select.addActionListener(new ActionListener() {

				/**
				 * This is an actionPerformed function.
				 */
				@Override
				public void actionPerformed(ActionEvent e) {

					JFileChooser jfc = new JFileChooser();
					jfc.setCurrentDirectory(new File("mapfile/"));
					jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

					int returnValue = jfc.showOpenDialog(null);

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						System.out.println("File name:" + jfc.getSelectedFile().getName());
						String filename = "mapfile/" + jfc.getSelectedFile().getName();

						IO io = new IO();
						io.readFile(filename);
						boolean isMap = Message.isSuccess();
						if (isMap) {
							Checkmap checkmap = new Checkmap(io.getCountries(), io.getContinents());
							checkmap.judge();
							boolean result = Message.isSuccess();
							if (!result) {
								String error = Message.getMessage();
								JOptionPane.showMessageDialog(null, error);
							} else {

								frame.dispose();

								ArrayList<String> playlist = new ArrayList<>();
								for (int i = 0; i < players.size(); i++) {
									//System.out.println(players.get(i).getSelectedItem());
									String strategy = (String) players.get(i).getSelectedItem();
									if (!strategy.equals("null")) {
										System.out.println(strategy);
										playlist.add(strategy);
									}
								}
								InitGame_controller controller = new InitGame_controller();
								
								controller.receive(playlist.size(),playlist, filename);
							}

						} else {

							String error = Message.getMessage();
							JOptionPane.showMessageDialog(null, error);
						}

					}

				}
			});
			
			
			
			JButton conti = new JButton("Continue Game");
			conti.setBounds(200, 400, 200, 100);
			add(conti);
			conti.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {

					JFileChooser jfc = new JFileChooser();
					jfc.setCurrentDirectory(new File("LoadGame/"));
					jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

					int returnValue = jfc.showOpenDialog(null);

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						System.out.println("File name:" + jfc.getSelectedFile().getName());
						String filename = "LoadGame/" + jfc.getSelectedFile().getName();

						
						
						
						

								frame.dispose();
								InitGame_controller controller = new InitGame_controller();
								
								controller.continueGame(filename);
							}


					

				}
			});
			

		}

		/**
		 * It is an override method in JPanel it defines the window size.
		 * 
		 * @return window size the data type is dimension.
		 */
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(650, 650);
		}
	}

}
