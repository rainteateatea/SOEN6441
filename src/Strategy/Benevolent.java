package Strategy;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

import javax.swing.JLabel;

import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Player;
import View.BackEnd;
import View.PlayView;

public class Benevolent implements BehaviorStrategy{

	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	PlayView playView;

	@Override
	public void reinforcemnet(JLabel c, String click, InitializePhase observable, BackEnd b) {
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);
		
//		find weakest countries
		
		observable.Reinforcement(playView.name.getText().split("_")[1]);
		
		LinkedList<Country> list = new LinkedList<Country>();
		list = findWeakCountry(curPlayer);
		Collections.sort(list);
		
		while(curPlayer.getArmy() != 0) {
			for(Country country : list) {
				if (curPlayer.getArmy() == 0) {
					break;
				} else {
					playView.setColor(toString().valueOf(country.getName()));
					observable.Startup(playView.name.getText().split("_")[1], toString().valueOf(country.getName()));
					playView.setNull(toString().valueOf(country.getName()));
				}
			}
		}
		
		if (curPlayer.getArmy() == 0) {
			boolean canAttack = b.canAttack(playView.name.getText().split("_")[1]);
			if (canAttack) {

				// enter attack phase
				System.out.println("enter Benevolent Attack phase");
				attack(null, null, observable, b);

			} else {

				// cannot attack enter fortification phase
				System.out.println("enter Benevolent fortification phase");
				fortification(null, null, null, observable, b);
			}

		}	
		
	}

	@Override
	public void attack(JLabel from, JLabel to, InitializePhase observable, BackEnd b) {
		System.out.println("enter Benevolent fortification phase");
		fortification(null, null, null, observable, b);	
	}

	@Override
	public void fortification(JLabel from, JLabel c, String to, InitializePhase observable, BackEnd b) {
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);
		
		Country fromCountry = innerCountry(curPlayer);
		playView.setColor(toString().valueOf(fromCountry.getName()));
		
		LinkedList<Country> list = new LinkedList<Country>();
		list = curPlayer.getCountryList();
		Collections.sort(list);
		
		for(Country country : list) {
			if (observable.canTransfer(playView.name.getText().split("_")[1], 
					toString().valueOf(fromCountry.getName()), toString().valueOf(country.getName()))) {
				playView.setColor(toString().valueOf(country.getName()));
				observable.Fortification(toString().valueOf(fromCountry.getName()), 
						toString().valueOf(country.getName()), fromCountry.getArmy()-1);
				playView.setNull(toString().valueOf(country.getName()));
				playView.setNull(toString().valueOf(fromCountry.getName()));
				break;
			}
			
		}
		
		observable.nextTurn(1);
	}
	
	private LinkedList<Country> findWeakCountry(Player player) {
		LinkedList<Country> list = new LinkedList<Country>();
		
		for(Country country : player.getCountryList()) {
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
	
	private Country innerCountry( Player player) {
		LinkedList<Country> list = new LinkedList<Country>();
		for(Country country : player.getCountryList()) {
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
