package lab2.impl;

import lab2.inf.ProcessPaymentNode;

import commonosgi.gui.GUI;
import commonosgi.model.BillingInfo;
import commonosgi.util.NodeBehavior;

public class ProcessPaymentNodeImpl extends AbstractNode implements
		ProcessPaymentNode {

	private BillingInfo billingInfo;

	public ProcessPaymentNodeImpl() {
		super();

		new GUI("Process payment node", this);
	}

	@Override
	public void next() {
		billingInfo = null;

		if (!queue.isEmpty()) {
			billingInfo = (BillingInfo) queue.poll();
		
			NodeBehavior.processPaymentBehavior(billingInfo);
	
			gui.notify(null, billingInfo);
	
			gui.enable();
		} else {
			gui.disable();
		}
		
		gui.setQueueSize(queue.size());		
	}

	@Override
	public void processPayment(BillingInfo billingInfo) {
		if (queue.isEmpty() && this.billingInfo == null) {
			this.billingInfo = billingInfo;

			NodeBehavior.processPaymentBehavior(billingInfo);

			gui.notify(null, billingInfo);

			gui.enable();
		} else {
			queue.add(billingInfo);
		}
		
		gui.setQueueSize(queue.size());		
	}

}
