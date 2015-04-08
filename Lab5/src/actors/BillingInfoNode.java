package actors;

import gui.GUI;
import akka.actor.Props;
import akka.japi.Creator;
import model.BillingInfo;
import util.MessageHelper;
import util.NodeBehavior;

public class BillingInfoNode extends AbstractNode {

	private BillingInfo billingInfo;

	public BillingInfoNode() {
		super();
		new GUI("BillingInfoNode", this);
	}
	
	@Override
	protected void internalProcessMessage(Object message) {
		gui.enable();

		billingInfo = new BillingInfo();
		NodeBehavior.billingInfoBehavior(billingInfo);

		gui.notify(null, billingInfo);
	}

	@Override
	public void next() {
		gui.disable();
		getContext().actorSelection(
				MessageHelper.getActorAddress(PaymentInfoNode.class)).tell(
				new BillingInfoNodeDone(billingInfo), self());
		super.next();
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

	public static Props props() {
		return Props.create(BillingInfoNode.class, BillingInfoNode::new);
	}
}
