package actors;

import gui.GUI;
import model.BillingInfo;
import util.NodeBehavior;
import actors.PaymentInfoNode.PaymentInfoNodeDone;
import akka.actor.Props;

public class ProcessPaymentNode extends AbstractNode {

	private BillingInfo billingInfo;

	public ProcessPaymentNode() {
		super();
		new GUI("ProcessPaymentNode", this);
	}

	@Override
	protected void internalProcessMessage(Object message) {
		PaymentInfoNodeDone msg = (PaymentInfoNodeDone) message;

		gui.enable();
		billingInfo = msg.getBillingInfo();
		NodeBehavior.processPaymentBehavior(billingInfo);
		gui.notify(null, billingInfo);
	}

	@Override
	public void next() {
		gui.disable();
		super.next();
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

	public static Props props() {
		return Props.create(ProcessPaymentNode.class, ProcessPaymentNode::new);
	}
}
