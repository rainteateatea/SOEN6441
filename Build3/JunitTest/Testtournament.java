import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import Model.InitializePhase;
import View.InitGame.initPane;


/**
 * @author jiamin_he
 *
 */
public class Testtournament {

	InitializePhase initPhase = new InitializePhase();

	/**
	 * This method tests a tournament mode.
	 */
	@Test
	public void test() {
		String map = "world.map";
		ArrayList<String> mapList = new ArrayList<String>();
		mapList.add(map);
		ArrayList<String> playerList = new ArrayList<String>();
		playerList.add("Cheater");
		playerList.add("Aggressive");
		playerList.add("Random");
		playerList.add("Benevolent");
		int turns = 30;
		
		initPhase.receivemap(mapList);
		initPhase.receive(mapList, playerList, "1", turns);
		
		assertEquals("Cheater", initPhase.winnerlist.get(0));
	}

}
