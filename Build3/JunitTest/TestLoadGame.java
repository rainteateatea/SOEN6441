import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Model.InitializePhase;
import Model.Message;
import View.InitGame.initPane;


/**
 * @author jiamin_he
 */
public class TestLoadGame {

	InitializePhase initPane = new InitializePhase();
	
	/**
	 * This method tests loading a game to succeed or not.
	 */
	@Test
	public void test() {
		
		String path = "LoadGame/TestSaveGame.game";
		initPane.loadGame(path);
		assertTrue(Message.isSuccess());
 	}

}
