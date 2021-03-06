package lab2.impl;

import lab2.inf.BillingInfoNode;
import lab2.inf.PaymentInfoNode;

import commonosgi.gui.GUI;
import commonosgi.model.BillingInfo;
import commonosgi.util.NodeBehavior;

public class BillingInfoNodeImpl extends AbstractNode implements
		BillingInfoNode {

	private BillingInfo billingInfo;

	private PaymentInfoNode pin;

	public BillingInfoNodeImpl() {
		super();

		new GUI("BillingInfo node", this);
	}

	@Override
	public void createBillingInfo() {
		if (queue.isEmpty() && billingInfo == null) {
			billingInfo = new BillingInfo();

			NodeBehavior.billingInfoBehavior(billingInfo);

			gui.notify(null, billingInfo);

			gui.enable();
		} else {
			queue.add(new Object());
		}
		
		gui.setQueueSize(queue.size());
	}

	@Override
	public void next() {
		if (billingInfo != null && pin != null) {
			pin.getPaymentInfo(billingInfo);

			billingInfo = null;

			gui.disable();
		}

		if (!queue.isEmpty()) {
			queue.poll();

			billingInfo = new BillingInfo();

			NodeBehavior.billingInfoBehavior(billingInfo);

			gui.notify(null, billingInfo);

			gui.enable();
		}
		
		
		gui.setQueueSize(queue.size());		
	}

	public void setPin(PaymentInfoNode pin) {
		this.pin = pin;
	}

	public void unsetPin(PaymentInfoNode pin) {
		if (this.pin == pin) {
			pin = null;
		}
	}
}
