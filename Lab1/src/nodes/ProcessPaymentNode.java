package nodes;

import model.BillingInfo;
import nodes.PaymentInfoNode.PaymentInfoNodeDone;
import util.NodeBehavior;
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
		
		gui.enable();
		billingInfo = msg.getBillingInfo();
		NodeBehavior.processPaymentBehavior(billingInfo);
		gui.notify(null, billingInfo);
	}	

	@Override
	public void next() {
		gui.disable();
		channel.broadcast(new ProcessPaymentNodeDone(billingInfo));
		synchronized (lock) {
			lock.notify();		
		}	
	}
	
	public static class ProcessPaymentNodeDone {
		private BillingInfo billingInfo;

		public ProcessPaymentNodeDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}
		
		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}
