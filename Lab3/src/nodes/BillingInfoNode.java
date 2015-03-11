package nodes;

import java.io.IOException;

import model.BillingInfo;
import util.NodeBehavior;

public class BillingInfoNode extends AbstractNode {

	//in
	private static final String TID = "travel_info_done";
	
	//out
	private static final String BID = "billing_info_done";

	private BillingInfo billingInfo;

	public BillingInfoNode() throws IOException {
		super(TID, BID);
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
