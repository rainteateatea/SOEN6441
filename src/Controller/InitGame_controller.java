package Controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import Model.Checkmap;
import Model.Continent;
import Model.Country;
import Model.IO;
import Model.InitializePhase;
import Model.Message;
import Model.Player;
import View.InitGame;
import View.PlayView;

/**
 * <h1>InitGame_controller</h1> it is initGame controller to connect
 * InitializePharse_model, IO and initGame view
 * 
 * @author chenwei_song
 * @version 3.0
 * @since 2019-03-01
 */
public class InitGame_controller extends Object {

	IO iomodel;
	InitializePhase pharseModel;

	/**
	 * It is a constructor.
	 */
	public InitGame_controller() {
		iomodel = new IO();
		pharseModel = new InitializePhase();
	}

	/**
	 * It receive the player number and file path.
	 * 
	 * @param num The number of players.
	 * @param filePath File path.
	 */
	public void receive(int num, ArrayList<String> playList,String filePath) {

		// checkMap whether connected or not
		iomodel.readFile(filePath);
		HashMap<String, Country> countries = iomodel.getCountries();
		HashMap<String, Continent> continents = iomodel.getContinents();
		HashMap<String, Player> playerSet = new HashMap<>();
		Checkmap checkmap = new Checkmap(countries, continents);
		checkmap.judge();
		boolean result = Message.isSuccess();
		if (result) {
			pharseModel.addData(num, playList,countries, continents);
			
			pharseModel.initPhase(false);
			countries = pharseModel.getCountries();
			continents = pharseModel.getContinents();
			PlayView p = new PlayView();

			p.countries = countries;
			p.continents = continents;
			p.playerSet = pharseModel.getPlayerSet();
			playerSet = pharseModel.getPlayerSet();
			String fullname = playerSet.get("1").getPlayerName()+"_"+"1";
			PlayView.name.setText(fullname);
			PlayView.phase.setText("start up phase");
			PlayView.currentPhase = "start up";
			PlayView.mappath = filePath;
			
		} else {
			String error = Message.getMessage();
			JOptionPane.showConfirmDialog(null, error);
			new InitGame();
		}

	}
	
	public void continueGame(String filename) {
		String result = pharseModel.loadGame(filename);
		System.out.println(result);
		
		PlayView p = new PlayView();
		p.countries = pharseModel.getCountries();
		p.continents = pharseModel.getContinents();
		p.playerSet = pharseModel.getPlayerSet();
	
		String[] information = result.split(" ");
		PlayView.mappath = information[0].split("/")[1];
		String fullname = p.playerSet.get(information[1]).getPlayerName()+"_"+information[1];
		PlayView.name.setText(fullname);
		if (information.length == 3) {
			PlayView.phase.setText(information[2]);
			PlayView.currentPhase = information[2];
		}
		else {
//			PlayView.phase.setText(information[2]+" "+information[3]);
//			PlayView.currentPhase =information[3]+" "+information[3];
			PlayView.phase.setText("start up phase");
			PlayView.currentPhase = "start up";
		}

		
	}
}
