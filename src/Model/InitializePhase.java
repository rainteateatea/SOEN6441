package Model;

import java.awt.*;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import Strategy.Aggressive;
import Strategy.Benevolent;
import Strategy.Cheater;
import Strategy.Human;
import Strategy.RandomSt;
import View.CardView;

/**
 * <h1>InitializePhase</h1> 
 * This is an initialized phase class.
 * 
 * @author jiamin_he chenwei_song
 * @version 3.0
 * @since 2019-03-01 13:08
 */
public class InitializePhase extends Observable {
	private int playerNum;
	private  HashMap<String, Player> playerSet;
	private  HashMap<String, Country> countries;
	private HashMap<String, Continent> continents;
	private ArrayList<String> playerType = new ArrayList<String>();
	private ColorList cList = new ColorList();
	public boolean change = false;
	

	/**
	 * This is a constructor of initializePhase.
	 */
	public InitializePhase() {
		this.playerSet = new HashMap<>();
		this.playerNum = 0;
		
	}

	/**
	 * This method obtains a hash map which contains all player information.
	 *
	 * @return A hash map which contains all player information.
	 */
	public HashMap<String, Player> getPlayerSet() {
		return playerSet;
	}

	/**
	 * This method modifies player set.
	 *
	 * @param playerSet A hash map which contains all player information.
	 */
	public void setPlayerSet(HashMap<String, Player> playerSet) {
		this.playerSet = playerSet;
	}

	/**
	 * This method obtains a hash map storing all countries which are in the map.
	 *
	 * @return A hash map stores all countries which are in the map.
	 */
	public HashMap<String, Country> getCountries() {
		return countries;
	}

	/**
	 * This method modified a hash map storing all countries which are in the map.
	 *
	 * @param countries A new hash map storing all countries which are in the map.
	 */
	public void setCountries(HashMap<String, Country> countries) {
		this.countries = countries;
	}

	/**
	 * This method obtains a hash map storing all continents in the map.
	 *
	 * @return A hash map storing all continents in the map.
	 */
	public HashMap<String, Continent> getContinents() {
		return continents;
	}

	/**
	 * This method modified a hash map storing all continents which are in the map.
	 *
	 * @param continents A new hash map storing all continents in the map.
	 */
	public void setContinents(HashMap<String, Continent> continents) {
		this.continents = continents;
	}

	/**
	 * This method obtains the number of player.
	 *
	 * @return The number of player.
	 */
	public int getPlayerNum() {
		return playerNum;
	}

	/**
	 * This method adding data to playerNum, countries, continents.
	 *
	 * @param playerNum  The number of players.
	 * @param countries  A hash map storing all countries which are in the map.
	 * @param continents A hash map storing all continents which are in the map.
	 */
	public void addData(int playerNum, ArrayList<String> playlist,HashMap<String, Country> countries, HashMap<String, Continent> continents) {
		this.countries = countries;
		this.continents = continents;
		this.playerNum = playerNum;
		this.playerType = playlist;
	}

	/**
	 * This method invokes judgePlayerNum(), initializePlayerSet(),
	 * initializePlayerSet(), initializeCountries() to implement game's initializing
	 * phase.
	 */
	public void initPhase() {
		boolean judgeNum = judgePlayerNum();
		boolean initPlatyer = initializePlayerSet();
		boolean initAmry = initializeArmy();
		boolean initCount = initializeCountries();
		
		for (Map.Entry<String, Continent> cEntry : this.continents.entrySet()) {
			System.out.println("Control Value of Continent " + cEntry.getKey()+ " : " + cEntry.getValue().getConvalue());
		}

		if ((judgeNum && initPlatyer && initAmry && initCount) == true) {
			setChanged();
			notifyObservers(this);
		} else {
			System.out.println("Initialize Phase Failure");

		}
	}

	/**
	 * This method is used to make sure that the number of players not greater than
	 * the number of countries.
	 *
	 * @return true if less, otherwise is false.
	 */
	private boolean judgePlayerNum() {

		if (playerNum > countries.size()) {
			System.out.println("The number of player is greater than countries.");
			return false;
		} else {
			
			System.out.println("Get into initialized phase.");
			return true;
		}

	}

