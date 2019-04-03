package Strategy;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Player;
import View.BackEnd;
import View.PlayView;
import View.CardView.CardPane.checkCard;
import View.InitGame.initPane;

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

		observable.nextTurn(0);

		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);

//		random get a country
		Random random = new Random();
		int index = random.nextInt(curPlayer.getCountryList().size());
		Country country = curPlayer.getCountryList().get(index);
		playView.setColor(toString().valueOf(country.getName()));

//		invoking startUp
		while (curPlayer.getArmy() != 0) {
			observable.Startup(playView.name.getText().split("_")[1], toString().valueOf(country.getName()));
		}

		if (curPlayer.getArmy() == 0) {
			boolean canAttack = b.canAttack(playView.name.getText().split("_")[1]);
			if (canAttack) {

				// enter attack phase
				System.out.println("enter Random Attack phase");
				playView.setNull(toString().valueOf(country.getName()));
				attack(null, null, observable, b);

			} else {

				// cannot attack enter fortification phase
				System.out.println("enter Random fortification phase");
				playView.setNull(toString().valueOf(country.getName()));
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
		playView.setColor(att);
		playView.setColor(def);

//		judge attack or not

		Random isAttack = new Random();
		String tmp = "";
		while (isAttack.nextBoolean() && (countries.get(att).getArmy() != 1) && (!capture)) {

			int[] dices = findDicesNum(att, def);
			int attD = dices[0];
			int defD = dices[1];
			tmp = observable.attackPhase(att, def, "One_Time", attD, defD);

			String[] canTransfer = tmp.split(" ");
			if (Integer.parseInt(canTransfer[2]) != 0) {
				capture = true;
			}
		}

//		updating information and transfer armies
		String[] record = tmp.split(" ");
		if (tmp == "") {

			playView.setNull(att);
			playView.setNull(def);
			fortification(null, null, null, observable, b);

		} else {
			if (!record[1].equals("0")) {
				int armies = randomArimes(Integer.parseInt(record[1]), Integer.parseInt(record[2]));
				observable.Fortification(att, def, armies);
				playView.setNull(att);
				playView.setNull(def);
				fortification(null, null, null, observable, b);
			}

			playView.setNull(att);
			playView.setNull(def);
			fortification(null, null, null, observable, b);
		}

	}

	@Override
	public void fortification(JLabel from, JLabel c, String to, InitializePhase observable, BackEnd b) {

		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		Player curPlayer = playerSet.get(playView.name.getText().split("_")[1]);

		LinkedList<Country> cantransferCountries = new LinkedList<Country>();
		for (Country country : curPlayer.getCountryList()) {
			if (country.getArmy() > 1) {
				cantransferCountries.add(country);
			}
		}

		Random random = new Random();
		int index = random.nextInt(cantransferCountries.size());
		Country fromCountry = cantransferCountries.get(index);
		playView.setColor(toString().valueOf(fromCountry.getName()));
		cantransferCountries.clear();

		for (Country country : curPlayer.getCountryList()) {
			if ((country.getName() != fromCountry.getName())
					&& observable.canTransfer(playView.name.getText().split("_")[1],
							toString().valueOf(fromCountry.getName()), toString().valueOf(country.getName()))) {
				cantransferCountries.add(country);
			}
		}

		index = random.nextInt(cantransferCountries.size());
		Country toCountry = cantransferCountries.get(index);
		playView.setColor(toString().valueOf(toCountry.getName()));

		int armires = randomArimes(1, fromCountry.getArmy() - 1);

		observable.Fortification(toString().valueOf(fromCountry.getName()), toString().valueOf(toCountry.getName()),
				armires);
		playView.setNull(toString().valueOf(fromCountry.getName()));
		playView.setColor(toString().valueOf(toCountry.getName()));
		observable.nextTurn(1);

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

			ArrayList<String> list = checkValidAtt(index, checkList, player);

			if (list.size() == 0) {

				checkList.remove(index);

			} else {

				result = result + toString().valueOf(checkList.get(index).getName()) + " ";
				att = true;
				int i = random.nextInt(list.size());
				result = result + list.get(i);
				def = true;
			}

		}

		if (att && def) {

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

		Country curCountry = check.get(index);
		String[] adj = curCountry.getCountryList().split(" ");
		ArrayList<String> list = new ArrayList<String>();

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

		int max = from;
		int min = to;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;

		System.out.println("Random transfer armies: " + s);

		return s;
	}

}
