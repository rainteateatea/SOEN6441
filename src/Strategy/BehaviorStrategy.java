package Strategy;

import javax.swing.JLabel;

import Model.InitializePhase;
import View.BackEnd;

public interface BehaviorStrategy {
	void reinforcemnet(JLabel c, String click,
			InitializePhase observable,BackEnd b);
	
	void attack(JLabel from , JLabel to , 
			InitializePhase observable, BackEnd b);
	
	void fortification(JLabel from, JLabel c, String to,InitializePhase observable, BackEnd b);

}