	/**
	 * This method implements player objects and stores them into player set.
	 *
	 * @return true if the size of player set is non-empty, otherwise is false.
	 */
	private boolean initializePlayerSet() {
		LinkedList<Color> colorLinkedList = cList.getColors();
		for (int i = 1; i <= playerNum; i++) {
			String playName = this.playerType.get(i-1);
			Player player = new Player(playName);
			//set player strategy
			switch (playName) {
			case "Human":
				player.setStrategy(new Human());
				break;
			case "Aggressive":
				player.setStrategy(new Aggressive());
				break;
			case "Benevolent":
				player.setStrategy(new Benevolent());
				break;
			case "Random":
				player.setStrategy(new RandomSt());
				break;
			case "Cheater":
				player.setStrategy(new Cheater());
				break;
			default:
				System.out.println("Get into default!!");
				break;
			}
			
			player.setColor(colorLinkedList.get(i - 1));// set player color
			playerSet.put(String.valueOf(i), player);// add player to playerSet; key is "1,2,3..." 
		}


		if (playerSet.size() == playerNum) {
			System.out.println("InitializePlayerSet success");
			return true;
		} else {
			System.out.println("InitializePlayerSet Failure");
			return false;
		}
	}

	/**
	 * This method bases the number of players to initialize the number of armies.
	 *
	 * @return true if initializes successful, otherwise is false.
	 */
	private boolean initializeArmy() {
		int armyDefault = 0;
		switch (playerNum) {
		case 2:
			armyDefault = 40;
			break;
		case 3:
			armyDefault = 15;
			break;
		case 4:
			armyDefault = 30;
			break;
		case 5:
			armyDefault = 25;
			break;
		default:
			break;
		}

		if (armyDefault == 0) {
			System.out.println("InitializeArmy Failure");
			return false;
		} else {
			for (Map.Entry<String, Player> entry : playerSet.entrySet()) {
				entry.getValue().setArmy(armyDefault);
			}
			System.out.println("InitializeArmy success");
			return true;
		}
	}

	/**
	 * This method implements randomly assigning countries to players and setting
	 * player's color, setting every country with one armies.
	 *
	 * @return true if initialization is succeed, otherwise is false.
	 */
	private boolean initializeCountries() {
		if (!judgePlayerNum()) {

			System.out.println("InitializeCountries Failure");
			return false;

		} else {
			LinkedList<String> countryList = new LinkedList<>();
			for (Map.Entry<String, Country> entry : countries.entrySet()) {
				countryList.addLast(entry.getKey());
			}

			while (!countryList.isEmpty()) {
				Random randomC = new Random();
				if (countryList.size() < playerNum) {
					LinkedList<String> playerList = new LinkedList<>();
					for (Map.Entry<String, Player> entry : playerSet.entrySet()) {
						playerList.addLast(entry.getKey());
					}
					for (String str : countryList) {
						Random randomP = new Random();
						int index = randomP.nextInt(playerList.size());
						String tmpP = playerList.get(index);
						Country country = countries.get(str);
						Player player = playerSet.get(tmpP);
						country.setColor(player.getColor());// set country color
						country.setArmy(1);// set country
						LinkedList<Country> pcountryList = player.getCountryList();
						player.setArmy(player.getArmy() - 1);
						pcountryList.addLast(country);
					}
					countryList.clear();

				} else {
					for (Map.Entry<String, Player> entry : playerSet.entrySet()) {

						int index = randomC.nextInt(countryList.size());
						String remove = countryList.get(index);
						Country country = countries.get(remove);
						country.setColor(entry.getValue().getColor());// set country color
						country.setArmy(1);// set country army
						LinkedList<Country> pcountryList = entry.getValue().getCountryList();
						entry.getValue().setArmy(entry.getValue().getArmy() - 1); // update player army
						pcountryList.addLast(country);
						countryList.remove(remove);
					}
				}
			}
			return true;
		}
	}

	/**
	 * This method implements player allocated the number of initial armies.
	 *
	 * @param pname   Player name.
	 * @param couname Country name.
	 */
	public void Startup(String pname, String couname) {

		int parmies = playerSet.get(pname).getArmy() - 1;
		playerSet.get(pname).setArmy(parmies);

		int carmies = countries.get(couname).getArmy() + 1;
		countries.get(couname).setArmy(carmies);
		setChanged();
		notifyObservers(this);

	}

	/**
	 * This method implements Reinforcement phase.
	 * 
	 * @param player Player name.
	 */
	public void Reinforcement(String player) {

		// update armies number for each player
		int system = SystemArmy(player);
		int continent = ContinentArmy(player);
		int card = 0;
if (!playerSet.get(player).equals("Human")) {
			
			String result = autoChangeCard(playerSet.get(player));
			String[] tmp = result.split(" ");
			card = card + Integer.parseInt(tmp[0]);
			
			while (Integer.parseInt(tmp[1]) == 1) {
				
				result = autoChangeCard(playerSet.get(player));
				tmp = result.split(" ");
				card = card + Integer.parseInt(tmp[0]);
			}
			
		}
		
		playerSet.get(player).setArmy(system + continent + card);

		setChanged();
		notifyObservers(this);

	}

