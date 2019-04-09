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

public class TourCheater implements BehaviorStrategy{
	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	@Override
	public void reinforcemnet(JLabel c, String click, InitializePhase observable, BackEnd b) {
		playerSet = observable.getPlayerSet();
		String player = click;
		String fullname =  playerSet.get(player).getPlayerName()+"_"+player;
		System.out.println(fullname+" enter reinforcement phase");
		
		ArrayList<String> countList = new ArrayList<>();
		for (int i = 0; i < playerSet.get(player).getCountryList().size(); i++) {
			int country = playerSet.get(player).getCountryList().get(i).getName();
			countList.add(String.valueOf(country));
		}
		observable = observable.cheaterRein(player, countList);
		playerSet = observable.getPlayerSet();
		countries = observable.getCountries();
		boolean canAttack = b.canAttack(player);
		if (canAttack) {
			//attack phase
			System.out.println((fullname+" enter Attack phase"));
			JLabel attacker = new JLabel(player);
			attack(attacker, attacker, observable, b);
		}
		else if(!canAttack && playerSet.size()!=1){
			System.out.println(fullname+"enter fortification phase");
		
			//fortification phase
			fortification(c, c, player, observable, b);
		}
		
		
		
	}

	@Override
	public void attack(JLabel from, JLabel to, InitializePhase observable, BackEnd b) {
		//from 
		playerSet = observable.getPlayerSet();
		countries = observable.getCountries();
		HashMap<String, String> occupylist = new HashMap<>();
		//ArrayList<String> occupylist = new ArrayList<>();
		String player = from.getText();
		String fullname =  playerSet.get(player).getPlayerName()+"_"+player;
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
			if (observable.getDturns() == observable.D) {
				System.out.println("it is a draw");
				observable.refreshgame("draw");
			}else {
				System.out.println(fullname+"enter fortification phase");
				fortification(null, null, player, observable, b);
			}
			
		}
		else {
			System.out.println("Congradulation!!!!player " + fullname + " is winnner!!!");
			observable.refreshgame(playerSet.get(player).getPlayerName());
			
			
		}
	
		
	}

	@Override
	public void fortification(JLabel from, JLabel c, String player, InitializePhase observable, BackEnd b) {
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
