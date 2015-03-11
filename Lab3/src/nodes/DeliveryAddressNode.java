package nodes;

import model.BillingInfo;
import nodes.SelectModeOfReciptNode.SelectModeOfReciptDone;
import util.NodeBehavior;

public class DeliveryAddressNode extends AbstractNode {

	private BillingInfo billingInfo;

	public DeliveryAddressNode() {
		super();
	}

	@Override
	protected void init() {
//		channel.add(SelectModeOfReciptDone.class, msg -> {
//			if (msg.isDelivery()) {
//				onMessageReceived(msg);
//			}
//		});
	}

	@Override
	protected void processMessage(Object message) {
		SelectModeOfReciptDone msg = (SelectModeOfReciptDone) message;
		
		gui.enable();

		billingInfo = msg.getBillingInfo();

		NodeBehavior.deliveryAddressBehavior(billingInfo);

		gui.notify(null, billingInfo);
	}	

	@Override
	public void next() {
		gui.disable();
//		channel.broadcast(new DeliveryAddressDone(billingInfo));
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