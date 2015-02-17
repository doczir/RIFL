package nodes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.GUI;
import gui.SMORGUI;

import javax.swing.JButton;

import model.BillingInfo;
import nodes.PaymentInfoNode.PaymentInfoNodeDone;
import channel.Channel;

public class SelectModeOfReciptNode extends AbstractNode {

	private boolean delivery;
	private BillingInfo billingInfo;

	
	public SelectModeOfReciptNode(Channel channel) {
		super(channel);
	}

	@Override
	protected void init() {
		channel.add(PaymentInfoNodeDone.class, msg -> {
			onMessageReceived(msg);
		});
	}

	@Override
	protected void processMessage(Object message) {
		PaymentInfoNodeDone msg = (PaymentInfoNodeDone) message;

		gui.enable();
		
		billingInfo = msg.getBillingInfo();

		gui.notify(null, billingInfo);
	}
	
	@Override
	public void next() {
		gui.disable();
		channel.broadcast(new SelectModeOfReciptDone(billingInfo, delivery));
		synchronized (lock) {
			lock.notify();
		}
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
	public void setGui(GUI gui) {
		super.setGui(gui);
		
		if (gui instanceof SMORGUI) {
			JButton receiptInPersonalBtn = ((SMORGUI) gui).getReceiptInPersonalBtn();
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
	}

	public static class SelectModeOfReciptDone {
		private boolean isDelivery;
		private BillingInfo billingInfo;

		public SelectModeOfReciptDone(BillingInfo billingInfo, boolean isDelivery) {
			super();
			this.billingInfo = billingInfo;
			this.isDelivery = isDelivery;
		}

		public boolean isDelivery() {
			return isDelivery;
		}
		
		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}
