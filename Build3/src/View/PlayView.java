package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.xml.crypto.dsig.keyinfo.PGPData;

import Model.Attack;
import Model.Continent;
import Model.Country;
import Model.IO;
import Model.InitializePhase;
import Model.Line;
import Model.Player;
import Strategy.Human;

/**
 * <h1>PlayView</h1> It is Play Game main View have three function
 * (reinforcement, attack, fortification)
 *
 * @author chenwei_song
 * @version 3.0
 * @since 2019-03-01
 */
public class PlayView extends JFrame implements Observer {

	private HashMap<String, Line> lineMap = new HashMap<>();
	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	public static String mappath;
	public static JLabel name = new JLabel();
	public static JLabel color;
	public static JLabel armies;
	public static String currentPhase;
	public static JButton phase = new JButton();
	public static boolean WIN = false;
	public ArrayList<JLabel> labellist = new ArrayList<>();
	BackEnd b;
	InitializePhase observable = new InitializePhase();

	DominationView dominationView;
	
	private static final long serialVersionUID = 1L;
	JFrame frame = new JFrame("Risk Game");

	/**
	 * It is observer override update method update countries, continents, players
	 * at the same time
	 */

	@Override
	public void update(Observable obs, Object x) {
		
		countries = ((InitializePhase) obs).getCountries();
		continents = ((InitializePhase) obs).getContinents();
		playerSet = ((InitializePhase) obs).getPlayerSet();
		
		
		updateLabel();
		try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		boolean isChange =  ((InitializePhase) obs).change;
		if (isChange) {
			//当前玩家不是human
			phase.setText("Reinforcement");
			String[] fullname = name.getText().split("_");
			playerSet.get(fullname[1]).reinforcement(null, "", observable, b);
		}
		
		

	}

