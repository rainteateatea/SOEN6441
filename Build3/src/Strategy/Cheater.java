package Strategy;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
		System.out.println(playView.name.getText()+"&enter reinforcement phase");
		String player = fullname[1];
		playerSet = observable.getPlayerSet();
		ArrayList<String> countList = new ArrayList<>();
		for (int i = 0; i < playerSet.get(player).getCountryList().size(); i++) {
			int country = playerSet.get(player).getCountryList().get(i).getName();
			countList.add(String.valueOf(country));
		}
		observable = observable.cheaterRein(player, countList);
		playerSet = observable.getPlayerSet();
		countries = observable.getCountries();
		boolean canAttack = canAttack(player);
		if (canAttack) {
			//attack phase
			System.out.println((playView.name.getText()+"enter Attack phase"));
			playView.currentPhase = "Attack";
			playView.phase.setText("Attack");
			JLabel attacker = new JLabel(player);
		//	observable.internalPhase(true, "Attack");
			//b does not update
			attack(attacker, attacker, observable, b);
		}
		else if(!canAttack && playerSet.size()!=1){
			System.out.println(player+"enter fortification phase");
			playView.phase.setText("Fortification");
			playView.currentPhase = "Fortification";
			//fortification phase
			fortification(c, c, player, observable, b);
		}
		
		
		
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
		HashMap<String, String> occupylist = new HashMap<>();
		//ArrayList<String> occupylist = new ArrayList<>();
		String player = from.getText();
		for (int i = 0; i < playerSet.get(player).getCountryList().size(); i++) {
			String atcoun  = String.valueOf(playerSet.get(player).getCountryList().get(i).getName());
			
			String[] countlist = countries.get(atcoun).getCountryList().split(" ");
			for (int j = 0; j < countlist.length; j++) {
				String defender = b.findPlayer(countlist[j]);
				if (!player.equals(defender)) {
					String territy = player+"_"+defender;
					occupylist.put(countlist[j], territy);
				}
			}
		}
		observable = observable.cheaterAttack(occupylist);
		playerSet = observable.getPlayerSet();
		if (playerSet.size()!=1) {
			System.out.println(playView.name.getText()+"enter fortification phase");
			playView.phase.setText("Fortification");
			playView.currentPhase = "Fortification";
			fortification(null, null, player, observable, b);
		}
	
		else {
			JOptionPane.showMessageDialog(null,
					"Congradulation!!!!player " +  playView.name.getText() + " is winnner!!!");
			
			
		}
	
		
	}

	@Override
	public void fortification(JLabel from, JLabel c, String player,
			InitializePhase observable, BackEnd b) {
		playerSet = observable.getPlayerSet();
		countries = observable.getCountries();
		LinkedList<Country> atList = playerSet.get(player).getCountryList();
		for (int i = 0; i < atList.size(); i++) {
			String hiscountry = String.valueOf(atList.get(i).getName());
			String[] neighbour = countries.get(hiscountry).getCountryList().split(" ");
			for (int j = 0; j < neighbour.length; j++) {
				String checkplayer = findPlayer(neighbour[j]);
				if (!player.equals(checkplayer)) {
					observable = observable.cheaterForti(player,atList.get(i).getName());
					break;
				}
			}
		}
		// fortification only one time enter reinforcement
					playView.currentPhase = "Reinforcement";
					playView.phase.setText("Reinforcement");
					playerSet = observable.getPlayerSet();
					String nextP = findnext(player,observable);
					if (!nextP.equals(player)) {
						
					
					// change player
					String playername = playerSet.get(nextP).getPlayerName()+"_"+nextP;
					playView.name.setText(playername);
					playView.color.setBackground(playerSet.get(nextP).getColor());
					
					//next player is Human and card army != 0
					if (playerSet.get(nextP).getCardList().size() != 0 && playerSet.get(nextP).getPlayerName().equals("Human")) {
						observable.Reinforcement(nextP);
						observable.cardArmy(nextP, playerSet.get(nextP).getCardList(), false);
						playView.armies.setText(
								"<html><body><p align=\"center\">calculating...<br/>press&nbsp;reinforcement</p></body></html>");

					}
					//next player is Human and card army ==0
					else if(playerSet.get(nextP).getCardList().size() == 0 &&playerSet.get(nextP).getPlayerName().equals("Human")){
						observable.Reinforcement(nextP);
						playView.armies.setText(String.valueOf(playerSet.get(nextP).getArmy()));
					}
					// next player is not human
					else if (!playerSet.get(nextP).getPlayerName().equals("Human")) {
						observable.nextTurn(1);
					}
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
