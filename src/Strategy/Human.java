package Strategy;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Player;
import View.BackEnd;
import View.PlayView;

public class Human implements BehaviorStrategy{
	
	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	

	PlayView playView;

	/**
	 * This method executes reinforcement.
	 *
	 * @param c     The country that is chosen.
	 * @param click The country name.
	 * @param observeable The InitializePhase class.
	 * @param b The BackEnd class.
	 */
	@Override
	public void reinforcemnet(JLabel c, String click,InitializePhase observable, BackEnd b) {
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		String[] fullname =playView.name.getText().split("_");
		boolean match = rightcountry(fullname[1], c.getName());

				if (match) {
					observable.Startup(fullname[1], click);
					
					// update country army number
//					String[] old = c.getText().split(" ");
//					String now = old[0] + " " + countries.get(c.getName()).getArmy();
//					c.setText(now);
					playView.armies.setText(String.valueOf(playerSet.get(fullname[1]).getArmy()));
					if (playerSet.get(fullname[1]).getArmy() == 0) {
						boolean canAttack = b.canAttack(fullname[1]);
						if (canAttack) {
							
							// enter attack phase
							JOptionPane.showMessageDialog(null, "enter attack phase");
							System.out.println("enter Attack phase");
							playView.currentPhase = "Attack";
							playView.phase.setText("Attack");
						} else { 
							
							// cannot attack enter fortification phase
							JOptionPane.showMessageDialog(null, "you cannot attack,enter fortification phase");
							System.out.println("enter fortification phase");
							playView.phase.setText("Fortification");
							playView.currentPhase = "Fortification";
						}

					}

				}

				else {
					JOptionPane.showMessageDialog(null, "please select your own countries");
				}
		
	}

	/**
	 * This method check whether current player click right country or not.
	 *
	 * @param attak  Who wants to operate this country.
	 * @param defend Which country would be operated.
	 */
	@Override
	public void attack(JLabel attak, JLabel defend, 
			InitializePhase observable, BackEnd b) {

		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		
		// select mode
		String mode = b.chooseMode();
		String defender = b.findPlayer(defend.getName());
		String[] atcoun = attak.getText().split(" ");
		String[] decoun = defend.getText().split(" ");
		if (mode != "") {
			if (mode.equals("All_Out")) {
				String canTransfer = observable.attackPhase(attak.getName(), defend.getName(), mode, 0, 0);
				Transfer(canTransfer, attak, defend , observable , b);
			} else {
				
				// one - time
				String[] fullname = playView.name.getText().split("_");
				String dicses = b.dicsnumber(fullname[1], atcoun[1], "at", "");
				if (dicses != "") {
					
					// defender choose disc
					String de = b.dicsnumber(defender, decoun[1], "de", dicses);
					if (de != "") {
						
						// one_time
						String canTransfer = observable.attackPhase(attak.getName(), defend.getName(), mode,
								Integer.valueOf(dicses), Integer.valueOf(de));
						Transfer(canTransfer, attak, defend, observable , b);
					}

				}

			}
		}

	
		
	}

	
	/**
	 * This method updates attack and defender JLabel.
	 *
	 * @param record A String shows transfer army information.
	 * @param att    A JLabel shows attack country army.
	 * @param def    A JLabel shows defender country army.
	 */
	public void Transfer(String record, JLabel att, JLabel def,InitializePhase observable, BackEnd b) {
		
		String[] readrecord = record.split(" ");
		if (readrecord[1].equals("0")) {//update countries information
			att.setBorder(null);
			def.setBorder(null);
			String[] fullname = playView.name.getText().split("_");
			if (readrecord[0].equals(fullname[1])) {
				JOptionPane.showMessageDialog(null, "attacker Player"+ playView.name.getText()+" win");
				if (!readrecord[2].equals("0")) {
					playView.WIN = true;
				}
				
			}
			else if (readrecord[0].equals("-1")) {
				
				JOptionPane.showMessageDialog(null, "This is a draw.");
			}
			else {
				String defender = b.findPlayer(def.getName());
				JOptionPane.showMessageDialog(null, "defender Player"+ defender+" win");
			}
			
		}
		else {
			playView.WIN = true;
		
			int move = b.moveArmies(Integer.valueOf(readrecord[1]), Integer.valueOf(readrecord[2]));
			observable.Fortification(att.getName(), def.getName(), move);
			att.setBorder(null);
			def.setBorder(null);
		}
	}
	/**
	 * This method implements fortification.
	 *
	 * @param from A label that is a start country.
	 * @param c    A label that is a end country.
	 * @param to   A string that is name of end country.
	 */
	@Override
	public void fortification(JLabel from, JLabel c, String to,InitializePhase observable, BackEnd b) {

		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		String[] fullname = playView.name.getText().split("_");
		boolean canTransfer = observable.canTransfer(fullname[1], from.getName(), to);

		// input how many armies you want to move
		String question = "how many armies you want to move from" + from.getName() + "to " + to;
		String str = JOptionPane.showInputDialog(null, question, "input armies number",
				JOptionPane.PLAIN_MESSAGE);

		// whether country armies number is zero<= 0
		boolean iszero = b.Iszero(from.getName(), Integer.valueOf(str));

		if (!canTransfer) {
			JOptionPane.showMessageDialog(null, "no path can transfer your armies");
		}
		if (!iszero) {
			JOptionPane.showMessageDialog(null, "too many armies to move ");
		}
		if (canTransfer && iszero) {

			observable.Fortification(from.getName(), to, Integer.valueOf(str));
			
			// occupy a territory then obtain a card
			if (playView.WIN) {
				observable.earnCard(fullname[1]);
			}
			playView.WIN = false;
			
			// fortification only one time enter reinforcement
			playView.currentPhase = "Reinforcement";
			playView.phase.setText("Reinforcement");

			// find next player
			String nextP = b.findnext(fullname[1]);
			System.out.println("1");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("2");
			// update next player armies
	//		System.out.println(playerSet.get(nextP).getArmy());
			
			
			// change player
			String playername = playerSet.get(nextP).getPlayerName()+"_"+nextP;
			playView.name.setText(playername);

			playView.color.setBackground(playerSet.get(nextP).getColor());

			//next player Ϊ Human ���� card army ��Ϊ0
			if (playerSet.get(nextP).getCardList().size() != 0 && playerSet.get(nextP).getPlayerName().equals("Human")) {
				observable.Reinforcement(nextP);
				observable.cardArmy(nextP, playerSet.get(nextP).getCardList(), false);
				playView.armies.setText(
						"<html><body><p align=\"center\">calculating...<br/>press&nbsp;reinforcement</p></body></html>");

			}
			//next player Ϊ Human ���� card army Ϊ0
			else if(playerSet.get(nextP).getCardList().size() == 0 &&playerSet.get(nextP).getPlayerName().equals("Human")){
				observable.Reinforcement(nextP);
				playView.armies.setText(String.valueOf(playerSet.get(nextP).getArmy()));
			}
			// next player ���� human
			else if (!playerSet.get(nextP).getPlayerName().equals("Human")) {
				System.out.println("next "+playername);
				playView.currentPhase = "Reinforcement";
				playView.phase.setText("Reinforcement");
				observable.nextTurn(1);
			}

		}

	
		
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
	//	playerSet = observable.getPlayerSet();
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

}