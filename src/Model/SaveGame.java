package Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SaveGame {

	public String gamePath = "mapfile/";
	public HashMap<String, Country> countries = new HashMap<>();
	public HashMap<String, Continent> continents = new HashMap<>();
	public HashMap<String, Player> playerSet = new HashMap<>();
	/**
	 * This method implements to save map.
	 * 
	 * @param fileName      The file path.
	 * @param currentPlayer Current Player index like 1,2,3...
	 * @param currentPhase  Current Phase.
	 * @return Message of saving game.
	 */
	public void saveGame(String fileName, String currentPlayer, String currentPhase, HashMap<String, Country> Countries, HashMap<String, Continent> Continents, HashMap<String, Player> PlayerSet) { // file name = file path like
																						// ---"/6441/world.game"

		countries = Countries;
		continents = Continents;
		playerSet = PlayerSet;
		//		if the file exists, then delete and create a new file with same name
		String filePath = gamePath + fileName + ".game";
		File file = new File(filePath);

		if (file.exists()) {
			file.delete();
		}

		if (continents.isEmpty() || countries.isEmpty() || playerSet.isEmpty()) {
			Message.setSuccess(false);
			Message.setMessage("Invalid data！");
			return;

		} else {
		
			String image = fileName + ".bmp";
			String wrap = "yes";
			String scroll = "none";
			String author = "Soen6441_team_25-B";
			String warn = "yes";

			String header_1 = "[Game]\nimage=" + image + "\nwrap=" + wrap + "\nscroll=" + scroll + "\nauthor=" + author
					+ "\nwarn=" + warn + "\n\n";
			String header_2 = "[Map]\n" + filePath + "\n\n";
			String header_3 = "[Countires]\n";
			String header_4 = "[Players]\n";
			String header_5 = "[CurrentPlayer]\n" + currentPlayer + "\n\n";
			String header_6 = "[Phase]\n" + currentPhase;
			String blankLine = "\n";
			String comma = ",";

			if (!file.exists()) {
				try {
					file.createNewFile();
					FileWriter writer = new FileWriter(filePath, true);
					writer.write(header_1);
					writer.write(header_2);
					writer.write(header_3);

//					this is countries section, record name, color, armies;
					for (Map.Entry<String, Country> countEntry : countries.entrySet()) {
						writer.write(countryInfo(countEntry.getKey()));
						writer.write(blankLine);
					}

					writer.write(blankLine);

//					this is players' section;
					writer.write(header_4);
					for (Map.Entry<String, Player> playerEntry : playerSet.entrySet()) {
						writer.write(playerInfo(playerEntry.getKey()));
						writer.write(blankLine);
					}

					writer.write(blankLine);

					writer.write(header_5);

					if (currentPhase == "Attack") {
						header_6 = header_6 + comma + Attack.isHasCard() + "\n";
						writer.write(header_6);
					} else {
						writer.write(header_6);
						writer.write(blankLine);

					}

					writer.close();
					Message.setSuccess(true);
					Message.setMessage("Success saving Game!");
					return;

				} catch (IOException e) {
					e.printStackTrace();
					Message.setSuccess(false);
					Message.setMessage("Exist an IO exception");
					return;
				}
			}

		}
		Message.setSuccess(false);
		Message.setMessage("Never exist any data in Game file!");
		return;
	}
	/**
	 * This method combine country's information.
	 * 
	 * @param name Country name.
	 * @return All information of country.
	 */
	private String countryInfo(String name) {
		Country country = this.countries.get(name);
		String info = "";
		info = name + ",";
	//	info += String.valueOf(cList.getColors().indexOf(country.getColor())) + ",";
		info += String.valueOf(country.getArmy());
		return info;
	}

	/**
	 * This method combine player's information.
	 * 
	 * @param index The index of player.
	 * @return All information of player.
	 */
	private String playerInfo(String index) {
		Player player = this.playerSet.get(index);
		String info = index + ",";
		info += player.getPlayerName() + ",";
		info += String.valueOf(player.getArmy()) + ",";
	//	info += String.valueOf(cList.getColors().indexOf(player.getColor())) + ",";
		info += String.valueOf(player.getChangeCardTime());

		for (Card card : player.getCardList()) {
			info = "," + card.getName();
		}

		return info;
	}

	/**
	 * This method reads map file.
	 * 
	 * @return true if it is succeed, otherwise false.
	 */
}
