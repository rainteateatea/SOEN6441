package Strategy;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Player;
import View.BackEnd;


public class TourAggressive implements BehaviorStrategy{
	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	@Override
	public void reinforcemnet(JLabel c, String click, InitializePhase observable, BackEnd b) {
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		String player = click;
		String fullname = playerSet.get(player).getPlayerName()+"_"+player;
		System.out.println(fullname+" enter Reinforcement phase");
		observable.Reinforcement(player);
		String strong = Strongest(player);
		while (playerSet.get(player).getArmy() != 0) {
			observable.Startup(player, strong);
		}
		if ((playerSet.get(player).getArmy() == 0)) {
			boolean canAttack = b.canAttack(player);
			if (canAttack) {

				// enter attack phase
				System.out.println( fullname+" enter Aggressive Attack phase");
				JLabel p = new JLabel();
				p.setText(player);
				p.setName(strong);
				attack(p, null, observable, b);

			} else if(playerSet.size()!=1){

				// cannot attack enter fortification phases
				System.out.println( fullname+"enter Aggressive fortification phase");
				fortification(null, null, null, observable, b);
			}
		}
	}

	@Override
	public void attack(JLabel playcountry, JLabel to, InitializePhase observable, BackEnd b) {
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		boolean win = false;
		String player = playcountry.getText();
		String fullname = playerSet.get(player).getPlayerName()+"_"+player;
		String atcountry = playcountry.getName();
		String[] defcountry = countries.get(atcountry).getCountryList().split(" ");
		for (int i = 0; i < defcountry.length; i++) {
			String defender = b.findPlayer(defcountry[i]);
			if (countries.get(atcountry).getArmy()== 1) {
				System.out.println("aggressive cannot attack");
				break;
			}
			//two different players
			if (!player.equals(defender)) {
				System.out.println("Aggressive_"+player+"uses country"+atcountry+" to attack country"+defcountry[i]);
				String canTransfer = observable.attackPhase(atcountry, defcountry[i], "All_Out", 0, 0);
				String record[] = canTransfer.split(" ");
				if (record[0].equals(player)) {//winner is attacker
					System.out.println("aggressive player win");

					win = true;
					if (!record[1].equals("0")) {
						System.out.println("Aggressive_"+player+"move"+ record[1]);
						observable.Fortification(atcountry, defcountry[i], Integer.valueOf(record[1]));
					}
					
				}
			}
			
		}
		if(win){
			observable.earnCard(player);
		}
		
		

//		use strongest country to attack until it cannot attack anymore
		playerSet = observable.getPlayerSet();
		if (playerSet.size() != 1) {
			if (observable.getDturns() == observable.D) {
				System.out.println("it is a draw");
				observable.refreshgame("draw");
			}
			else {
				
				System.out.println( fullname+" enter Aggressive fortification phase");
				fortification(null, null, player, observable, b);
			}
		
		}
		else {
			System.out.println("Congradulation!!!!player " +  fullname + " is winnner!!!");
			observable.refreshgame(playerSet.get(player).getPlayerName());
		}
		
		
	}

	@Override
	public void fortification(JLabel from, JLabel c, String to, InitializePhase observable, BackEnd b) {
		
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
	//	Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);
	//	String[] fullname = playView.name.getText().split("_");
		String player = to;
		LinkedList<Country> list = new LinkedList<Country>(playerSet.get(player).getCountryList());
		Collections.sort(list);

		if (list.peekLast().getArmy() == 1) {
			System.out.println("Aggressive_" + player + "has no country can transfer armies.");

		} else {

			while (list.size() != 0) {
				Country strongCountry = list.pollLast();
				String[] result = canTransfer(strongCountry, list, observable, player).split(" ");
				if ( result[0] == "true") {
					String distination = String.valueOf(strongCountry.getName());		
					String start = result[1];
					int armies = countries.get(start).getArmy()-1;
					observable.Fortification(start, distination, armies);
					System.out.println("Aggressive_" +player+"fortification from " + start +" to "+distination+ " move " + armies );
					break;
 				}
			}
		}
		
		// fortification only one time enter reinforcement
		
		playerSet = observable.getPlayerSet();
		String nextP = findnext(player,observable);
		// change player
		String playername = playerSet.get(nextP).getPlayerName()+"_"+nextP;
		
		
		
		// next player is not human
	
		playerSet.get(nextP).reinforcement(null, nextP, observable, b);
	}
	
	public String findnext(String current,InitializePhase observable) {

		int max = maxplayer();
		String next = String.valueOf(Integer.valueOf(current) + 1);
		if (Integer.valueOf(current) == max) {
			next = "1";
			
				observable.addturn();
		
		}
		if (playerSet.containsKey(next)) {
			return next;
		} else {
			return findnext(next,observable);
		}
	}
	/**
	 * This method finds the max number of player.
	 * 
	 * @return Player name.
	 */
	public int maxplayer() {
		int max = 0;
		for (String key : playerSet.keySet()) {
			int temp = Integer.valueOf(key);
			if (temp > max) {
				max = temp;
			}
		}
		return max;
	}
	private String Strongest(String player) {
		LinkedList<Country> candidates = playerSet.get(player).getCountryList();
		String strongest = "";
		int max = 0;
		int neightbours = 0;

		for (int i = 0; i < candidates.size(); i++) {
			int temp = candidates.get(i).getArmy();
			int neigtharmy = neighbourarmy(candidates.get(i).getName());
			if (temp >max) {
				max = temp;
				neightbours = neigtharmy;
				strongest = String.valueOf(candidates.get(i).getName());
			}
			else if (temp == max && neightbours >neigtharmy ) {
				max = temp;
				neightbours = neigtharmy;
				strongest = String.valueOf(candidates.get(i).getName());
			}
		}
		return strongest;
	}
	private int neighbourarmy(int country) {
		int armies = 0;
		String cc = String.valueOf(country);
		Color ccColor = countries.get(cc).getColor();
		String[] countlist = countries.get(cc).getCountryList().split(" ");
		for (int i = 0; i < countlist.length; i++) {
			if (ccColor != countries.get(countlist[i]).getColor()) {
				armies = armies+ countries.get(countlist[i]).getArmy();
			}
		}
		return armies;
	}
	
	private String canTransfer(Country country, LinkedList<Country> linkedList, InitializePhase observable,
			String player) {
		String name = String.valueOf(country.getName());
		LinkedList<Country> list = new LinkedList<Country>();

		if (linkedList.size() == 0) {
			return "false";
		} else {

			for (Country c : linkedList) {
				String cname = String.valueOf(c.getName());
				while (observable.canTransfer(player, name, cname) && (c.getArmy() > 1)) {// can transfer and armies																				// more than 1;
					list.add(c);
					break;
				}
			}
			
			if (list.size() == 0) {
				return "false";
			} 
			Collections.sort(list);
			String result = "true " + String.valueOf(list.peekLast().getName());
			return result;
		}
	}

}
