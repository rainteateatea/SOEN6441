package Controller;

import java.util.ArrayList;

import Model.IO;
import Model.InitializePhase;

public class Tournament_controller {

	public void receive(ArrayList<String> mapList, ArrayList<String> playerList, String times, int turns) {
		
		InitializePhase  phase = new InitializePhase();
		phase.receive( mapList, playerList, times, turns);
		phase.receivemap(mapList);
	}
}
