package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI {

	private JFrame frame = null;
	
	
	GUI(String title, Node node) {
		frame = new JFrame(title);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container container = frame.getContentPane();
		
		JPanel panel = null;
		JLabel label = null;
		JTextField textField = null;
		
		/* Create TravelInfo view */
		panel = new JPanel();
		panel.setLayout(new GridLayout(7, 2));
		
		label = new JLabel("ID");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);
		
		label = new JLabel("Origin");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);

		label = new JLabel("Destination");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);

		label = new JLabel("Travel date");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);

		label = new JLabel("Return date");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);

		label = new JLabel("Number of pass.");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);

		label = new JLabel("Reservation accepted");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);
		
		container.add(panel, BorderLayout.LINE_START);
		/* ********************** */
		

		/* Create BillingInfo view */
		panel = new JPanel();
		panel.setLayout(new GridLayout(7, 2));
		
		label = new JLabel("Credit card owner");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);
		
		label = new JLabel("Credit card number");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);

		label = new JLabel("Billing name");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);

		label = new JLabel("Billing address");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);

		label = new JLabel("Delivery address");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);

		label = new JLabel("In full disc.");
		textField = new JTextField();
		panel.add(label);
		panel.add(textField);
		
		container.add(panel, BorderLayout.LINE_END);
		/* ********************** */
		
		JButton nextBtn = new JButton("Next");
		container.add(nextBtn, BorderLayout.PAGE_END);
		
		//Display the window.
        frame.pack();
        frame.setVisible(true);
	}
	
	public void enable() {
		if (frame != null) {
			frame.setEnabled(true);
		}
	}
	
	public void disable() {
		if (frame != null) {
			frame.setEnabled(false);
		}
	}
	
	public void notify(TravelInfo ti, BillingInfo bi) {
		// TODO
	}
	
	
}
