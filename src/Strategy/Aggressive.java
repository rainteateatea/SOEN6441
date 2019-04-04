package Strategy;

import java.awt.Color;
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
		String[] fullname = playView.name.getText().split("_");
		String player = fullname[1];
		observable.Reinforcement(player);
		PlayView.armies.setText(String.valueOf(playerSet.get(player).getArmy()));
		String strong = Strongest(player);

		

		while (curPlayer.getArmy() != 0) {
			observable.Startup(player, strong);
		}
		if (curPlayer.getArmy() == 0) {
			boolean canAttack = b.canAttack(player);
			if (canAttack) {

				// enter attack phase
				System.out.println(fullname +" enter Aggressive Attack phase");
				PlayView.phase.setText("Attack");
				PlayView.currentPhase = "Attack";
				JLabel p = new JLabel();
				p.setText(player);
				p.setName(strong);
				attack(p, null, observable, b);

			} else {

				// cannot attack enter fortification phases
				System.out.println("enter Aggressive fortification phase");
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
		String atcountry = playcountry.getName();
		String[] defcountry = countries.get(atcountry).getCountryList().split(" ");
		for (int i = 0; i < defcountry.length; i++) {
			String defender = b.findPlayer(defcountry[i]);
			if (countries.get(atcountry).getArmy()== 1) {
				break;
			}
			if (!player.equals(defender)) {
				
				String canTransfer = observable.attackPhase(player, defender, "All_Out", 0, 0);
				String record[] = canTransfer.split(" ");
				if (record[0].equals(player)) {//winner is attacker
					win = true;
					if (!record[1].equals("0")) {
						observable.Fortification(atcountry, defcountry[i], Integer.valueOf(record[1]));
					}
					
				}
			}
			
		}
		if(win){
			observable.earnCard(player);
		}
		
		

//		use strongest country to attack until it cannot attack anymore
		
		
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
			
				
				observable.Fortification(String.valueOf(fromCountry.getName()), 
						String.valueOf(strongestCountry.getName()), fromCountry.getArmy()-1);
				break;
			}
		}
		
		observable.nextTurn(1);
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