	/**
	 * This method calculating the number of reinforcement armies.
	 *
	 * @param player Player name.
	 * @return The number of reinforcement armies.
	 */
	public int SystemArmy(String player) {

		if (playerSet.get(player).getCountryList().size() <= 9) {
			return 3;
		}
		// countries # >9
		else {
			int n = playerSet.get(player).getCountryList().size() / 3;
			n = (int) Math.floor(n);
			return n;
		}
	}

	/**
	 * This method judges a play whether occupies a continent or not. If yes, then
	 * player get control value, otherwise.
	 *
	 * @param player Player name.
	 * @return Control value.
	 */
	public int ContinentArmy(String player) {
		int n = 0;
		LinkedList<Country> captital = playerSet.get(player).getCountryList();
		HashMap<String, Integer> cal = new HashMap<>();
		LinkedList<String> listB = new LinkedList<>();
		for (int i = 0; i < captital.size(); i++) {
			String c = captital.get(i).getContinent();

			listB.add(c);
		}
		for (int i = 0; i < listB.size(); i++) {
			int temp = Collections.frequency(listB, listB.get(i));
			if (continents.get(listB.get(i)).getCountryList().size() == temp) {

				cal.put(listB.get(i), continents.get(listB.get(i)).getConvalue());
			}
		}
		for (String key : cal.keySet()) {
			n = n + cal.get(key);
		}
		return n;

	}

	/**
     * This method is used to calculate army produced by cards.
     *
     * @param player Name of player.
     * @param reCards Cards player hold after change cards phase.
     * @param change Change cards or not.
     */
	public void cardArmy(String player, LinkedList<Card> reCards, boolean change) {
		if (!change) {

			CardView cardview = new CardView(this);
			this.addObserver(cardview);
			cardview.receive(player, playerSet.get(player).getCardList());

		}

		if (change) {

			int size = playerSet.get(player).getCardList().size();
			int resize = reCards.size();

			if (resize != size) {

				playerSet.get(player).setCardList(reCards);
				int armies = playerSet.get(player).getArmy();
				int times = playerSet.get(player).getChangeCardTime();
				playerSet.get(player).setArmy(armies + times * 5);
				playerSet.get(player).setChangeCardTime(times + 1);
				if (resize >= 3) {
					cardArmy(player, playerSet.get(player).getCardList(), false);
				}
			}

			setChanged();
			notifyObservers(this);

		}

	}

	/**
	 * This method implements attack object and invokes attack function.
	 * 
	 * @param attacker Attack country's name.
	 * @param defender Defended country's name.
	 * @param mode     Attack mode which player chooses.
	 * @param attDices The number of dices attacker chooses.
	 * @param defDices The number of dices defender chooses.
	 * @return The result of attack phase.
	 */
	public String attackPhase(String attacker, String defender, String mode, int attDices, int defDices) {

		Attack attack = new Attack(this.countries, this.continents, this.playerSet, attacker, defender, mode, attDices,
				defDices);

		String result = attack.attacking();// invoking attacking function

		if (result != "") {
			setChanged();
			notifyObservers(this);
			return result;

		} else {
			System.out.println("Attack Failure!!!");
		}

		return "Failure";
	}

	/**
	 * This method implements armies transfer in attack phase.
	 * 
	 * @param attacker Attack country's name.
	 * @param defender Defended country's name.
	 * @param armies   The number of armies that player want to move.
	 */
	public void attTransforArmies(String attacker, String defender, int armies) {
		Attack attack = new Attack(this.countries, this.continents, this.playerSet, attacker, defender);
		attack.transferArmy(armies);
		System.out.println(" Attack Phase: finished transfer armies.");
		setChanged();
		notifyObservers(this);

	}

