package nodes;

import model.BillingInfo;
import nodes.PaymentInfoNode.PaymentInfoNodeDone;
import nodes.SelectModeOfReciptNode.SelectModeOfReciptDone;
import util.NodeBehavior;
import channel.Channel;

public class DeliveryAddressNode extends AbstractNode {

	private Object lock = new Object();
	private BillingInfo billingInfo;

	public DeliveryAddressNode(Channel channel) {
		super(channel);

		channel.add(SelectModeOfReciptDone.class, msg -> {
			try {
				if (msg.isDelivery())
					queue.put(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		start(() -> {
			while (true) {
				SelectModeOfReciptDone msg = null;
				try {
					msg = (SelectModeOfReciptDone) queue.take();
				} catch (Exception e) {
					e.printStackTrace();
				}
				gui.enable();

				billingInfo = msg.getBillingInfo();

				NodeBehavior.deliveryAddressBehavior(billingInfo);

				gui.notify(null, billingInfo);
				gui.enable();
				synchronized (lock) {
					try {
						lock.wait();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void next() {
		gui.disable();
		channel.broadcast(new DeliveryAddressDone(billingInfo));
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
