import static org.junit.Assert.*;
import Model.Card;
import Model.InitializePhase;
import Model.Player;
import View.CardView;
import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

/**
 *<h1>TestCardView5</h1>
 * This class implements card view test.
 *
 * @author shuo_chi
 * @version 3.0
 * @since 2019-03-21
 */

import java.util.HashMap;
import java.util.LinkedList;

public class TestCardView5 {

    CardView cardView;
    InitializePhase model;
    LinkedList<String> diffCards;
    LinkedList<String> sameCards;
    LinkedList<Card> currCard;
    

   
    @Before
    /**
	 * This method initializes all objects for test.
	 *
	 * @throws IllegalArgumentException if diameter not in given range.
	 */
    public void setUp() throws Exception{

      	model = new InitializePhase();
    	cardView=new CardView(model);
        cardView.playerSet=new HashMap<>();
        cardView.boxes=new HashMap<>();
        cardView.boxes.put("0","i");
        cardView.boxes.put("1", "i");
        cardView.boxes.put("2", "i");
        cardView.cards=new LinkedList<>();
        
        Player player1=new Player("1");
        player1.setArmy(28);;
        player1.setColor(new Color(119, 240, 228));
       
        cardView.playerSet.put("1",player1);
        
        Card card1=new Card();
        card1.setName("i");
        Card card2=new Card();
        card2.setName("i");
        Card card3=new Card();
        card3.setName("i");
        Card card4=new Card();
        card4.setName("i");
        Card card5=new Card();
        card5.setName("i");
        Card card6=new Card();
        card6.setName("i");
        Card card7=new Card();
        card7.setName("c");
        Card card8=new Card();
        card8.setName("a");
        currCard=new LinkedList<>();
        currCard.add(card1);
        currCard.add(card2);
        currCard.add(card3);
        currCard.add(card4);
        currCard.add(card5);
        currCard.add(card6);
        currCard.add(card7);
        currCard.add(card8);
        
        sameCards=new LinkedList<>();
        sameCards.add("i");
        sameCards.add("i");
        sameCards.add("i");
        
        diffCards=new LinkedList<>();
        diffCards.add("i");
        diffCards.add("c");
        diffCards.add("a");
    }
        
	/**
	 * This method tests receive function.
	 * 
	 * Test case:
	 * "1":
	 * i-i-i-i-i-i-c-a
	 */     
    @Test
    public void testreceive() {  
      	cardView.receive("1", currCard);
      	int num=cardView.cards.size();
      	assertEquals(num, 8);
    }

	/**
	 * This method tests is same function.
	 * 
	 * Test case:
	 * i-i-i
	 */  
    @Test
    public void testIsSame() {
    	boolean flag=cardView.isSame(sameCards);
    	assertEquals(flag, true);
    }
    
	/**
	 * This method tests is different function.
	 * 
	 * Test case:
	 * i-c-a
	 */
    @Test
    public void testIsDifferent() {
    	boolean flag=cardView.isDifferent(diffCards);
    	assertEquals(flag, true);
    }  
    
	/**
	 * This method tests is delete function.
	 * 
	 * Test case:
	 * boxes: i-i-i
	 * card: i-i-i-i-i-i-c-a
	 */
    @Test
    public void testdelete() {
      	cardView.receive("1", currCard);
    	int numDelete=cardView.boxes.size();
      	int numberFirst=cardView.delete().size();
      	LinkedList<Card> reminder=cardView.delete();
      	
      	assertEquals(numDelete,3);
      	assertEquals(numberFirst, 5);
      	
      	cardView.receive("1", reminder);    	
      	int numberSecond=cardView.delete().size();
      	assertEquals(numberSecond, 2);
      	
    }
}
