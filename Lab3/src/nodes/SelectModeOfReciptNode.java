package nodes;

import gui.GUI;
import gui.SMORGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

import util.Serializer;

import com.rabbitmq.client.QueueingConsumer;

import model.BillingInfo;
import nodes.PaymentInfoNode.PaymentInfoNodeDone;

public class SelectModeOfReciptNode extends AbstractNode {

	private boolean delivery;
	private BillingInfo billingInfo;

	
	public static String EXCHANGE_NAME =  "EXCHANGE_SMOR";
	
	
	public SelectModeOfReciptNode() throws IOException {
		super();
	}

	@Override
	protected void init() throws IOException {
		channel.exchangeDeclare(PaymentInfoNode.EXCHANGE_NAME, "fanout");

		String queueName = channel.queueDeclare().getQueue();
		
		channel.queueBind(queueName, PaymentInfoNode.EXCHANGE_NAME, "");

		QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        consumers.add(consumer);
        
        
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");		
		
//		channel.add(PaymentInfoNodeDone.class, msg -> {
//			onMessageReceived(msg);
//		});
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();
		
		billingInfo = (BillingInfo) message;

		gui.notify(null, billingInfo);
	}
	
	@Override
	public void next() throws IOException {
		gui.disable();
//		channel.broadcast(new SelectModeOfReciptDone(billingInfo, delivery));
		
		channel.basicPublish(EXCHANGE_NAME, "", null, Serializer.serialize(billingInfo));
		
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
