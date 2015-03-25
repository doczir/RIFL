package nodes;

import javax.jms.BytesMessage;
import javax.jms.Queue;
import javax.jms.QueueSender;

import model.BillingInfo;
import util.NodeBehavior;
import util.Serializer;

public class BillingInfoNode extends BasicAbstractNode {
	private BillingInfo billingInfo;

	public static String QUEUE_NAME = "queue/QUEUE_BIN";
	
	protected QueueSender sender_out;
	
	public BillingInfoNode() throws Exception {
		super();
	}

	@Override
	protected void init() throws Exception {
		Queue queue_in = (Queue) initialContext.lookup(TravelInfoNode.QUEUE_NAME_BIN);
		Queue queue_out = (Queue) initialContext.lookup(QUEUE_NAME);
		
		receiver = session.createReceiver(queue_in);
		
		sender_out = session.createSender(queue_out);
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();

		billingInfo = new BillingInfo();
		NodeBehavior.billingInfoBehavior(billingInfo);

		gui.notify(null, billingInfo);
	}

	@Override
	public void next() throws Exception {
		gui.disable();
		
		BytesMessage message = session.createBytesMessage();

		if (lastMessage.getStringProperty(JoinAbstractNode.KEY_CORRELATION_ID) != null) {
			message.setStringProperty(JoinAbstractNode.KEY_CORRELATION_ID, lastMessage.getStringProperty(JoinAbstractNode.KEY_CORRELATION_ID));
		}

		message.writeBytes(Serializer.serialize(billingInfo));

		sender_out.send(message);
		
		synchronized (lock) {
			lock.notify();
		}
	}

	public static class BillingInfoNodeDone {
		private BillingInfo billingInfo;

		public BillingInfoNodeDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}
