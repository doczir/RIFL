package nodes;

import model.BillingInfo;
import nodes.TravelInfoNode.TravelInfoNodeDone;
import util.NodeBehavior;
import channel.BaseMessage;
import channel.Channel;

public class BillingInfoNode extends AbstractNode {

	private BillingInfo billingInfo;

	public BillingInfoNode(Channel channel) {
		super(channel);
	}

	@Override
	protected void init() {
		channel.add(TravelInfoNodeDone.class, msg -> {
			onMessageReceived(msg);
		});
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();
		id = ((BaseMessage) message).getId();
		billingInfo = new BillingInfo();
		NodeBehavior.billingInfoBehavior(billingInfo);

		gui.notify(null, billingInfo);
	}

	@Override
	public void next() {
		gui.disable();
		channel.broadcast(new BillingInfoNodeDone(billingInfo, id));
		synchronized (lock) {
			lock.notify();
		}
	}

	public static class BillingInfoNodeDone extends BaseMessage {
		private BillingInfo billingInfo;

		public BillingInfoNodeDone(BillingInfo billingInfo, int id) {
			super();
			this.billingInfo = billingInfo;
			setId(id);
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}
