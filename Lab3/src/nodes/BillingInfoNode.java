package nodes;

import java.io.IOException;

import model.BillingInfo;
import util.NodeBehavior;

public class BillingInfoNode extends AbstractNode {
	private BillingInfo billingInfo;

	public BillingInfoNode() throws IOException {
		super();
	}

	@Override
	protected void init() {
		// channel.add(TravelInfoNodeDone.class, msg -> {
		// onMessageReceived(msg);
		// });
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();

		billingInfo = new BillingInfo();
		NodeBehavior.billingInfoBehavior(billingInfo);

		gui.notify(null, billingInfo);
	}

	@Override
	public void next() {
		gui.disable();
		// channel.broadcast(new BillingInfoNodeDone(billingInfo));
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
