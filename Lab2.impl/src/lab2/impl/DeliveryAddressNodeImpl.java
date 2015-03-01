package lab2.impl;

import lab2.inf.DeliveryAddressNode;
import commonosgi.gui.GUI;
import commonosgi.model.BillingInfo;
import commonosgi.util.NodeBehavior;

public class DeliveryAddressNodeImpl extends AbstractNode implements
		DeliveryAddressNode {

	private BillingInfo billingInfo;

	public DeliveryAddressNodeImpl() {
		super();

		new GUI("Delivery Address Node", this);
	}

	@Override
	public void next() {
		billingInfo = null;

		if (!queue.isEmpty()) {
			billingInfo = (BillingInfo) queue.poll();
		
			NodeBehavior.deliveryAddressBehavior(billingInfo);
			
			gui.notify(null, billingInfo);
			gui.enable();		
		} else {
			gui.disable();
		}
		
		gui.setQueueSize(queue.size());		
	}

	@Override
	public void getDeliveryAddress(BillingInfo billingInfo) {
		if (queue.isEmpty() && this.billingInfo == null) {
			this.billingInfo = billingInfo;
			
			NodeBehavior.deliveryAddressBehavior(billingInfo);
			
			gui.notify(null, billingInfo);
			gui.enable();
		} else {
			queue.add(billingInfo);
		}
		
		gui.setQueueSize(queue.size());
	}

}
