package nodes;

import model.BillingInfo;
import nodes.TravelInfoNode.TravelInfoNodeDone;
import channel.Channel;

public class BillingInfoNode extends AbstractNode {

	private BillingInfo billingInfo;

	public BillingInfoNode(Channel channel) {
		super(channel);

		channel.add(TravelInfoNodeDone.class, msg -> {
			gui.enable();
			billingInfo = new BillingInfo();
			
			
			gui.notify(null, billingInfo);
		});
	}

	@Override
	public void next() {

	}

}
