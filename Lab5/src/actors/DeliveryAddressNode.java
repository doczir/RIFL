package actors;

import java.io.Serializable;

import gui.GUI;
import model.BillingInfo;
import util.NodeBehavior;
import actors.SelectModeOfReciptNode.SelectModeOfReciptDone;
import akka.actor.Props;
import akka.japi.Creator;

public class DeliveryAddressNode extends AbstractNode {

	private BillingInfo billingInfo;

	public DeliveryAddressNode() {
		super();
		new GUI("DeliveryAddressNode", this);
	}
	
	@Override
	protected void internalProcessMessage(Object message) {
		SelectModeOfReciptDone msg = (SelectModeOfReciptDone) message;

		gui.enable();

		billingInfo = msg.getBillingInfo();

		NodeBehavior.deliveryAddressBehavior(billingInfo);

		gui.notify(null, billingInfo);
	}

	@Override
	public void next() {
		gui.disable();
		super.next();
	}

	public static class DeliveryAddressDone  implements Serializable {

		private BillingInfo billingInfo;

		public DeliveryAddressDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
	
	public static Props props() {
		return Props.create(DeliveryAddressNode.class,new Creator<DeliveryAddressNode>() {

			@Override
			public DeliveryAddressNode create() throws Exception {
				return new DeliveryAddressNode();
			}
		});
	}
}