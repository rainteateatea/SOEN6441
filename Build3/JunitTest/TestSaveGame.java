import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.naming.spi.DirStateFactory.Result;

import org.junit.Before;
import org.junit.Test;

import Model.Attack;
import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Message;
import Model.Player;
import View.BackEnd;
import View.InitGame.initPane;


/**
 * @author jiamin_he
 *s
 */
public class TestSaveGame {

	HashMap<String, Player> playerSet;
	HashMap<String, Country> countries;
	HashMap<String, Continent> continents;
	BackEnd backEnd;
	Attack attack;
	Player player1;
	Player player2;
	Continent continentA;
	Continent continentB;
	Country country1;
	Country country2;
	Country country3;
	Country country4;
	InitializePhase initPhase;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		this.playerSet = new HashMap<String, Player>();
		this.countries = new HashMap<String, Country>();
		this.continents = new HashMap<String, Continent>();
		
		this.player1 = new Player("Human");
		player1.setColor(new Color(119, 240, 228));
		this.player2 = new Player("Aggressive");
		player2.setColor(new Color(91, 255, 120));
		
		this.continentA = new Continent();
		continentA.setName("A");
		this.continentB = new Continent();
		continentB.setName("B");
		
		this.country1 = new Country();
		this.country2 = new Country();
		this.country3 = new Country();
		this.country4 = new Country();
		country1.setName(1);// set name
		country2.setName(2);
		country3.setName(3);
		country4.setName(4);
		country1.setColor(new Color(119, 240, 228));// set color
		country2.setColor(new Color(119, 240, 228));
		country3.setColor(new Color(91, 255, 120));
		country4.setColor(new Color(91, 255, 120));
		country1.setContinent("A");// set continent
		country2.setContinent("A");
		country3.setContinent("B");
		country4.setContinent("B");
		String str1 = "2 3";// set countryList
		country1.setCountryList(str1);
		String str2 = "1 4";
		country2.setCountryList(str2);
		String str3 = "1 4";
		country3.setCountryList(str3);
		String str4 = "2 3";
		country4.setCountryList(str4);
		
		ArrayList<String> l1 = new ArrayList<String>();// set countryList in continent
		l1.add("1");
		l1.add("2");
		continentA.setCountryList(l1);
		ArrayList<String> l2 = new ArrayList<String>();
		l2.add("3");
		l2.add("4");
		continentB.setCountryList(l2);
		
		LinkedList<Country> l3 = new LinkedList<Country>();
		l3.add(country1);
		l3.add(country2);
		player1.setCountryList(l3);
		LinkedList<Country> l4 = new LinkedList<Country>();
		l4.add(country3);
		l4.add(country4);
		player2.setCountryList(l4);
		
		playerSet.put("1", player1);
		playerSet.put("2", player2);
		
		continents.put(continentA.getName(), continentA);
		continents.put(continentB.getName(), continentB);
		
		countries.put(String.valueOf(country1.getName()), country1);
		countries.put(String.valueOf(country2.getName()), country2);
		countries.put(String.valueOf(country3.getName()), country3);
		countries.put(String.valueOf(country4.getName()), country4);
		
		initPhase = new InitializePhase();
		
		
		//game map
		//plater1 1(A)-2(B)
		//        |    |
		//player2 3(A)-4(B)
		
	}

	/**
	 * This method tests save game to succeed ot not.
	 */
	@Test
	public void test() {
		
		initPhase.addData(2, null, this.countries, this.continents);
		initPhase.setPlayerSet(this.playerSet);
		
		String mapPaht = "mapfile/Save_Load.map";
		
		initPhase.saveGame("TestSaveGame", mapPaht, "1", "Attack");
				
		assertTrue(Message.isSuccess());
	}

}
