package nodes;

import java.util.function.IntSupplier;

import model.BillingInfo;
import nodes.SelectModeOfReciptNode.SelectModeOfReciptDone;
import util.NodeBehavior;
import channel.BaseMessage;
import channel.Channel;

public class DeliveryAddressNode extends AbstractNode {

	private BillingInfo billingInfo;

	public DeliveryAddressNode(Channel channel, boolean automatic, IntSupplier delay) {
		super(channel, automatic, delay);
	}

	@Override
	protected void init() {
		channel.add(SelectModeOfReciptDone.class, msg -> {
			if (msg.isDelivery()) {
				onMessageReceived(msg);
			}
		});
	}

	@Override
	protected void processMessage(Object message) {
		SelectModeOfReciptDone msg = (SelectModeOfReciptDone) message;
		id = msg.getId();
		gui.enable();

		billingInfo = msg.getBillingInfo();

		NodeBehavior.deliveryAddressBehavior(billingInfo);

		gui.notify(null, billingInfo);
	}

	@Override
	public void next() {
		super.next();
				
		gui.disable();
		channel.broadcast(new DeliveryAddressDone(billingInfo, id));
		synchronized (lock) {
			lock.notify();
		}
	}

	public static class DeliveryAddressDone extends BaseMessage {

		private BillingInfo billingInfo;

		public DeliveryAddressDone(BillingInfo billingInfo, int id) {
			super();
			this.billingInfo = billingInfo;
			setId(id);
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}