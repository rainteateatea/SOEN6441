package Strategy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;


import javax.swing.JLabel;

import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Player;
import View.BackEnd;
import View.PlayView;

public class RandomSt implements BehaviorStrategy {

	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	PlayView playView;

	/**
	 * This method executes reinforcement phase.
	 *
	 * @param c           The country that is chosen.
	 * @param click       The country name.
	 * @param observeable The InitializePhase class.
	 * @param b           The BackEnd class.
	 */
	@Override
	public void reinforcemnet(JLabel c, String click, InitializePhase observable, BackEnd b) {

//		random get a country and adding all armies in this country
		observable.nextTurn(0);

		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);

//		random get a country
		Random random = new Random();
		int index = random.nextInt(curPlayer.getCountryList().size());
		Country country = curPlayer.getCountryList().get(index);

//		invoking startUp
		while (curPlayer.getArmy() != 0) {
			observable.Startup(playView.name.getText().split("_")[1], toString().valueOf(country.getName()));
		}

		System.out.println("Reinfocement country is " + country.getName());

		if (curPlayer.getArmy() == 0) {
			boolean canAttack = b.canAttack(playView.name.getText().split("_")[1]);
			if (canAttack) {

				// enter attack phase
				System.out.println("enter Random Attack phase");

				attack(null, null, observable, b);

			} else {

				// cannot attack enter fortification phase
				System.out.println("enter Random fortification phase");

				fortification(null, null, null, observable, b);
			}

		}

	}

	@Override
	public void attack(JLabel attcker, JLabel defender, InitializePhase observable, BackEnd b) {

		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		boolean capture = false;

//		find a random attacker and defender
		String result = findAttDef(playView.name.getText().split("_")[1]);
		String[] info = result.split(" ");
		String att = info[0];
		String def = info[1];

//		judge attack or not
		Random isAttack = new Random();
		String tmp = "";

//		if random is false then not attack
		while (isAttack.nextBoolean() && (countries.get(att).getArmy() != 1) && (!capture)) {

			int[] dices = findDicesNum(att, def);
			int attD = dices[0];
			int defD = dices[1];
			tmp = observable.attackPhase(att, def, "One_Time", attD, defD);

			System.out.println("Att is : " + att + " Def is " + def);

//			if defender is captured, set capture is "true"
			String[] canTransfer = tmp.split(" ");
			if (Integer.parseInt(canTransfer[2]) != 0) {
				capture = true;
			}
		}

//		updating information and transfer armies
		if(tmp == "") {
			System.out.println("Get into Random fortification: ");
			fortification(null, null, null, observable, b);
		} else {
			String[] record = tmp.split(" ");
		if (!record[1].equals("0")) {
			int armies = randomArimes(Integer.parseInt(record[1]), Integer.parseInt(record[2]));
			observable.Fortification(att, def, armies);

			System.out.println( playView.name.getText() + " transfer armies from " + att + " to " + def + " armies: " + armies);

			
			System.out.println( playView.name.getText() +" get into Random fortification:");
			fortification(null, null, null, observable, b);
			} else {
				System.out.println( playView.name.getText() +" get into Random fortification:");
				fortification(null, null, null, observable, b);
			}
		}
		

		

	}

	@Override
	public void fortification(JLabel from, JLabel c, String to, InitializePhase observable, BackEnd b) {

		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);

		String[] fullname = playView.name.getText().split("_");
		String player = fullname[1];
//		a country can transfer which must hold more than 1 armies
		LinkedList<Country> cantransferCountries = new LinkedList<Country>();
		for (Country country : curPlayer.getCountryList()) {
			if (country.getArmy() > 1) {
				cantransferCountries.add(country);
			}
		}

