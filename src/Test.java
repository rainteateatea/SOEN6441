import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import View.PlayView;


public class Test {
	public static HashMap<Integer, Integer> h = new HashMap<>();
	
	
	public static void main(String args[]) {
		
//		final Timer timer = new Timer();
//	    TimerTask task = new TimerTask() {
//	        private int count;
//	  
//	        @Override
//	        public void run() {
//	            this.count++;
//	            System.out.println(count);
//	            if (count == 10) {
//	                System.out.println("定时器停止了");
//	                timer.cancel();// 停止定时器
//	            }
//	        }
//	    };
//	    timer.schedule(task, 0, 1000);// 1秒一次
		  System.out.println("@@@");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("!!!!");
		
//		File file = new File("mapfile/");
//		File[] files = file.listFiles();
//		ArrayList<String> listfile = new ArrayList<>();
//		
//		for (int i = 0; i < files.length; i++) {
//			
//			System.out.println(files[i]);
//		}

//		
//		LinkedList<String> list = new LinkedList<>();
//		list.add("a");
//		list.add("b");
//		list.add("c");
//		ArrayList<String> card = new ArrayList<>();
//		card.add("b");
//		card.add("c");
//		card.add("a");
//		
//		for (int i = 0; i < card.size(); i++) {
//			for (int j = 0; j < list.size(); j++) {
//				if (card.get(i).equals(list.get(j))) {
//					card.remove(i);
//					i =0;
//					break;
//				}
//			}
//		}
//		for (int i = 0; i < card.size(); i++) {
//			System.out.println("##"+card.get(i));
//		}
//		
//		int[] a = new int[5];
//		int mid = 3;
//		int temp =1;
//		for (int i = 0; i < a.length; i++) {
//			if (temp == a.length+1) {
//				temp =1;
//			}
//			a[i] = temp;
//			temp++;
//			
//		}
//		for (int i = 0; i < a.length; i++) {
//			System.out.println(a[i]);
//		}
//		LinkedList<String> list = new LinkedList<>();
//		list.add("a");
//		list.add("b");
//		list.add("c");
//		list.add("d");
//		list.add("b");
//		list.add("c");
//		list.add("a");
//		list.add("a");
//		list.add("a");
//	 
//		System.out.println("\n例子 1 -统计'a'出现的频率");
//		System.out.println("a : " + Collections.frequency(list, "a"));
//		
//		 JFrame frame = new JFrame("Example of 2 panels");
//	      JPanel  containerPane = new JPanel();
//	       JPanel topPane = new JPanel();
//	       JPanel bottomPane = new JPanel();
//
//	        containerPane.setLayout(new GridLayout(2, 1));
//	        topPane.setLayout(new GridLayout(1, 2));
//	        bottomPane.setLayout(new BorderLayout());
//
//	        topPane.add(new JLabel("Left side"));
//	        topPane.add(new JLabel("Right side"));
//
//	        bottomPane.add(new JLabel("Left side"), BorderLayout.WEST);
//	        bottomPane.add(new JLabel("Right side"), BorderLayout.EAST);
//
//	        topPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Using GridLayout"));
//	        bottomPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "Using BorderLayout"));
//
//	        containerPane.add(topPane);
//	        containerPane.add(bottomPane);
//
//	        frame.add(containerPane);

//	      frame.pack();
//	        frame.setSize(500, 400);
//	        frame.setVisible(true);
//	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//		final JFrame frame = new JFrame();
//	
//
//		//card 挑选卡片换卡界面
//	    JPanel myPanel = new JPanel();
//	   
//	   frame.setUndecorated(true);
//	    JCheckBox jcb1 = new JCheckBox("card1");// 定义一个复选框
//	  	JCheckBox jcb2 = new JCheckBox("card 2");// 定义一个复选框
//	  	JCheckBox jcb3 = new JCheckBox("card 3");// 定义一个复选框
//	  	jcb1.setSelected(false);// 复选框状态为 未选择
//	  	jcb1.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});;
//	  	
//	 
//	  	myPanel.add(jcb1);
//	  	myPanel.add(jcb2);
//	  	myPanel.add(jcb3);
//	  	frame.add(myPanel);
//	int result = JOptionPane.showConfirmDialog(null,myPanel, "!!!", JOptionPane.DEFAULT_OPTION);
	//     int result = JOptionPane.showConfirmDialog(null, myPanel);

//	      if (result == JOptionPane.OK_OPTION ) {
//	    	   
//		         System.out.println("card value: " + jcb1.isSelected() +" "+ jcb1.getText());
//		        
//		      }
	      
	     
//		for (int i = 0; i < 20; i++) {
//			int j = (int)(0+Math.random()*7);
//			System.out.println(j);
//		}
		
//		 attack 输入攻 守双方 骰子数界面
//	      JPanel myPanel = new JPanel();
//		  JTextField xField = new JTextField(5);
//	      JTextField yField = new JTextField(5);
//	      myPanel.add(new JLabel("攻:"));
//	      myPanel.add(xField);
//	      myPanel.add(Box.createHorizontalStrut(15)); // a spacer
//	      myPanel.add(new JLabel("守:"));
//	      myPanel.add(yField);
//	      JButton button = new JButton("22");
//	 //     button.setEnabled(false);
//	      button.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				System.out.println("222");
//			}
//		});
//	      myPanel.add(button);
//	      int result = JOptionPane.showConfirmDialog(null,myPanel,null, JOptionPane.OK_OPTION);
//	      if (result == JOptionPane.OK_OPTION && !xField.getText().isEmpty()&&!yField.getText().isEmpty()) {
//	         System.out.println("x value: " + xField.getText());
//	         System.out.println("y value: " + yField.getText());
//	      }
//	      
		
		
//		h.put(1, 1);
//		h.put(2, 2);
//		h.put(4, 4);
//		h.put(7, 7);
//		int j = next(7);
//	//	System.out.println(j);
//		
//	}
//	public static int next(int i){
//		int a = i+1;
//		if (i == 7) {
//			a=1;
//		}
//		if (h.containsKey(a)) {
//			return a;
//		}
//		else {
//			return next(a);
//		}
//	}
		
//		JPanel myPanel = new JPanel();
//	
//		myPanel.setLayout(new GridLayout(3, 4));
//		JLabel attackerLabel = new JLabel("Attacker");
//		myPanel.add(attackerLabel);
//		//JTextField attackerField = new JTextField();
//		ButtonGroup g = new ButtonGroup();
//		for (int i = 1; i < 4; i++) {
//			JRadioButton button = new JRadioButton(String.valueOf(i));
//			myPanel.add(button);
//			g.add(button);
//		}
//		JLabel defender = new JLabel("Defender");
//		myPanel.add(defender);
//		ButtonGroup grou = new ButtonGroup();
//		for (int i = 1; i < 4; i++) {
//			JRadioButton button = new JRadioButton(String.valueOf(i));
//			myPanel.add(button);
//			grou.add(button);
//		}
//	//	JTextField defenderField = new JTextField();
//		JRadioButton allOutButton = new JRadioButton("All_Out");
//		allOutButton.setName("all");
//		JRadioButton oneTimeButton = new JRadioButton("One_Time");
//		oneTimeButton.setName("one");
//	
//	//	myPanel.add(attackerField);
//	
//	//	myPanel.add(defenderField);
//		myPanel.add(allOutButton);
//		myPanel.add(oneTimeButton);
//
//		ButtonGroup group = new ButtonGroup();
//		group.add(allOutButton);
//		group.add(oneTimeButton);
//		
//		
//	
//		int result = JOptionPane.showConfirmDialog(null, myPanel,
//				"Please select number of dices", JOptionPane.OK_CANCEL_OPTION);
//		for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
//            AbstractButton button = buttons.nextElement();
//
//            if (button.isSelected()) {
//               System.out.println(button.getText());
//               System.out.println(result);
//            }
//        }
		
	//	System.out.println(j.getName());
		
//		JOptionPane.showMessageDialog(null, "!!!","》》",JOptionPane.ERROR_MESSAGE);
//      JDialog dialog = new JDialog(frame, true);
//		
//	   //   frame.setSize(300, 300);
//	 
//		JPanel panel = new JPanel();
//		panel.setSize(300, 300);
//		frame.add(panel);
//		int FPS_MIN = 0;
//		int FPS_MAX = 30;
//		int FPS_INIT = 15;    //initial frames per second
//		
//		 final JLabel sliderLabel = new JLabel("armies            ", JLabel.CENTER);
//	        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//	        final JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL,
//                    FPS_MIN, FPS_MAX, FPS_INIT);
//	       
//	      //  framesPerSecond.setMajorTickSpacing(5);
//	        framesPerSecond.setMinorTickSpacing(1);
//	        framesPerSecond.setPaintTicks(true);
//	        framesPerSecond.setPaintLabels(true);
//	        framesPerSecond.setBorder(
//	                BorderFactory.createEmptyBorder(0,0,10,0));
//	        Font font = new Font("Serif", Font.ITALIC, 15);
//	        framesPerSecond.setFont(font);
//	        framesPerSecond.addChangeListener(new ChangeListener() {
//				
//				@Override
//				public void stateChanged(ChangeEvent e) {
//					// TODO Auto-generated method stub
//					 JSlider source = (JSlider)e.getSource();
//					 int fps = (int)source.getValue();
//					 sliderLabel.setText("armies:"+String.valueOf(fps));
//					 
//				}
//			});
//	        
//	        panel.add(sliderLabel);
//	        panel.add(framesPerSecond);
//	     
//	      JButton button = new JButton("YES");
//	      button.setBounds(150, 150, 25, 30);
//	 //    sliderLabel.setBounds(10, 10, 100, 100);
//	     panel.add(button);
//	   //   frame.setSize(300, 300);
//	     
//	      button.addActionListener(new ActionListener() {
//		
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println( framesPerSecond.getValue());
//				frame.dispose();
//			}
//		});
//	      dialog.add(panel);
//	      dialog.setLocationRelativeTo(null);
//	      dialog.setUndecorated(true);
//	      dialog.setSize(500, 100);
//	 //     dialog.pack();
//	      dialog.setVisible(true);
//	      System.out.println( "@@@"+framesPerSecond.getValue());
	    //  frame.add(panel);
	     
//			int result = JOptionPane.showConfirmDialog(null, panel,
//			"Please select how many armies you want to move", JOptionPane.OK_OPTION);
//			if (result == 0) {
//				System.out.println( framesPerSecond.getValue());
//			}
	//      frame.pack();
	//      frame.setVisible(true);
	
	      
}
	
}
