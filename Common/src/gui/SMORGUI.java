package gui;

import javax.swing.JButton;

import node.Node;

public class SMORGUI extends GUI {
	
	private JButton receiptInPersonalBtn;
	private JButton receiptDeliveryBtn;
	
	public SMORGUI(String title, Node node) {
		super(title, node);
	}

	@Override
	protected void createGUI(Node node) {
		super.createGUI(node);
		
		receiptInPersonalBtn = new JButton("In personal");
		buttons.add(receiptInPersonalBtn);
		
		receiptDeliveryBtn = new JButton("Delivery");
		buttons.add(receiptDeliveryBtn);
	}
	
	@Override
	public void enable() {
		super.enable();
		
		receiptInPersonalBtn.setEnabled(true);
		receiptDeliveryBtn.setEnabled(true);
	}

	@Override
	public void disable() {
		super.disable();

		receiptInPersonalBtn.setEnabled(false);
		receiptDeliveryBtn.setEnabled(false);
	}

	public JButton getReceiptInPersonalBtn() {
		return receiptInPersonalBtn;
	}

	public JButton getReceiptDeliveryBtn() {
		return receiptDeliveryBtn;
	}
}
