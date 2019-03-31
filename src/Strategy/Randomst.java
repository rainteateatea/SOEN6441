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

public class RandomSt implements BehaviorStrategy{
	
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
		
//		random get a country
		Random random = new Random();
		int index = random.nextInt(curPlayer.getCountryList().size());
		Country country = curPlayer.getCountryList().get(index);
		
		int armies = 0;
		String result = "";
		
//		check and change card
		result = b.changeCards(curPlayer);
		String[] infor = result.split(" ");
		armies = armies + Integer.parseInt(infor[0]);
		
		while(Integer.parseInt(infor[1]) == 1) {
			result = b.changeCards(curPlayer);
			infor = result.split(" ");
			armies = armies + Integer.parseInt(infor[0]);
		}
		
		armies = armies + observable.SystemArmy(playView.name.getText());
		armies = armies + observable.ContinentArmy(playView.name.getText());
		curPlayer.setArmy(armies);
		
		while(curPlayer.getArmy()!= 0) {
			observable.Startup(playView.name.getText(), toString().valueOf(country.getName()));
		}	
		
		if (curPlayer.getArmy() == 0) {
			boolean canAttack = b.canAttack(playView.name.getText());
			if (canAttack) {

				// enter attack phase
				JOptionPane.showMessageDialog(null, "enter attack phase");
				System.out.println("enter Attack phase");
				playView.currentPhase = "Attack";
				playView.phase.setText("Attack");
			} else {

				// cannot attack enter fortification phase
				JOptionPane.showMessageDialog(null, "you cannot attack,enter fortification phase");
				System.out.println("enter fortification phase");
				playView.phase.setText("Fortification");
				playView.currentPhase = "Fortification";
			}

		}
				
	}


	@Override
	public void attack(JLabel attcker, JLabel defender, InitializePhase observable, BackEnd b) {
		countries = observable.getCountries();
		continents = observable.getContinents();
		playerSet = observable.getPlayerSet();
		Player denPlayer = playerSet.get(b.findPlayer(defender.getName()));
		boolean capture = false;
		
		String canTransfer = "";
		
//		attack random times
		Random attack = new Random();
		while(attack.nextBoolean() && (countries.get(attcker.getName()).getArmy() != 1) && (!capture)) {
			String mode = "One_Time";
			int[] dices = findDicesNum(attcker.getName(), defender.getName());
			int att = dices[0];
			int def = dices[1];
			
			canTransfer = observable.attackPhase(attcker.getName(), defender.getName(), mode, att, def);
			
			if (!countries.get(defender.getName()).getColor().equals(denPlayer.getColor())) {
				capture = true;
			}
		}
		if (canTransfer != "") {
			Transfer(canTransfer, attcker, defender, observable, b);
		}
			
	}
	
	/**
	 * This method updates attack and defender JLabel.
	 *
	 * @param record A String shows transfer army information.
	 * @param att    A JLabel shows attack country army.
	 * @param def    A JLabel shows defender country army.
	 */
	public void Transfer(String record, JLabel att, JLabel def, InitializePhase observable, BackEnd b) {

		String[] readrecord = record.split(" ");
		if (readrecord[1].equals("0")) {// update countries information
			updateCountries(att);
			updateCountries(def);
			att.setBorder(null);
			def.setBorder(null);
			if (readrecord[0].equals(playView.name.getText())) {
				JOptionPane.showMessageDialog(null, "attacker Player" + playView.name.getText() + " win");
				if (!readrecord[2].equals("0")) {
					playView.WIN = true;
				}

			} else if (readrecord[0].equals("-1")) {

				JOptionPane.showMessageDialog(null, "This is a draw.");
			} else {
				String defender = b.findPlayer(def.getName());
				JOptionPane.showMessageDialog(null, "defender Player" + defender + " win");
			}

		} else {
			playView.WIN = true;
			updateCountries(att);
			updateCountries(def);

//			int move = b.moveArmies(Integer.valueOf(readrecord[1]), Integer.valueOf(readrecord[2]));
			
			int max = Integer.valueOf(readrecord[1]);
	        int min = Integer.valueOf(readrecord[2]);
	        Random random = new Random();
	        int move = random.nextInt(max)%(max-min+1) + min;
	       
			observable.Fortification(att.getName(), def.getName(), move);
			updateCountries(att);
			updateCountries(def);
			att.setBorder(null);
			def.setBorder(null);
		}
	}

	/**
	 * This method updates JLabel information of countries.
	 *
	 * @param label A JLabel shows a country information.
	 */
	public void updateCountries(JLabel label) {

		// update country army number
		String[] old = label.getText().split(" ");
		String now = old[0] + " " + countries.get(label.getName()).getArmy();
		label.setText(now);

		// update country color
		ImageIcon imageIcon = (ImageIcon) label.getIcon();
		Image image = imageIcon.getImage();

		BufferedImage img = (BufferedImage) image;
		int width = img.getWidth();
		int height = img.getHeight();

		WritableRaster raster = img.getRaster();
		for (int xx = 0; xx < width; xx++) {
			for (int yy = 0; yy < height; yy++) {
				int[] pixels = raster.getPixel(xx, yy, (int[]) null);
				pixels[0] = countries.get(label.getName()).getColor().getRed();
				pixels[1] = countries.get(label.getName()).getColor().getGreen();
				pixels[2] = countries.get(label.getName()).getColor().getBlue();
				raster.setPixel(xx, yy, pixels);
			}

		}

	}


	@Override
	public void fortification(JLabel from, JLabel c, String to, InitializePhase observable, BackEnd b) {
		
		
	}
	
	public String findAttDef(String player) {
		LinkedList<Country> checkList = new LinkedList<Country>(playerSet.get(player).getCountryList());
		String result = "";
		boolean att = false;
		boolean def = false;
		
		while(!att) {
			Random random = new Random();
			int index = random.nextInt(checkList.size());
			if (countries.get(toString().valueOf(checkList.get(index).getName())).getArmy() == 1) {
				checkList.remove(index);
			} else {
				
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
		}
		
		if (att && def) {
			
			return result;
			
		}
		
		return null;
	}
	
	private ArrayList<String> checkValidAtt(int index, LinkedList<Country> check, String player) {
		
		Country curCountry = check.get(index);
		String[] adj = curCountry.getCountryList().split(" ");
		ArrayList<String> list = new ArrayList<String>();
		
		for(String tmp : adj) {
			if (!countries.get(tmp).getColor().equals(curCountry.getColor())) {
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


}
