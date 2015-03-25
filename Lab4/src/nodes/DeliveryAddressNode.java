package nodes;

import javax.jms.BytesMessage;
import javax.jms.Queue;
import javax.jms.QueueSender;

import model.BillingInfo;
import util.NodeBehavior;
import util.Serializer;

public class DeliveryAddressNode extends BasicAbstractNode {

	private BillingInfo billingInfo;
	
	public static String QUEUE_NAME = "queue/QUEUE_DAN";
	
	protected QueueSender sender_out;

	

	public DeliveryAddressNode() throws Exception {
		super();
	}

	@Override
	protected void init() throws Exception {
		Queue queue_in = (Queue) initialContext.lookup(SelectModeOfReciptNode.QUEUE_NAME);
		Queue queue_out = (Queue) initialContext.lookup(QUEUE_NAME);
		
		receiver = session.createReceiver(queue_in);
		
		sender_out = session.createSender(queue_out);
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();

		billingInfo = (BillingInfo) message;

		NodeBehavior.deliveryAddressBehavior(billingInfo);

		gui.notify(null, billingInfo);
	}	

	@Override
	public void next() throws Exception {
		gui.disable();

		BytesMessage message = session.createBytesMessage();
		message.writeBytes(Serializer.serialize(billingInfo));
		
		sender_out.send(message);
		
		synchronized (lock) {
			lock.notify();
		}
	}

	public static class DeliveryAddressDone {

		private BillingInfo billingInfo;

		public DeliveryAddressDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}