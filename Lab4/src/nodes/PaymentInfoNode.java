package nodes;

import java.util.Map;
import java.util.logging.Logger;

import javax.jms.BytesMessage;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;

import model.BillingInfo;
import model.TravelInfo;
import util.NodeBehavior;
import util.Serializer;

public class PaymentInfoNode extends JoinAbstractNode {
	private static final int KEY_TRAVEL_INFO = 0;
	private static final int KEY_BILLING_INFO = 1;
	
	private BillingInfo billingInfo;
	
	
	public static String QUEUE_NAME_SMORN = "queue/QUEUE_PIN_SMORN";
	public static String QUEUE_NAME_PPN = "queue/QUEUE_PIN_PPN";
	
	protected QueueSender sender_out_smorn;
	protected QueueSender sender_out_ppn;
	
	
	public PaymentInfoNode() throws Exception {
		super();
	}

	@Override
	public void next() throws Exception {
		synchronized (lock) {
			BytesMessage message = session.createBytesMessage();
			message.writeBytes(Serializer.serialize(billingInfo));
			
			sender_out_smorn.send(message);
			sender_out_ppn.send(message);
			
			billingInfo = null;
			
			if (queue.isEmpty()) {
				gui.disable();
			} else {
				processMessage(null);
			}
		}
	}

	public static class PaymentInfoNodeDone {
		private BillingInfo billingInfo;

		public PaymentInfoNodeDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}

	@Override
	protected void init() throws Exception {
		super.init();

		Queue queue_in_bin = (Queue) initialContext.lookup(BillingInfoNode.QUEUE_NAME);
		Queue queue_in_prn = (Queue) initialContext.lookup(ProcessReservationNode.QUEUE_NAME);
		Queue queue_out_smorn = (Queue) initialContext.lookup(QUEUE_NAME_SMORN);
		Queue queue_out_ppn = (Queue) initialContext.lookup(QUEUE_NAME_PPN);
		
		QueueReceiver receiver = null;
		
		receiver = session.createReceiver(queue_in_bin);
		receivers.add(receiver);
		receiverKeys.put(receiver, KEY_BILLING_INFO);
		
		receiver = session.createReceiver(queue_in_prn);
		receivers.add(receiver);
		receiverKeys.put(receiver, KEY_TRAVEL_INFO);
		
		sender_out_smorn = session.createSender(queue_out_smorn);
		sender_out_ppn = session.createSender(queue_out_ppn);
	}
	
	@Override
	protected void processMessage(Object message) {
		synchronized (lock) {
			if (billingInfo != null && !queue.isEmpty()) {
				return;
			}
			
			message = queue.poll();
			
			TravelInfo ti = null;
			BillingInfo bi = null;
			
			try {
				ti = (TravelInfo) ((Map<Integer, Object>) message).get(KEY_TRAVEL_INFO);
				bi = (BillingInfo) ((Map<Integer, Object>) message).get(KEY_BILLING_INFO);
	
				billingInfo = bi;				
				NodeBehavior.paymentInfoBehavior(billingInfo);
	
				gui.notify(null, billingInfo);
				gui.enable();
			} catch (Exception e) {
				Logger.getLogger(PaymentInfoNode.this.getClass().getSimpleName()).severe("An exception occured: " + e.getMessage());
			}
		}
	}
}
