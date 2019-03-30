

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ExampleAppStarter {

    public static void main(String [] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                new ExampleAppStarter().start();
            }
        });
    }

    private void start() {
	
        MainWindow m = new MainWindow();
    //    new DialogWindow(m);
    }
}

class MainWindow implements Observer {

    private JLabel label;
    private JButton button;
    private int i = 0;
	MessageObservable o = new MessageObservable();
	
	
    @Override // Observer interface's implemented method
    public void update(Observable o, Object data) {
	
       // label.setText((String) data); // displays new text in JLabel
    	button.setText((String) data);
    }
	
    MainWindow() {
    	o.addObserver(this);
	
        JFrame frame = new JFrame("Main Window");
        frame.getRootPane().setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20));		
       // label = new JLabel("Click button in Dialog...");
       // label.setFont(new Font("Dialog", Font.PLAIN, 20));	
        
        button = new JButton("num");
        
        button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
			String data = String.valueOf(i++);
			o.changeData(data);
			}
        	
		});
        frame.add(button);
       
        
      //  frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocation(200, 200);
        frame.setVisible(true);
        
    }
}

class DialogWindow {

    private int clicks;
	
    DialogWindow(MainWindow mainWindow) {
	
        // Create Observable and add Observer		
        final MessageObservable observable = new MessageObservable();
        observable.addObserver(mainWindow);
		
        // Display Dialog
        JFrame dialog = new JFrame("Dialog");		
        JButton button = new JButton("Press me");
        button.setFont(new Font("Dialog", Font.PLAIN, 20));
		
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String data = "button clicked in dialog [" + ++clicks + "]";
                observable.changeData(data);
            }
        });
		
        dialog.add(button);
        dialog.setSize(250, 150);
        dialog.setLocation(600, 200);
        dialog.setVisible(true);
    }
}

class MessageObservable extends Observable {

    MessageObservable() {
	
        super();
    }
    void changeData(Object data) {
	
        setChanged(); // the two methods of Observable class
        notifyObservers(data);
    }
}