package Strategy;

import javax.swing.JLabel;

import Model.InitializePhase;
import View.BackEnd;

public class Benevolent implements BehaviorStrategy{

	@Override
	public void reinforcemnet(JLabel c, String click,
			InitializePhase observable, BackEnd b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attack(JLabel from, JLabel to, InitializePhase observable,
			BackEnd b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fortification(JLabel from, JLabel c, String to,
			InitializePhase observable, BackEnd b) {
		// TODO Auto-generated method stub
		
	}

}