//		get a random country which can transfer armies
		Random random = new Random();
		int index = (int)(0+Math.random()*cantransferCountries.size());
	//	int index = random.nextInt(cantransferCountries.size());
	//	(int)(0+Math.random()*list.size());
		if(cantransferCountries.size() != 0) {
			Country fromCountry = cantransferCountries.get(index);
			cantransferCountries.clear();

			for (Country country : curPlayer.getCountryList()) {
				if ((country.getName() != fromCountry.getName())// cannot self-transfer..
						&& observable.canTransfer(playView.name.getText().split("_")[1],
								toString().valueOf(fromCountry.getName()), toString().valueOf(country.getName()))) {
					cantransferCountries.add(country);
				}
			}

			if (cantransferCountries.size() == 0) {
				
				System.out.println( playView.name.getText()+ " Random country with no path to transfer");
				
			} else {
				
//				trandfer random armies to target country
				index = random.nextInt(cantransferCountries.size());
				Country toCountry = cantransferCountries.get(index);
				
				int armires = 0;
				if (fromCountry.getArmy() == 2) {
					armires = 1;
				}else {
					armires = randomArimes(1, fromCountry.getArmy() - 1);
				}
				
				observable.Fortification(toString().valueOf(fromCountry.getName()), toString().valueOf(toCountry.getName()),
						armires);

				System.out.println( playView.name.getText()+ " fortification from : " + fromCountry.getName() + " to : " + toCountry.getName()
						+ " armies : " + armires);

			}

		} 
		
		// fortification only one time enter reinforcement
		playView.currentPhase = "Reinforcement";
		playView.phase.setText("Reinforcement");
		playerSet = observable.getPlayerSet();
		String nextP = findnext(player);
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
	public String findnext(String current) {

		int max = maxplayer();
		String next = String.valueOf(Integer.valueOf(current) + 1);
		if (Integer.valueOf(current) == max) {
			next = "1";
		}
		if (playerSet.containsKey(next)) {
			return next;
		} else {
			return findnext(next);
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
	 * 
	 * @param player
	 * @return
	 */
	public String findAttDef(String player) {

		LinkedList<Country> checkList = new LinkedList<Country>();

//		the number of armies of attacker must greater than 1
		for (Country country : playerSet.get(player).getCountryList()) {
			if (country.getArmy() > 1) {
				checkList.add(country);
			}
		}

		String result = "";
		boolean att = false;
		boolean def = false;

		while (!att) {
			Random random = new Random();
			int index = random.nextInt(checkList.size());

//			check a random country whether can be an attacker or not. Checking its neightbour countries
			ArrayList<String> list = checkValidAtt(index, checkList, player);

//			if list size is 0; means random country cannot be an attacker; then find next one
//			else this random country can be an attack and find defender
			if (list.size() == 0) {

				checkList.remove(index);

			} else {

				result = result + toString().valueOf(checkList.get(index).getName()) + " ";
				att = true;
				if (list.size() == 1) {// list size is one, do not need to random
					result = result + list.get(0);
					def = true;
				} else { // list size is greater than 1, then random get a defender
					int i = random.nextInt(list.size());
					result = result + list.get(i);
					def = true;
				}

			}

		}

		if (att && def) { // result format is (attacker" "defender)

			return result;

		}

		return "Error in Random find attacker and defender";
	}

	/**
	 * 
	 * 
	 * @param index
	 * @param check
	 * @param player
	 * @return
	 */
	private ArrayList<String> checkValidAtt(int index, LinkedList<Country> check, String player) {

		Country curCountry = check.get(index);// attack country
		String[] adj = curCountry.getCountryList().split(" "); // all its neighbors
		ArrayList<String> list = new ArrayList<String>(); // storing all countries which can be a defender country

		for (String tmp : adj) {
			if (!countries.get(tmp).getColor().equals(playerSet.get(player).getColor())) {
				list.add(tmp);
			}

		}
		return list;
	}

	/**
	 * This method implements finding the number of dice in the all out mode.
	 *
	 * @return The number of dices which is chosen by attacker and defender.
	 */
	private int[] findDicesNum(String attackCountry, String defendCountry) {
		int[] dicsNum = { 0, 0 };

		Country att = countries.get(attackCountry);
		Country def = countries.get(defendCountry);

//      finding the number of dices of attacker
		if (att.getArmy() > 3) {
			dicsNum[0] = 3;
		} else if (att.getArmy() == 3) {
			dicsNum[0] = 2;
		} else if (att.getArmy() == 2) {
			dicsNum[0] = 1;
		}

//      finding the number of dices of defender
		if (def.getArmy() >= 2) {
			dicsNum[1] = 2;
			if (dicsNum[1] > dicsNum[0]) {
				dicsNum[1] = dicsNum[0];
			}
		} else if (def.getArmy() == 1) {
			dicsNum[1] = 1;
		}

		for (int i : dicsNum) {
			if (i == 0) {
				System.out.println(" Exist a zero dices");
			}
		}

		return dicsNum;
	}

	/**
	 * This method obtains a random number of armies.
	 * 
	 * @param from The number of dices.
	 * @param to   The maximum number of armies can be chosen.
	 * @return The number of armies will be transfered.
	 */
	public int randomArimes(int from, int to) {

		int min = from;
		int max = to;
		Random random = new Random();
		int s = random.nextInt(max-min+1)+min;
		System.out.println("Random transfer armies: " + s);

		return s;
	}

}