	/**
	 * This method a valid fortification.
	 *
	 * @param from Starting country.
	 * @param to   Target country.
	 * @param move The number of armies to be moved.
	 */
	public void Fortification(String from, String to, int move) {
		int start = countries.get(from).getArmy() - move;
		countries.get(from).setArmy(start);

		int end = countries.get(to).getArmy() + move;
		countries.get(to).setArmy(end);

		setChanged();
		notifyObservers(this);

	}

	
	/**
	 * This method implements auto changing cards.
	 * 
	 * @param curPlayer Current player.
	 * @return Result of changing cards.
	 */
	public String autoChangeCard(Player curPlayer) {

		if (curPlayer.getCardList().size() < 3) {
			return "0 0";
		}

		int armies = 0;
		LinkedList<Card> newlist = new LinkedList<Card>();
		LinkedList<Card> i = new LinkedList<Card>();
		LinkedList<Card> c = new LinkedList<Card>();
		LinkedList<Card> a = new LinkedList<Card>();

		for (Card card : curPlayer.getCardList()) {
			if (card.getName().equals(i)) {
				i.add(card);
			} else if (card.getName().equals(a)) {
				a.add(card);
			} else {
				c.add(card);
			}
		}

		if (i.size() >= 3) {
			for (int j = 0; j < 3; j++) {
				i.pollFirst();
			}

			armies = armies + curPlayer.getChangeCardTime() * 5;
			curPlayer.setChangeCardTime(curPlayer.getChangeCardTime() + 1);
		} else if (c.size() >= 3) {
			for (int j = 0; j < 3; j++) {
				c.pollFirst();
			}

			armies = armies + curPlayer.getChangeCardTime() * 5;
			curPlayer.setChangeCardTime(curPlayer.getChangeCardTime() + 1);
		} else {
			for (int j = 0; j < 3; j++) {
				a.pollFirst();
			}

			armies = armies + curPlayer.getChangeCardTime() * 5;
			curPlayer.setChangeCardTime(curPlayer.getChangeCardTime() + 1);
		}

		if (i.size() != 0 && a.size() != 0 && c.size() != 0) {
			i.pollFirst();
			a.pollFirst();
			c.pollFirst();
			armies = armies + curPlayer.getChangeCardTime() * 5;
			curPlayer.setChangeCardTime(curPlayer.getChangeCardTime() + 1);
		}

		String result = String.valueOf(armies);

		newlist.addAll(i);
		newlist.addAll(a);
		newlist.addAll(c);
		curPlayer.setCardList(newlist);

		if (i.size() >= 3 || a.size() >= 3 || c.size() >= 3 || (i.size() != 0 && a.size() != 0 && c.size() != 0)) {
			result = result + " " + "1";
//			setsChanged();
//			notifyObservers(this);
			return result;
		}

		result = result + " " + "0";
//		setChanged();
//		notifyObservers(this);
		return result;
	}
	/**
     * This method is to earnCard.
     *
     * @param player Current player.
     */
	public void earnCard(String player) {
		int card = (int) (1 + Math.random() * 3);
		Card c = new Card();
		LinkedList<Card> list = new LinkedList<>();
		switch (card) {
		case 1:

			// infantry
			c.setName("i");
			list = playerSet.get(player).getCardList();
			list.add(c);
			playerSet.get(player).setCardList(list);
			if (playerSet.get(player).getPlayerName().equals("Human")) {
				JOptionPane.showMessageDialog(null, "you got infantry card");
			}
			System.out.println(player+" you got infantry card");
			break;
		case 2:

			// cavalry
			c.setName("c");
			list = playerSet.get(player).getCardList();
			list.add(c);

			playerSet.get(player).setCardList(list);
			if (playerSet.get(player).getPlayerName().equals("Human")) {
			JOptionPane.showMessageDialog(null, "you got cavalry card");
			}
			System.out.println(player+" you got cavalry card");
			break;
		case 3:

			// artillery
			c.setName("a");
			list = playerSet.get(player).getCardList();
			list.add(c);

			playerSet.get(player).setCardList(list);
			if (playerSet.get(player).getPlayerName().equals("Human")) {
			JOptionPane.showMessageDialog(null, "you got artillery card");
			}
			System.out.println(player+" you got artillery card");
			break;

		}

		setChanged();
		notifyObservers(this);
	}

	/**
	 * The method judges these countries can transfer or not.
	 * 
	 * @param player The current player.
	 * @param s The start country.
	 * @param d The destination country.
	 * @return true if these countries can transfer.
	 */
	public boolean canTransfer(String player, String s, String d) {
		ArrayList<String> fromlist = new ArrayList<>();
		HashMap<String, String> flagcountry = new HashMap<>();
		flagcountry.put(s, s);
		fromlist.add(s);
		boolean result = findPath(player, fromlist, d,flagcountry);
		return result;

	}
	