	/**
	 * It is constructor of play view to upload a thread of JFrame show all buttons
	 * ,labels, texts.
	 */
	public PlayView() {
	//	b = new BackEnd();
	//	observable.addObserver(b);
		observable.addObserver(this);
	
		
		
		EventQueue.invokeLater(new Runnable() {

			/**
			 * This is a thread.
			 */
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//		PlayPane playPane = new PlayPane();
				frame.add( new PlayPane(), BorderLayout.WEST);
			
				observable.addObserver(dominationView);
				frame.add(dominationView, BorderLayout.EAST);
				observable.addObserver(dominationView);

				frame.setSize(1300, 650);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);

			}
		});

	}

	private void updateLabel() {
		for (int i = 0; i < labellist.size(); i++) {
			final JLabel label = labellist.get(i);

			
			// update country army number
			String[] old = label.getText().split(" ");
			if (!old[1].equals(String.valueOf(countries.get(label.getName()).getArmy()))) {
				
				final Timer timer = new Timer();
			    TimerTask task = new TimerTask() {
			        private int count = 12;
			  
			        @Override
			        public void run() {
			            this.count++;
			            label.setFont(new Font("Serif", Font.BOLD, count));
			            label.setForeground(Color.RED);
			           // System.out.println(count);
			            if (count == 18) {
			            	label.setFont(new Font("Serif", Font.BOLD, 12));
			            	 label.setForeground(Color.BLACK);
			                timer.cancel();
			            }
			        }
			    };
			    timer.schedule(task, 0,300);
			
			}
			String now = old[0] + " " + countries.get(label.getName()).getArmy();
			label.setText(now);
			
			// update country color
			ImageIcon imageIcon = (ImageIcon) label.getIcon();
			Image image = imageIcon.getImage();

			BufferedImage img = (BufferedImage) image;
			int width = img.getWidth();
			int height = img.getHeight();

			WritableRaster raster = img.getRaster();
			for (int xx = 0; xx < width; xx++) {
				for (int yy = 0; yy < height; yy++) {
					int[] pixels = raster.getPixel(xx, yy, (int[]) null);
					pixels[0] = countries.get(label.getName()).getColor().getRed();
					pixels[1] = countries.get(label.getName()).getColor().getGreen();
					pixels[2] = countries.get(label.getName()).getColor().getBlue();
					raster.setPixel(xx, yy, pixels);
				}

			}

		
		}
	}
	/**
	 * It is a JLayeredPane to add buttons, labels, texts. it can receive mouse
	 * listener component.
	 */
	public class PlayPane extends JLayeredPane {

		iconHandler ih = new iconHandler();

		public PlayPane() {
			observable.setCountries(countries);
			observable.setContinents(continents);
			observable.setPlayerSet(playerSet);
			b = new BackEnd(observable);
			observable.addObserver(b);
			dominationView = new DominationView(observable);
			observable.addObserver(dominationView);
			File image = new File("resource/hexagon.png");

			for (String key : countries.keySet()) {

				Point start = countries.get(key).getLocation();
				String continent = countries.get(key).getContinent() + " " + countries.get(key).getArmy();

				String countryList = countries.get(key).getCountryList();
				
				countries.get(key).setCountryList(countryList + " ");
				String[] link = countryList.split(" ");
				try {
					BufferedImage img = ImageIO.read(image);

					int width = img.getWidth();
					int height = img.getHeight();

					WritableRaster raster = img.getRaster();
					for (int xx = 0; xx < width; xx++) {
						for (int yy = 0; yy < height; yy++) {
							int[] pixels = raster.getPixel(xx, yy, (int[]) null);
							pixels[0] = countries.get(key).getColor().getRed();
							pixels[1] = countries.get(key).getColor().getGreen();
							pixels[2] = countries.get(key).getColor().getBlue();
							raster.setPixel(xx, yy, pixels);
						}

					}

					JLabel label = new JLabel(new ImageIcon(img));
					label.setSize(label.getPreferredSize());
					label.setLocation(start);
					label.setText(continent);
					label.setName(key);
					label.setHorizontalTextPosition(JLabel.CENTER);
					label.setVerticalTextPosition(JLabel.CENTER);
					label.addMouseListener(ih);
					label.addMouseMotionListener(ih);
					labellist.add(label);
					add(label);

				} catch (IOException e1) {
					e1.printStackTrace();
				}

				for (int i = 0; i < link.length; i++) {
					if (countries.containsKey(link[i])) {
						Point end = countries.get(link[i]).getLocation();
						lineMap.put(key + " " + link[i], new Line(start, end));
					}
				}
			}

			repaint();

			// create button country
//			phase = new JButton("start up phase");
//			currentPhase = "start up";
			phase.setName("phase");
			phase.setBackground(Color.green);
			phase.setBounds(400, 600, 200, 50);
			phase.addMouseListener(ih);
			add(phase);

			// player name color label
			JLabel player = new JLabel("Player: ");
			player.setBounds(1000, 20, 80, 25);
			player.setName("player");
			add(player);
		//	name = new JLabel();
		//	String fullname = playerSet.get("1").getPlayerName()+"_"+"1";
		//	name.setText(fullname);
			String[] fullname = name.getText().split("_");
			String lastname = fullname[1];
			name.setName("player");
			name.setBounds(1060, 20, 80, 25);
			add(name);
			color = new JLabel("");
			color.setBounds(1130, 20, 25, 25);
			color.setBackground(playerSet.get(lastname).getColor());
			color.setOpaque(true);
			add(color);

			// receive armies number
			JLabel army = new JLabel();
			army.setText("Army: ");
			army.setBounds(1000, 60, 80, 25);
			add(army);
			String n = String.valueOf(playerSet.get(lastname).getArmy());
			armies = new JLabel(n);
			armies.setName("armies");
			armies.setBounds(1100, 70, 80, 25);
			add(armies);
			
			
			JButton save = new JButton("save");
			save.setBounds(1000, 500, 80, 25);
			add(save);
			save.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String fileName = JOptionPane.showInputDialog("save map?");
					if (fileName != null) {
						String[] currentplayer = name.getText().split("_");
					//	observable.saveGame(fileName, currentPlayer, n);
						observable.saveGame(fileName,mappath,currentplayer[1] , currentPhase);
						System.out.println(fileName);
						JOptionPane.showMessageDialog(null, "save map successful");

						frame.dispose();
						new StartGame();
					} 
				
				}
			});
			//判断是否为Human
			if (!playerSet.get(lastname).getPlayerName().equals("Human")) {
				startupPlayer("1");
			}

		}

		/**
		 * This is a paint function.
		 * @param g is object of swing drawing tool it can draw any shapes in this project, it is used to draw line between countries.
		 */
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			for (String key : lineMap.keySet()) {
				Point s = lineMap.get(key).getStart();
				Point e = lineMap.get(key).getEnd();
				g.drawLine(s.x + 50, s.y + 50, e.x + 50, e.y + 50);
			}

		}

		/**
		 * It is an override method of JFrame it defines the size of window.
		 * 
		 * @return The size of JFrame.
		 */
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(1200, 650);
		}
		
		
		public void startupPlayer(String player) {
			System.out.println(name.getText()+" start up");
			//start up phase
			String c = randomCountry(player);
			observable.Startup(player, c);
			//update player
			String nextP = b.nextplayer(player);
			if (nextP == "") {
				// get into reinforcement phase
				if (playerSet.get("1").getPlayerName().equals("Human")) {
					//next player 为 human
					System.out.println("enter reinforcement phase");
					phase.setText("Reinforcement");
					currentPhase = "Reinforcement";
					observable.Reinforcement("1");
					String lastname = playerSet.get("1").getPlayerName()+"_"+"1";
					name.setText(lastname);
					armies.setText(String.valueOf(playerSet.get("1").getArmy()));
					color.setBackground(playerSet.get("1").getColor());
					JOptionPane.showMessageDialog(null, "enter reinforcement phase");
				}
				else {
					//next player 非 human
				//	reinforcement("1");
					
					System.out.println("enter reinforcement phase");
					phase.setText("Reinforcement");
					currentPhase = "Reinforcement";
					observable.Reinforcement("1");
					String lastname = playerSet.get("1").getPlayerName()+"_"+"1";
					name.setText(lastname);
					armies.setText(String.valueOf(playerSet.get("1").getArmy()));
					color.setBackground(playerSet.get("1").getColor());
					playerSet.get("1").reinforcement(name,"", observable, b);
				}
				
				
			}
			else if (!playerSet.get(nextP).getPlayerName().equals("Human")) {
				String fullname = playerSet.get(nextP).getPlayerName()+"_"+nextP;
				name.setText(fullname);
				armies.setText(String.valueOf(playerSet.get(nextP).getArmy()));
				color.setBackground(playerSet.get(nextP).getColor());
				startupPlayer(nextP);
			}
			else {
				String fullname = playerSet.get(nextP).getPlayerName()+"_"+nextP;
				name.setText(fullname);
				armies.setText(String.valueOf(playerSet.get(nextP).getArmy()));
				color.setBackground(playerSet.get(nextP).getColor());
			}
			
			
}
		
		
		private String randomCountry(String player) {
			LinkedList<Country> list = playerSet.get(player).getCountryList();
			int n = (int)(0+Math.random()*list.size());
			int cou = list.get(n).getName();
			return String.valueOf(cou);
			
		}
		
		/**
		 *
		 * Check whether current player click right country or not.
		 * 
		 * @param cplayer  Who wants to operate this country
		 * @param ccountry Which country would be operated
		 * @return true if the player owns this country otherwise is false.
		 */
		public boolean rightcountry(String cplayer, String ccountry) {
			boolean match = false;
			LinkedList<Country> findCountries = playerSet.get(cplayer).getCountryList();
			for (Iterator<Country> iterator = findCountries.iterator(); iterator.hasNext();) {
				String s = String.valueOf(iterator.next().getName());
				if (ccountry.equals(s)) {
					match = true;
				}

			}
			return match;
		}

		/**
		 * It is an mouse adapter class to process the mouse operation we only use mouse
		 * click.
		 */
		public class iconHandler extends MouseAdapter {
			JLabel from;
			boolean start = true;

			/**
			 * It is a mouse clicked method.
			 * 
			 * @param e The component of mouse click.
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
			

				String click = e.getComponent().getName();
				if (click.equals("phase")) {

					JButton phase = (JButton) e.getComponent();
					if (phase.getText().equals("Reinforcement")) {
						System.out.println("Reinforcement");
						String[] fullname = name.getText().split("_");
						armies.setText(String.valueOf(playerSet.get(fullname[1]).getArmy()));

					} else if (phase.getText().equals("Attack")) {
						start = true;
						from = null;
						JOptionPane.showMessageDialog(null, "enter Fortification phase");
						phase.setText("Fortification");
						currentPhase = "Fortification";
					} else if (phase.getText().equals("Fortification")) {
						
						// earn card
						if (WIN) {
							String[] fullname = name.getText().split("_");
							observable.earnCard(fullname[1]);
						}
						WIN = false;
						System.out.println("Fortification, enter next phase Reinforcement, Change Player");
						String[] fullname = name.getText().split("_");
						String nextP = b.findnext(fullname[1]);

						phase.setText("Reinforcement");
						currentPhase = "Reinforcement";
						
						
						String lastname = playerSet.get(nextP).getPlayerName()+"_"+nextP;
						name.setText(lastname);
						color.setBackground(playerSet.get(nextP).getColor());
						
						if (playerSet.get(nextP).getCardList().size() != 0 && playerSet.get(nextP).getPlayerName().equals("Human")) {
							observable.Reinforcement(nextP);
							observable.cardArmy(nextP, playerSet.get(nextP).getCardList(), false);
							armies.setText(
									"<html><body><p align=\"center\">calculating...<br/>press&nbsp;reinforcement</p></body></html>");

						} else if(playerSet.get(nextP).getCardList().size() == 0 &&playerSet.get(nextP).getPlayerName().equals("Human")){
							observable.Reinforcement(nextP);
							armies.setText(String.valueOf(playerSet.get(nextP).getArmy()));
						}
						// next player 不是 human
						else if (!playerSet.get(nextP).getPlayerName().equals("Human")) {
							observable.nextTurn(1);
						}
						

					}

				}
				
////////////////////////////////////////////////				
				else {
					if (currentPhase.equals("start up")) {

						JLabel c = (JLabel) e.getComponent();
						
						// check whether the player click own country
						String[] fullname = name.getText().split("_");
						boolean match = rightcountry(fullname[1], c.getName());

						if (match) {
							observable.Startup(fullname[1], click);
							// change player
							String nextP = b.nextplayer(fullname[1]);
							if (nextP == "") {
								
								// get into reinforcement phase
								JOptionPane.showMessageDialog(null, "enter reinforcement phase");
								System.out.println("enter reinforcement phase");
								phase.setText("Reinforcement");
								currentPhase = "Reinforcement";
								observable.Reinforcement("1");
								String lastname = playerSet.get("1").getPlayerName()+"_"+"1";
								name.setText(lastname);
								armies.setText(String.valueOf(playerSet.get("1").getArmy()));
								color.setBackground(playerSet.get("1").getColor());

							} else if (!playerSet.get(nextP).getPlayerName().equals("Human")) {
								//非 human 状态
								String lastname = playerSet.get(nextP).getPlayerName()+"_"+nextP;
								name.setText(lastname);
								armies.setText(String.valueOf(playerSet.get(nextP).getArmy()));
								color.setBackground(playerSet.get(nextP).getColor());
								startupPlayer(nextP);
							}
							else {
								//human 状态
								String lastname = playerSet.get(nextP).getPlayerName()+"_"+nextP;
								name.setText(lastname);
								armies.setText(String.valueOf(playerSet.get(nextP).getArmy()));
								color.setBackground(playerSet.get(nextP).getColor());
							}

						}

						else {
							JOptionPane.showMessageDialog(null, "please select your own countries");
						}

					}
					
					/////////////////////////////////////////
					else if (currentPhase.equals("Reinforcement")) {

						JLabel c = (JLabel) e.getComponent();
						String[] fullname = name.getText().split("_");
					//	System.out.println(playerSet.get("1").getStrategy());
						playerSet.get(fullname[1]).reinforcement(c, click, observable, b);
					//	human.reinforcement(c,click, observable ,b);
					
				
					}
					//////////////////////////////////
					else if (currentPhase.equals("Attack")) {
						System.out.println(b.countries.size());
						JLabel c = (JLabel) e.getComponent();
						String[] fullname = name.getText().split("_");
						boolean match = rightcountry(fullname[1], c.getName());
						boolean isOne = b.isOne(c.getName());
						if (start && match && isOne) {
							from = c;
							start = false;
							from.setBorder(new LineBorder(Color.ORANGE));
							System.out.println("you choose" + from.getText() + " as attack country");
						} else {
							if (start && !isOne) {
								JOptionPane.showMessageDialog(null, "this country cannot attack others");
							}
							if (start && !match) {
								JOptionPane.showMessageDialog(null, "please select your own countries");
							}
							if (!start && match) {
								JOptionPane.showMessageDialog(null,
										"please select country that belongs to other players");
								from.setBorder(null);
								from = null;
								start = true;
							}

						}

						if (!start && !match) {
							boolean isNeighbour = b.Isneighbour(from.getName(), c.getName());
							String[] lastname = name.getText().split("_");
							boolean self = rightcountry(lastname[1], c.getName());
							if (isNeighbour && !self) {
								
								// calculate disc number
								c.setBorder(new LineBorder(Color.ORANGE));
								String[] firstname = name.getText().split("_");
								playerSet.get(firstname[1]).attack(from, c, observable, b);
								
								from = null;
								start = true;

							} else {
								from.setBorder(null);
								from = null;
								start = true;
								if (!isNeighbour) {
									JOptionPane.showMessageDialog(null, "please select your neighbour countries");
								}
								if (self) {
									JOptionPane.showMessageDialog(null,
											"please select country that belongs to other players");
								}
							}
						}
						if (Attack.isWIN()) {
							String winner = "";
							for (String key : playerSet.keySet()) {
								winner = key;
							}
							JOptionPane.showMessageDialog(null,
									"Congradulation!!!!player " + winner + " is winnner!!!");
							frame.dispose();
							new StartGame();

						}
						String[] lastname = name.getText().split("_");
						boolean canAttack = b.canAttack(lastname[1]);
						if (!canAttack && !Attack.isWIN()) {// cannot attack enter fortification phase
							JOptionPane.showMessageDialog(null, "you cannot attack,enter fortification phase");
							System.out.println("enter fortification phase");
							phase.setText("Fortification");
							currentPhase = "Fortification";
						}
						Attack.WIN = false;

					}

					////////////////////////////////////////
					else if (currentPhase.equals("Fortification")) {
						JLabel c = (JLabel) e.getComponent();
						String[] lastname = name.getText().split("_");
						boolean match = rightcountry(lastname[1], c.getName());
						if (match) {
							if (start) {
								from = c;
								from.setBorder(new LineBorder(Color.ORANGE));
								start = false;

								System.out.println("move army from " + from.getText());
							} else {
								c.setBorder(new LineBorder(Color.ORANGE));
								String to = c.getName();
								
							//	human.fortification(from, c, to, observable, b);
								String[] firstname = name.getText().split("_");
								
								playerSet.get(firstname[1]).fortification(from, c, to, observable, b);
								from.setBorder(null);
								c.setBorder(null);
								start = true;
								from = null;

							}

						} else {
							JOptionPane.showMessageDialog(null, "please select your own countries");
							if (from != null) {
								from.setBorder(null);
							}
						//	from.setBorder(null);
							start = true;
							from = null;
						}

					}
				}

			}



		}

	}

}
