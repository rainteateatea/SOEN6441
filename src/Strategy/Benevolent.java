package Strategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JLabel;

import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Player;
import View.BackEnd;
import View.PlayView;

public class Benevolent implements BehaviorStrategy {

	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	PlayView playView;

	@Override
	public void reinforcemnet(JLabel c, String click, InitializePhase observable, BackEnd b) {
		observable.nextTurn(0);
		System.out.println("enter Benevolent Reinforcement phase");
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		String[] fullname = playView.name.getText().split("_");
		String player = fullname[1];
		Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);

//		find weakest countries

		observable.Reinforcement(player);

		LinkedList<Country> list = new LinkedList<Country>();
		list = findWeakCountry(curPlayer);
		
		Collections.sort(list);

		while (curPlayer.getArmy() != 0) {
			for (Country country : list) {
				if (curPlayer.getArmy() == 0) {
					break;
				} else {
					System.out.println("Reinfocement country is " + country.getName());
					//playView.setColor(toString().valueOf(country.getName()));
					observable.Startup(player, String.valueOf(country.getName()));
				//	playView.setNull(toString().valueOf(country.getName()));
				}
			}
		}

		
		if (curPlayer.getArmy() == 0) 
		{
			if (observable.TournamentMode) {
				if (observable.getDturns() == observable.D) {
					System.out.println("it is a draw");
					observable.refreshgame("draw");
				}
				else {
					// cannot attack enter fortification phase
					System.out.println("enter Benevolent fortification phase");
					fortification(null, null, null, observable, b);
				}
				
			}
			else {
				// cannot attack enter fortification phase
				System.out.println("enter Benevolent fortification phase");
				fortification(null, null, null, observable, b);
			}

			
		}

	}

	@Override
	public void attack(JLabel from, JLabel to, InitializePhase observable, BackEnd b) {
		System.out.println("enter Benevolent fortification phase");
		
		//fortification(null, null, null, observable, b);
	}

	@Override
	public void fortification(JLabel from, JLabel c, String to, InitializePhase observable, BackEnd b) {
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		String[] fullname = playView.name.getText().split("_");
		String player = fullname[1];
		Player curPlayer = playerSet.get(fullname[1]);
		
		Country fromCountry = innerCountry(curPlayer);
	

		LinkedList<Country> list = new LinkedList<Country>();
		list = curPlayer.getCountryList();
		Collections.sort(list);

		for (Country country : list) {
		//	System.out.println(fromCountry.getContinent()+fromCountry.getArmy()+" "+country.getContinent()+country.getArmy());
			if (observable.canTransfer(fullname[1], String.valueOf(fromCountry.getName()),
					String.valueOf(country.getName()))) {
				int move = fromCountry.getArmy() - 1;
			
				observable.Fortification(String.valueOf(fromCountry.getName()),
						String.valueOf(country.getName()),move);
				System.out.println( playView.name.getText()+"Fortification : from " + fromCountry.getName() + " to " + country.getName() + " move " + move);
				break;
			}

		}
		
		
		
		// fortification only one time enter reinforcement
		playView.currentPhase = "Reinforcement";
		playView.phase.setText("Reinforcement");
		playerSet = observable.getPlayerSet();
		String nextP = findnext(player,observable);
		// change player
		String playername = playerSet.get(nextP).getPlayerName()+"_"+nextP;
		playView.name.setText(playername);
		playView.color.setBackground(playerSet.get(nextP).getColor());
		
		//next player 为 Human 并且 card army 不为0
		if (playerSet.get(nextP).getCardList().size() != 0 && playerSet.get(nextP).getPlayerName().equals("Human")) {
			observable.Reinforcement(nextP);
			observable.cardArmy(nextP, playerSet.get(nextP).getCardList(), false);
			playView.armies.setText(
					"<html><body><p align=\"center\">calculating...<br/>press&nbsp;reinforcement</p></body></html>");

		}
		//next player 为 Human 并且 card army 为0
		else if(playerSet.get(nextP).getCardList().size() == 0 &&playerSet.get(nextP).getPlayerName().equals("Human")){
			observable.Reinforcement(nextP);
			playView.armies.setText(String.valueOf(playerSet.get(nextP).getArmy()));
		}
		// next player 不是 human
		else if (!playerSet.get(nextP).getPlayerName().equals("Human")) {
			observable.nextTurn(1);
		}
		
		

	
	}
	/**
	 * This method finds who is next player.
	 * 
	 * @param current Current player.
	 * @return Next player.
	 */
	public String findnext(String current,InitializePhase observable) {

		int max = maxplayer();
		String next = String.valueOf(Integer.valueOf(current) + 1);
		if (Integer.valueOf(current) == max) {
			next = "1";
			if (observable.TournamentMode) {
				observable.addturn();
			}
			
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
	private int maxplayer() {
		int max = 0;
		for (String key : playerSet.keySet()) {
			int temp = Integer.valueOf(key);
			if (temp > max) {
				max = temp;
			}
		}
		return max;
	}
	private LinkedList<Country> findWeakCountry(Player player) {
		LinkedList<Country> list = new LinkedList<Country>();

		for (Country country : player.getCountryList()) {
			String[] coStrings = country.getCountryList().split(" ");
			for (int i = 0; i < coStrings.length; i++) {
				if (!country.getColor().equals(countries.get(coStrings[i]).getColor())) {
					list.add(country);
					break;
				}
			}
		}

		return list;
	}

	private Country innerCountry(Player player) {
		LinkedList<Country> list = new LinkedList<Country>();
		for (Country country : player.getCountryList()) {
			String[] coStrings = country.getCountryList().split(" ");
			for (int i = 0; i < coStrings.length; i++) {
				if (!country.getColor().equals(countries.get(coStrings[i]).getColor())) {
					break;
				}
			}
			if (country.getArmy() > 1) {
				list.add(country);
			}
		}

		if (list.size() != 0) {
			Collections.sort(list);
			return list.peekLast();
		} else {
			list = player.getCountryList();
			Collections.sort(list);
			return list.peekLast();
		}

	}

}