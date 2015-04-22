package nodes;

import model.BillingInfo;
import nodes.PaymentInfoNode.PaymentInfoNodeDone;
import util.NodeBehavior;
import channel.BaseMessage;
import channel.Channel;

public class ProcessPaymentNode extends AbstractNode {
	
	private BillingInfo billingInfo;

	
	public ProcessPaymentNode(Channel channel) {
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
		id = msg.getId();
		gui.enable();
		billingInfo = msg.getBillingInfo();
		NodeBehavior.processPaymentBehavior(billingInfo);
		gui.notify(null, billingInfo);
	}	

	@Override
	public void next() {
		gui.disable();
		channel.broadcast(new ProcessPaymentNodeDone(billingInfo, id));
		synchronized (lock) {
			lock.notify();		
		}	
	}
	
	public static class ProcessPaymentNodeDone extends BaseMessage {
		private BillingInfo billingInfo;

		public ProcessPaymentNodeDone(BillingInfo billingInfo, int id) {
			super();
			this.billingInfo = billingInfo;
			setId(id);
		}
		
		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}
