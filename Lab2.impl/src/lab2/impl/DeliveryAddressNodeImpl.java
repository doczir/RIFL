package lab2.impl;

import lab2.inf.DeliveryAddressNode;

import commonosgi.gui.GUI;
import commonosgi.model.BillingInfo;

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

		if (!queue.isEmpty())
			billingInfo = (BillingInfo) queue.poll();
		else
			gui.disable();
	}

	@Override
	public void getDeliveryAddress(BillingInfo billingInfo) {
		if (queue.isEmpty() & this.billingInfo == null) {
			this.billingInfo = billingInfo;
		} else {
			queue.add(billingInfo);
		}
	}

}
