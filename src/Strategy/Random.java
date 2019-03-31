package Strategy;

import java.util.HashMap;

import javax.swing.JLabel;

import Model.Continent;
import Model.Country;
import Model.InitializePhase;
import Model.Player;
import View.BackEnd;
import View.PlayView;
import View.CardView.CardPane.checkCard;

public class Random implements BehaviorStrategy{
	
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
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		
		Player curPlayer = playerSet.get(playView.name.getText());
		
		int armies = 0;
		String result;
//		check and change card
		result = b.changeCards(curPlayer);
		String[] infor = result.split(" ");
		armies = armies + Integer.parseInt(infor[0]);
		
		if (Integer.parseInt(infor[1]) == 1) {
			
		}
	
		
		
				
	}

	@Override
	public void attack(JLabel from, JLabel to, InitializePhase observable, BackEnd b) {
		
		
	}

	@Override
	public void fortification(JLabel from, JLabel c, String to, InitializePhase observable, BackEnd b) {
		
		
	}
	

}