	private boolean findPath(String player,ArrayList<String> from, String to,HashMap<String, String> flagcountry) {
		boolean result = false;
		ArrayList<String> templist = new ArrayList<>();
		for (int i = 0; i < from.size(); i++) {
			String[] countlist = countries.get(from.get(i)).getCountryList().split(" ");
			for (int j = 0; j < countlist.length; j++) {
				boolean isMatch = rightcountry(player, countlist[j]);
				if (isMatch && countlist[j].equals(to)) {
					return true;
				}
				else if (isMatch && !countlist[j].equals(to) &&  !flagcountry.containsKey(countlist[j])) {
					templist.add(countlist[j]);
					flagcountry.put(countlist[j], countlist[j]);
				}
			}
		}
		if (templist.size()!=0) {
		return	findPath(player, templist, to, flagcountry);
		}
		else {
			return result;
		}
	}


	/**
	 * The method checks whether current player click right country.
	 * 
	 * @param cplayer Current player.
	 * @param ccountry Current country.
	 * @return true if the player owns the country.
	 */
	public boolean rightcountry(String cplayer, String ccountry) {
		boolean match = false;
		LinkedList<Country> findCountries = playerSet.get(cplayer).getCountryList();
		for (Iterator<Country> iterator = findCountries.iterator(); iterator.hasNext();) {
			String s = String.valueOf(iterator.next().getName());
			if (ccountry.equals(s)) {
				match = true;
			}

		}
		return match;
	}

	
	public InitializePhase cheaterRein(String player, ArrayList<String> country){
		for (int i = 0; i < country.size(); i++) {
			int army = countries.get(country.get(i)).getArmy()*2;
			countries.get(country.get(i)).setArmy(army);
			LinkedList<Country> countrylist = playerSet.get(player).getCountryList();
			for (int j = 0; j < countrylist.size(); j++) {
				String couname = String.valueOf(countrylist.get(j).getName());
				if (country.equals(couname)) {
					countrylist.get(j).setArmy(army);
					break;
				}
			}
		}
//		setChanged();
//		notifyObservers(this);
		return this;
	}
	public InitializePhase cheaterAttack(HashMap<String, String> occupycou) {
		for (String key: occupycou.keySet()) {
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			String[] information = occupycou.get(key).split("_");
			String attaker = information[0];
			String defender = information[1];
			String country = key;
			Country c = countries.get(country);
			c.setColor(playerSet.get(attaker).getColor());
			LinkedList<Country> atlist = playerSet.get(attaker).getCountryList();
			atlist.add(c);
			playerSet.get(attaker).setCountryList(atlist);
			
			LinkedList<Country> deList = playerSet.get(defender).getCountryList();
			for (int j = 0; j < deList.size(); j++) {
				if (c.getName() == deList.get(j).getName()) {
					deList.remove(j);
					playerSet.get(defender).setCountryList(deList);
					break;
				}
			}
			
			if (playerSet.get(defender).getCountryList().size() == 0) {
				playerSet.remove(defender);
			}
		
		}
		return this;
	}
	
	public InitializePhase cheaterForti(String player, int country) {
		String cname = String.valueOf(country);
		//update countries
		int armies = countries.get(cname).getArmy()*2;
		countries.get(cname).setArmy(armies);
		
		//update playerSet countrylist 
		LinkedList<Country> list = playerSet.get(player).getCountryList();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName() == country) {
				list.get(i).setArmy(armies);
				break;
			}
		}
		setChanged();
		notifyObservers(this);
		return this;
	}
	/**
	 * This method modifies a signal for next turn.
	 * 
	 * @param signal 1 get into next turn and 0 stays in current turn.
	 */
	public void nextTurn(int signal) {
		if (signal == 0) {
			change = false;
		}
		else{
			change = true;
		}
		
		setChanged();
		notifyObservers(this);
	}

}

/**
 * <h1>ColorList</h1> This class defines color class, initialing all color
 * information.
 *
 * @author jiamin_he
 * @version 3.0
 * @since 2019-03-01
 */
class ColorList {

	private LinkedList<Color> colors = new LinkedList<>();

	/**
	 * This is a no-argument constructor of ColorList.
	 */
	public ColorList() {
		Color skyblue = new Color(119, 240, 228);
		Color lightgreen = new Color(91, 255, 120);
		Color ginger = new Color(217, 179, 64);
		Color pink = new Color(251, 132, 188);
		Color purple = new Color(182, 154, 228);
		colors.addLast(skyblue);
		colors.addLast(lightgreen);
		colors.addLast(ginger);
		colors.addLast(pink);
		colors.addLast(purple);
	}

	/**
	 * This method obtains a color list.
	 * 
	 * @return A linked list stores all color information.
	 */
	public LinkedList<Color> getColors() {
		return colors;
	}
}