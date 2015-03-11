package nodes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import model.BillingInfo;
import nodes.BillingInfoNode.BillingInfoNodeDone;
import nodes.ProcessReservationNode.ProcessReservationNodeDone;
import util.NodeBehavior;

public class PaymentInfoNode extends AbstractNode {
	private static final int KEY_PROCESS_RESERVATION_NODE_DONE = 0;
	private static final int KEY_BILLING_INFO_NODE_DONE = 1;
	
	private BillingInfo billingInfo;

	private ArrayBlockingQueue<ProcessReservationNodeDone> prnd;
	
	private ArrayBlockingQueue<BillingInfoNodeDone> bind;
	
	
	public PaymentInfoNode() {
		super();
	}

	@Override
	public void next() {
		gui.disable();
//		channel.broadcast(new PaymentInfoNodeDone(billingInfo));
		synchronized (lock) {
			lock.notify();
		}
	}

	public static class PaymentInfoNodeDone {
		private BillingInfo billingInfo;

		public PaymentInfoNodeDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}

	@Override
	protected void init() {
		prnd = new ArrayBlockingQueue<ProcessReservationNodeDone>(10);
		bind = new ArrayBlockingQueue<BillingInfoNodeDone>(10);
		
//		channel.add(ProcessReservationNodeDone.class, msg -> {
//			try {
//				prnd.put(msg);
//				
//				joinIncomingMessages();
//			} catch (Exception e) {
//				Logger.getLogger(PaymentInfoNode.this.getClass().getSimpleName()).severe("An exception occured: " + e.getMessage());
//			}
//		});
//
//		channel.add(BillingInfoNodeDone.class, msg -> {
//			try {
//				bind.put(msg);
//				
//				joinIncomingMessages();
//			} catch (Exception e) {
//				Logger.getLogger(PaymentInfoNode.this.getClass().getSimpleName()).severe("An exception occured: " + e.getMessage());
//			}
//		});
	}

	private void joinIncomingMessages() throws InterruptedException {
		if ((prnd.size() > 0) && (bind.size() > 0)) {
			Map<Integer, Object> message = new HashMap<Integer, Object>();
			
			message.put(KEY_PROCESS_RESERVATION_NODE_DONE, prnd.take());
			message.put(KEY_BILLING_INFO_NODE_DONE, bind.take());
			
			onMessageReceived(message);
		}
	}
	
	@Override
	protected void processMessage(Object message) {
		ProcessReservationNodeDone prndm = null;
		BillingInfoNodeDone bindm = null;
		
		try {
			prndm = (ProcessReservationNodeDone) ((Map<Integer, Object>) message).get(KEY_PROCESS_RESERVATION_NODE_DONE);
			bindm = (BillingInfoNodeDone) ((Map<Integer, Object>) message).get(KEY_BILLING_INFO_NODE_DONE);

			billingInfo = bindm.getBillingInfo();				
			NodeBehavior.paymentInfoBehavior(billingInfo);

			gui.notify(null, billingInfo);
			gui.enable();
		} catch (Exception e) {
			Logger.getLogger(PaymentInfoNode.this.getClass().getSimpleName()).severe("An exception occured: " + e.getMessage());
		}
	}
}
