package Strategy;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.JLabel;
import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Player;
import View.BackEnd;
import View.PlayView;

public class Cheater implements BehaviorStrategy{
	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	PlayView playView;
	@Override
	public void reinforcemnet(JLabel c, String click,
			InitializePhase observable, BackEnd b) {
		observable.nextTurn(0);
		String[] fullname = playView.name.getText().split("_");
		String player = fullname[1];
		playerSet = observable.getPlayerSet();
		for (int i = 0; i < playerSet.get(player).getCountryList().size(); i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int country = playerSet.get(player).getCountryList().get(i).getName();
			observable = observable.cheaterRein(player,String.valueOf(country));
			playerSet = observable.getPlayerSet();
			playView.armies.setText(String.valueOf(playerSet.get(player).getArmy()));
		}
		playerSet = observable.getPlayerSet();
		countries = observable.getCountries();
		boolean canAttack = canAttack(player);
//		if (canAttack) {
//			//attack phase
//			JLabel attacker = new JLabel(player);
//			//b does not update
//			attack(attacker, attacker, observable, b);
//		}
//		else {
//			//fortification phase
//			fortification(c, c, player, observable, b);
//		}
		
		
		
	}
	/**
	 * This method judges whether the player can attack other countries or not.
	 * 
	 * @param player Current player.
	 * @return true if the player can attack.
	 */
	public boolean canAttack(String player) {
		boolean canAttack = false;
		LinkedList<Country> countrylist = playerSet.get(player).getCountryList();
		for (int i = 0; i < countrylist.size(); i++) {
			String attcoun = String.valueOf(countrylist.get(i).getName());
			String[] surround = countries.get(attcoun).getCountryList().split(" ");
			for (int j = 0; j < surround.length; j++) {
				Color attColor = playerSet.get(player).getColor();
				Color defColor = countries.get(surround[j]).getColor();
				if (attColor != defColor && countrylist.get(i).getArmy() > 1) {
					canAttack = true;
				}
			}
		}

		return canAttack;
	}
	@Override
	public void attack(JLabel from, JLabel to, InitializePhase observable,
			BackEnd b) {
		//from 
		playerSet = observable.getPlayerSet();
		countries = observable.getCountries();
		String player = from.getText();
		for (int i = 0; i < playerSet.get(player).getCountryList().size(); i++) {
			String atcoun  = String.valueOf(playerSet.get(player).getCountryList().get(i).getName());
			
			String[] countlist = countries.get(atcoun).getCountryList().split(" ");
			for (int j = 0; j < countlist.length; j++) {
				String defender = b.findPlayer(countlist[j]);
				if (!player.equals(defender)) {
					observable = observable.cheaterAttack(player, defender, countlist[j]);
				}
			}
		}
		
		
		
	}

	@Override
	public void fortification(JLabel from, JLabel c, String player,
			InitializePhase observable, BackEnd b) {
		playerSet = observable.getPlayerSet();
		countries = observable.getCountries();
		LinkedList<Country> atList = playerSet.get(player).getCountryList();
		for (int i = 0; i < atList.size(); i++) {
			String[] counlist = countries.get(atList.get(i)).getCountryList().split(" ");
			for (int j = 0; j < counlist.length; j++) {
				String checkplayer = findPlayer(counlist[j]);
				if (!player.equals(checkplayer)) {
					observable.cheaterForti(player,atList.get(i).getName());
					break;
				}
			}
		}
		
		
	}
	/**
	 * This method implements finding a player who owns current country.
	 *
	 * @param countryName Current country name.
	 * @return The player who owns current country.
	 */
	public String findPlayer(String countryName) {

		Country country = countries.get(countryName);
		Color color = country.getColor();
		for (Map.Entry<String, Player> entry : playerSet.entrySet()) {
			if (entry.getValue().getColor().equals(color)) {
				return entry.getKey();
			}
		}
		System.out.println("Cannot find the player!!!");
		return "";
	}

}