package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.BillingInfo;
import model.TravelInfo;
import node.Node;

public class GUI {

	protected JFrame frame = null;
	protected JPanel buttons;
	private JLabel queueSizeLabel = null;
	private JTextField textField_id;
	private JTextField textField_origin;
	private JTextField textField_destination;
	private JTextField textField_tDate;
	private JTextField textField_rDate;
	private JTextField textField_nrOfPass;
	private JTextField textField_reservAccepted;
	private JTextField textField_ccNumber;
	private JTextField textField_ccOwner;
	private JTextField textField_billingName;
	private JTextField textField_billingAddress;
	private JTextField textField_deliveryAddress;
	private JTextField textField_inFullDischarge;
	private JButton nextBtn;
	
	
	public GUI(String title, Node node) {
		frame = new JFrame(title);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createGUI(node);

		setQueueSize(0);
		
		node.setGui(this);
		
		disable();
		
		//Display the window.
        frame.pack();
        frame.setVisible(true);
	}
	
	protected void createGUI(Node node) {
		Container container = frame.getContentPane();
		
		JPanel panel = null;
		JLabel label = null;
		
		queueSizeLabel = new JLabel();
		container.add(queueSizeLabel, BorderLayout.PAGE_START);
		
		/* Create TravelInfo view */
		panel = new JPanel();
		panel.setLayout(new GridLayout(7, 2));
		
		label = new JLabel("ID");
		textField_id = new JTextField(15);
		panel.add(label);
		panel.add(textField_id);
		
		label = new JLabel("Origin");
		textField_origin = new JTextField(15);
		panel.add(label);
		panel.add(textField_origin);

		label = new JLabel("Destination");
		textField_destination = new JTextField(15);
		panel.add(label);
		panel.add(textField_destination);

		label = new JLabel("Travel date");
		textField_tDate = new JTextField(15);
		panel.add(label);
		panel.add(textField_tDate);

		label = new JLabel("Return date");
		textField_rDate = new JTextField(15);
		panel.add(label);
		panel.add(textField_rDate);

		label = new JLabel("Number of pass.");
		textField_nrOfPass = new JTextField(15);
		panel.add(label);
		panel.add(textField_nrOfPass);

		label = new JLabel("Reservation accepted");
		textField_reservAccepted = new JTextField(15);
		panel.add(label);
		panel.add(textField_reservAccepted);
		
		container.add(panel, BorderLayout.LINE_START);
		/* ********************** */
		

		/* Create BillingInfo view */
		panel = new JPanel();
		panel.setLayout(new GridLayout(7, 2));
		
		label = new JLabel("Credit card owner");
		textField_ccOwner = new JTextField(15);
		panel.add(label);
		panel.add(textField_ccOwner);
		
		label = new JLabel("Credit card number");
		textField_ccNumber = new JTextField(15);
		panel.add(label);
		panel.add(textField_ccNumber);

		label = new JLabel("Billing name");
		textField_billingName = new JTextField(15);
		panel.add(label);
		panel.add(textField_billingName);

		label = new JLabel("Billing address");
		textField_billingAddress = new JTextField(15);
		panel.add(label);
		panel.add(textField_billingAddress);

		label = new JLabel("Delivery address");
		textField_deliveryAddress = new JTextField(15);
		panel.add(label);
		panel.add(textField_deliveryAddress);

		label = new JLabel("In full disc.");
		textField_inFullDischarge = new JTextField(15);
		panel.add(label);
		panel.add(textField_inFullDischarge);
		
		container.add(panel, BorderLayout.LINE_END);
		/* ********************** */
		
		buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		container.add(buttons, BorderLayout.PAGE_END);
		
		nextBtn = new JButton("Next");
		buttons.add(nextBtn);
		
		nextBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				node.next();
			}
		});
	}
	
	public void enable() {
		if (nextBtn != null) {
			nextBtn.setEnabled(true);
		}
	}
	
	public void disable() {
		if (nextBtn != null) {
			nextBtn.setEnabled(false);
		}
	}
	
	public void setQueueSize(int size) {
		EventQueue.invokeLater(() -> {
			if (queueSizeLabel != null) {
				queueSizeLabel.setText("Number of waiting tasks: " + size);
			}
		});
	}
	
	public void notify(TravelInfo ti, BillingInfo bi) {
		EventQueue.invokeLater(()->{
			if (ti != null) {
				textField_id.setText(ti.getId() + "");
				textField_origin.setText(ti.getOrigin());
				textField_destination.setText(ti.getDestination());
				textField_tDate.setText(ti.getTravelDate().toString());
				textField_rDate.setText(ti.getReturnDate().toString());
				textField_nrOfPass.setText(ti.getNrOfPassengers() + "");
				textField_reservAccepted.setText(ti.isReservationAccepted() + "");
			}
			
			if (bi != null) {
				textField_ccOwner.setText(bi.getCreditCardOwner());
				textField_ccNumber.setText(bi.getCreditCardNr());
				textField_billingName.setText(bi.getBillingName());
				textField_billingAddress.setText(bi.getBillingAddress());
				textField_deliveryAddress.setText(bi.getDeliveryAddress());
				textField_inFullDischarge.setText(bi.isInFullDischarge() + "");
			}
		});
	}
}
