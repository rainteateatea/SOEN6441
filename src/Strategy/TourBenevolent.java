package Strategy;

import java.time.Year;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JLabel;

import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Player;
import View.BackEnd;

public class TourBenevolent implements BehaviorStrategy{

	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	@Override
	public void reinforcemnet(JLabel c, String click, InitializePhase observable, BackEnd b) {
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		String player = click;
		String fullname =  playerSet.get(player).getPlayerName()+"_"+player;
		System.out.println(fullname+" enter Reinforcement phase");
		
	
		Player curPlayer = playerSet.get(player);

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
			
				if (observable.getDturns() == observable.D) {
					System.out.println("it is a draw");
					observable.refreshgame("draw");
				}
				else {
					// cannot attack enter fortification phase
					System.out.println("enter Benevolent fortification phase");
					fortification(null, null, player, observable, b);
				}

			
		}

	}

	@Override
	public void attack(JLabel from, JLabel to, InitializePhase observable, BackEnd b) {
		
	}

	@Override
	public void fortification(JLabel from, JLabel c, String to, InitializePhase observable, BackEnd b) {
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		String player = to;
		String fullname =  playerSet.get(player).getPlayerName()+"_"+player;
		
		Player curPlayer = playerSet.get(player);
		
		Country fromCountry = innerCountry(curPlayer);
	

		LinkedList<Country> list = new LinkedList<Country>();
		list = curPlayer.getCountryList();
		Collections.sort(list);

		for (Country country : list) {
		//	System.out.println(fromCountry.getContinent()+fromCountry.getArmy()+" "+country.getContinent()+country.getArmy());
			if (observable.canTransfer(player, String.valueOf(fromCountry.getName()),
					String.valueOf(country.getName()))) {
				int move = fromCountry.getArmy() - 1;
			
				observable.Fortification(String.valueOf(fromCountry.getName()),
						String.valueOf(country.getName()),move);
				System.out.println(fullname+"Fortification : from " + fromCountry.getName() + " to " + country.getName() + " move " + move);
				break;
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
