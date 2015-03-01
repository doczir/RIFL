package lab2.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import lab2.inf.PaymentInfoNode;
import lab2.inf.ProcessPaymentNode;
import lab2.inf.SelectModeOfReciptNode;
import commonosgi.gui.GUI;
import commonosgi.model.BillingInfo;
import commonosgi.model.TravelInfo;
import commonosgi.util.NodeBehavior;

public class PaymentInfoNodeImpl extends AbstractNode implements
		PaymentInfoNode {
	
	private static final int KEY_TRAVEL_INFO = 0;
	private static final int KEY_BILLING_INFO = 1;
	
	private Queue<TravelInfo> tiq;
	
	private Queue<BillingInfo> biq;
	

//	private Queue<BillingInfo> biq;
//	int prq = 0;
	private ProcessPaymentNode ppn;
	private SelectModeOfReciptNode smorn;
	private BillingInfo billingInfo;

	public PaymentInfoNodeImpl() {
		super();

		tiq = new LinkedList<TravelInfo>();
		biq = new LinkedList<BillingInfo>();
		
//		biq = new LinkedList<BillingInfo>();

		new GUI("PaymentInfoNode", this);
	}

	@Override
	public void next() {
//		smorn.selectModeOfRecipt(billingInfo);
//		ppn.processPayment(billingInfo);
//		billingInfo = null;
//
//		if (!biq.isEmpty() && prq > 0)
//			createBI();
//		else
//			gui.disable();
//		
//		
//		gui.setQueueSize(queue.size());	
		
		if (billingInfo != null && smorn != null && ppn != null) {
			smorn.selectModeOfRecipt(billingInfo);
			ppn.processPayment(billingInfo);

			this.billingInfo = null;
			
			gui.disable();
		}
		
		if (!queue.isEmpty()) {
			Map<Integer, Object> message = (Map<Integer, Object>) queue.poll();
			
			billingInfo = (BillingInfo) message.get(KEY_BILLING_INFO);

			NodeBehavior.billingInfoBehavior(billingInfo);

			gui.notify(null, billingInfo);

			gui.enable();			
		}
		
		gui.setQueueSize(queue.size());
	}

//	private void createBI() {
//		billingInfo = (BillingInfo) biq.poll();
//		NodeBehavior.paymentInfoBehavior(billingInfo);
//		gui.notify(null, billingInfo);
//	}

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
	public void processReservationFinished(TravelInfo travelInfo) {
//		if (!biq.isEmpty()) {
//			if (billingInfo == null)
//				createBI();
//			gui.enable();
//			
//			gui.setQueueSize(biq.size());
//		} else
//			prq++;
		tiq.add(travelInfo);
		
		joinIncomingMessages();
	}

	@Override
	public void getPaymentInfo(BillingInfo billingInfo) {
//		if (prq > 0) {
//			prq--;
//			biq.add(billingInfo);
//			if (this.billingInfo == null)
//				createBI();
//			gui.enable();
//			
//			gui.setQueueSize(biq.size());			
//		} else
//			biq.add(billingInfo);
		
		biq.add(billingInfo);
		
		joinIncomingMessages();
	}
	
	private void joinIncomingMessages() {
		if ((tiq.size() > 0) && (biq.size() > 0)) {
			Map<Integer, Object> message = new HashMap<Integer, Object>();
			
			message.put(KEY_TRAVEL_INFO, tiq.poll());
			message.put(KEY_BILLING_INFO, biq.poll());
			
			queue.add(message);

			gui.setQueueSize(queue.size());
			
			if (this.billingInfo == null) {
				next();
			}
		}
	}	

}
