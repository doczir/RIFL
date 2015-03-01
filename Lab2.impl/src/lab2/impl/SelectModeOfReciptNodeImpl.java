package lab2.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import lab2.inf.DeliveryAddressNode;
import lab2.inf.SelectModeOfReciptNode;

import commonosgi.gui.GUI;
import commonosgi.gui.SMORGUI;
import commonosgi.model.BillingInfo;

public class SelectModeOfReciptNodeImpl extends AbstractNode implements
		SelectModeOfReciptNode {

	private BillingInfo billingInfo;
	private boolean delivery;
	private DeliveryAddressNode dan;

	public SelectModeOfReciptNodeImpl() {
		super();

		new SMORGUI("Select mode of recipt", this);
	}

	@Override
	public void setGui(GUI gui) {
		super.setGui(gui);

		JButton receiptInPersonalBtn = ((SMORGUI) gui)
				.getReceiptInPersonalBtn();
		JButton receiptDeliveryBtn = ((SMORGUI) gui).getReceiptDeliveryBtn();

		receiptInPersonalBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setReceiptInPersonal();
			}
		});

		receiptDeliveryBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setReceiptDelivery();
			}
		});
	}

	public void setReceiptDelivery() {
		if (billingInfo != null) {
			delivery = true;

			billingInfo.setDeliveryAddress("-->");

			gui.notify(null, billingInfo);
		}
	}

	public void setReceiptInPersonal() {
		if (billingInfo != null) {
			delivery = false;

			billingInfo.setDeliveryAddress("-");

			gui.notify(null, billingInfo);
		}
	}

	@Override
	public void next() {
		if (delivery)
			dan.getDeliveryAddress(billingInfo);
		billingInfo = null;

		if (!queue.isEmpty()) {
			billingInfo = (BillingInfo) queue.poll();

			gui.notify(null, billingInfo);
			gui.enable();			
		} else {
			gui.disable();
		}
		
		gui.setQueueSize(queue.size());		
	}

	@Override
	public void selectModeOfRecipt(BillingInfo billingInfo) {
		if (queue.isEmpty() && this.billingInfo == null) {
			this.billingInfo = billingInfo;

			gui.notify(null, billingInfo);
			gui.enable();
		} else {
			queue.add(billingInfo);
		}
		
		gui.setQueueSize(queue.size());
	}

	public void setDAN(DeliveryAddressNode dan) {
		this.dan = dan;
	}

	public void unsetDAN(DeliveryAddressNode dan) {
		if (this.dan == dan)
			this.dan = null;
	}

}
