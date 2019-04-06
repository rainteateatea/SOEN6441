package View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Player;

public class TourMode implements Observer{
	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	
	InitializePhase observable  = new InitializePhase();
	BackEnd b;
	
	@Override
	public void update(Observable obs, Object x) {
		countries = ((InitializePhase) obs).getCountries();
		continents = ((InitializePhase) obs).getContinents();
		playerSet = ((InitializePhase) obs).getPlayerSet();
		
	}
	
	public TourMode( HashMap<String, Country> Countries, HashMap<String, Continent> Continents, HashMap<String, Player> PlayerSet) {
		System.out.println(countries.size());
		countries = Countries;
		continents = Continents;
		playerSet = PlayerSet;
		b = new BackEnd();
		observable.addObserver(b);
		observable.addObserver(this);
		new Tour();
	//	startup("1");
	}
	
	
	public class Tour {
		public Tour() {
			System.out.println(countries.size());
			observable.setCountries(countries);
			observable.setContinents(continents);
			observable.setPlayerSet(playerSet);
			startup("1");
		}
		
		public void startup(String player) {
			String fullname = playerSet.get(player).getPlayerName()+"_"+player;
			String c = randomCountry(player);
			System.out.println(fullname+"enter start up and choose country "+c);
			observable.Startup(player, c);
			//update player
			String nextP = b.nextplayer(player);
			if (nextP == "") {
				//enter reinforcement phase
				System.out.println(fullname+"enter reinforcement phase");
				playerSet.get("1").reinforcement(null,"1", observable, b);
			}
			else {
				startup(nextP);
			}
		}
		private String randomCountry(String player) {
			LinkedList<Country> list = playerSet.get(player).getCountryList();
			int n = (int)(0+Math.random()*list.size());
			int cou = list.get(n).getName();
			return String.valueOf(cou);
			
		}
	}

}
