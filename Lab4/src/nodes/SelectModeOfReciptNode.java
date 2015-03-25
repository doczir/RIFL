package nodes;

import gui.GUI;
import gui.SMORGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.jms.BytesMessage;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.swing.JButton;

import model.BillingInfo;
import util.Serializer;

public class SelectModeOfReciptNode extends BasicAbstractNode {

	private boolean delivery;
	private BillingInfo billingInfo;

	
	public static String QUEUE_NAME =  "queue/QUEUE_SMORN";
	
	protected QueueSender sender_out;
	
	
	public SelectModeOfReciptNode() throws Exception {
		super();
	}

	@Override
	protected void init() throws Exception {
		Queue queue_in = (Queue) initialContext.lookup(PaymentInfoNode.QUEUE_NAME_SMORN);
		Queue queue_out = (Queue) initialContext.lookup(QUEUE_NAME);
		
		receiver = session.createReceiver(queue_in);
		
		sender_out = session.createSender(queue_out);
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();
		
		billingInfo = (BillingInfo) message;

		gui.notify(null, billingInfo);
	}
	
	@Override
	public void next() throws Exception {
		gui.disable();

		if (delivery) {
			BytesMessage message = session.createBytesMessage();
			message.writeBytes(Serializer.serialize(billingInfo));
			
			sender_out.send(message);
		}
		
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
}
