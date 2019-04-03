package Strategy;

import java.util.Collection;
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

public class Aggressive implements BehaviorStrategy {

	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	PlayView playView;

	@Override
	public void reinforcemnet(JLabel c, String click, InitializePhase observable, BackEnd b) {
		observable.nextTurn(0);
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);

		Country strongestCountry = findStrongsetCountry(curPlayer);
		playView.setColor(toString().valueOf(strongestCountry.getName()));

		observable.Reinforcement(playView.name.getText().split("_")[1]);

		while (curPlayer.getArmy() != 0) {
			observable.Startup(playView.name.getText().split("_")[1], toString().valueOf(strongestCountry.getName()));
		}

		playView.setNull(toString().valueOf(strongestCountry.getName()));

		if (curPlayer.getArmy() == 0) {
			boolean canAttack = b.canAttack(playView.name.getText().split("_")[1]);
			if (canAttack) {

				// enter attack phase
				System.out.println("enter Aggressive Attack phase");
				attack(null, null, observable, b);

			} else {

				// cannot attack enter fortification phases
				System.out.println("enter Aggressive fortification phase");
				fortification(null, null, null, observable, b);
			}
		}
	}

	@Override
	public void attack(JLabel from, JLabel to, InitializePhase observable, BackEnd b) {
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);

//		use strongest country to attack until it cannot attack anymore
		Country attCountry = findStrongsetCountry(curPlayer);
		playView.setColor(toString().valueOf(attCountry));
		
		LinkedList<Country> list = new LinkedList<Country>();
		list = fingneighbours(curPlayer, attCountry);
		
		while ((list.size()!= 0) && (attCountry.getArmy()!= 1)) {
			
			for (Country defCountry : list) {
				
				playView.setColor(toString().valueOf(defCountry));
				String[] result = observable.attackPhase(toString().valueOf(attCountry.getName()),
						toString().valueOf(defCountry.getName()), "All_Out", 0, 0).split(" ");
				
				if (result[1] != "0" && list.size() != 0) {
					observable.Fortification(toString().valueOf(attCountry.getName()),
							toString().valueOf(defCountry.getName()), Integer.parseInt(result[1]));
				} else if(result[1] != "0" && list.size() == 0){
					observable.Fortification(toString().valueOf(attCountry.getName()),
							toString().valueOf(defCountry.getName()), Integer.parseInt(result[2]));
				}
				playView.setName( toString().valueOf(defCountry.getName()));
			}			
			
		}
		
		playView.setName( toString().valueOf(attCountry.getName()));
		
		System.out.println("enter Aggressive fortification phase");
		fortification(null, null, null, observable, b);
		
	}

	@Override
	public void fortification(JLabel from, JLabel c, String to, InitializePhase observable, BackEnd b) {
		
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);

		LinkedList<Country> list = new LinkedList<Country>(curPlayer.getCountryList());
		Collections.sort(list);
		
		Country strongestCountry;
		
		for (int i = list.size()-1; i >=0 ; i--) {
			strongestCountry = list.get(i);
			if (findTransferCountry(curPlayer, strongestCountry, observable) != null) {
				Country fromCountry = findTransferCountry(curPlayer, strongestCountry, observable);
				playView.setColor(toString().valueOf(strongestCountry.getName()));
				playView.setColor(toString().valueOf(fromCountry.getName()));
				
				observable.Fortification(toString().valueOf(fromCountry.getName()), 
						toString().valueOf(strongestCountry.getName()), fromCountry.getArmy()-1);
				
				playView.setNull(toString().valueOf(strongestCountry.getName()));
				playView.setNull(toString().valueOf(fromCountry.getName()));
				break;
			}
		}
		
		observable.nextTurn(1);
	}

	private Country findStrongsetCountry(Player player) {
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
		Collections.sort(list);

		return list.getLast();
	}

	private LinkedList<Country> fingneighbours(Player player, Country country) {
		LinkedList<Country> list = new LinkedList<Country>();
		String[] coStrings = country.getCountryList().split(" ");
		for (int i = 0; i < coStrings.length; i++) {
			if (!country.getColor().equals(countries.get(coStrings[i]).getColor())) {
				list.add(countries.get(coStrings[i]));
			}
		}
		Collections.sort(list);
		return list;

	}
	
	private Country findTransferCountry(Player player, Country country, InitializePhase observable) {
		
		String pName = playView.name.getText().split("_")[1];
		LinkedList<Country> list = new LinkedList<Country>(player.getCountryList());
		Collections.sort(list);
		list.remove(country);
		Country testCountry = list.pollLast();
		
		while (!observable.canTransfer(pName, toString().valueOf(country.getName()), 
				toString().valueOf(testCountry.getName())) && (list.size() != 0) && (testCountry.getArmy() == 1)) {
			testCountry = list.pollLast();	
		}
		
		if (observable.canTransfer(pName, toString().valueOf(country.getName()), 
				toString().valueOf(testCountry.getName())) && list.size() == 0) {
			return testCountry;
		} else if (list.size() != 0) {
			return testCountry;
		}
		
		return null;
	}

}
