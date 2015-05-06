package nodes;

import gui.GUI;
import gui.SMORGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.IntSupplier;

import javax.swing.JButton;

import util.StatCollector;
import model.BillingInfo;
import nodes.PaymentInfoNode.PaymentInfoNodeDone;
import channel.BaseMessage;
import channel.Channel;

public class SelectModeOfReciptNode extends AbstractNode {

	private boolean delivery;
	private BillingInfo billingInfo;
	private double dir;
	
	public SelectModeOfReciptNode(Channel channel, boolean automatic, IntSupplier delay, double dir) {
		super(channel, automatic, delay);
		this.dir = dir;
	}

	@Override
	protected void init() {
		channel.add(PaymentInfoNodeDone.class, msg -> {
			onMessageReceived(msg);
		});
	}

	@Override
	protected void processMessage(Object message)  {
		PaymentInfoNodeDone msg = (PaymentInfoNodeDone) message;
		id = msg.getId();
		gui.enable();
		
		billingInfo = msg.getBillingInfo();
		
		if(Math.random() > dir) 
			setReceiptDelivery();
		else 
			setReceiptInPersonal();

		gui.notify(null, billingInfo);
	}
	
	@Override
	public void next() {
		super.next();
		
		gui.disable();
		channel.broadcast(new SelectModeOfReciptDone(billingInfo, delivery, id));
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

	public static class SelectModeOfReciptDone extends BaseMessage {
		private boolean isDelivery;
		private BillingInfo billingInfo;

		public SelectModeOfReciptDone(BillingInfo billingInfo, boolean isDelivery, int id) {
			super();
			this.billingInfo = billingInfo;
			this.isDelivery = isDelivery;
			setId(id);
		}

		public boolean isDelivery() {
			return isDelivery;
		}
		
		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}
