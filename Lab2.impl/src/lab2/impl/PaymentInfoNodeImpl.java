package lab2.impl;

import java.util.LinkedList;
import java.util.Queue;

import lab2.inf.PaymentInfoNode;
import lab2.inf.ProcessPaymentNode;
import lab2.inf.SelectModeOfReciptNode;
import commonosgi.gui.GUI;
import commonosgi.model.BillingInfo;
import commonosgi.util.NodeBehavior;

public class PaymentInfoNodeImpl extends AbstractNode implements
		PaymentInfoNode {

	private Queue<BillingInfo> biq;
	int prq = 0;
	private ProcessPaymentNode ppn;
	private SelectModeOfReciptNode smorn;
	private BillingInfo billingInfo;

	public PaymentInfoNodeImpl() {
		super();

		biq = new LinkedList<BillingInfo>();
		
		new GUI("PaymentInfoNode", this);
	}

	@Override
	public void next() {
		smorn.selectModeOfRecipt(billingInfo);
		ppn.processPayment(billingInfo);
		billingInfo = null;
		
		if (!biq.isEmpty() && prq > 0)
			createBI();
		else
			gui.disable();
	}

	private void createBI() {
		billingInfo = (BillingInfo) biq.poll();
		NodeBehavior.paymentInfoBehavior(billingInfo);
		gui.notify(null, billingInfo);
	}

	public void setPPN(ProcessPaymentNode ppn) {
		this.ppn = ppn;
	}

	public void unsetPPN(ProcessPaymentNode ppn) {
		if (this.ppn == ppn)
			this.ppn = null;
	}

	public void setSMORN(SelectModeOfReciptNode smorn) {
		this.smorn = smorn;
	}

	public void unsetSMORN(SelectModeOfReciptNode smorn) {
		if (this.smorn == smorn)
			this.smorn = null;
	}

	@Override
	public void processReservationFinished() {
		if (!biq.isEmpty()) {
			if (billingInfo == null)
				createBI();
			gui.enable();
		} else
			prq++;
	}

	@Override
	public void getPaymentInfo(BillingInfo billingInfo) {
		if (prq > 0) {
			prq--;
			biq.add(billingInfo);
			if (this.billingInfo == null)
				createBI();
			gui.enable();
		} else
			biq.add(billingInfo);
	}

}
