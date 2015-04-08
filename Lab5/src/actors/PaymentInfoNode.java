package actors;

import gui.GUI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;

import model.BillingInfo;
import util.NodeBehavior;
import actors.BillingInfoNode.BillingInfoNodeDone;
import actors.ProcessReservationNode.ProcessReservationNodeDone;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class PaymentInfoNode extends AbstractNode {
	private static final int KEY_PROCESS_RESERVATION_NODE_DONE = 0;
	private static final int KEY_BILLING_INFO_NODE_DONE = 1;

	private BillingInfo billingInfo;

	private Queue<ProcessReservationNodeDone> prnd;

	private Queue<BillingInfoNodeDone> bind;
	
	public PaymentInfoNode() {
		super();
		new GUI("PaymentInfoNode", this);
	}

	@Override
	public void next() {
		gui.disable();
		// channel.broadcast(new PaymentInfoNodeDone(billingInfo));
		super.next();
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
		prnd = new LinkedList<ProcessReservationNodeDone>();
		bind = new LinkedList<BillingInfoNodeDone>();

		receive(ReceiveBuilder
				.match(ProcessReservationNodeDone.class,
						msg -> {
							try {
								prnd.add(msg);

								joinIncomingMessages();
							} catch (Exception e) {
								Logger.getLogger(
										PaymentInfoNode.this.getClass()
												.getSimpleName()).severe(
										"An exception occured: "
												+ e.getMessage());
							}
						})
				.match(BillingInfoNodeDone.class,
						msg -> {
							try {
								bind.add(msg);

								joinIncomingMessages();
							} catch (Exception e) {
								Logger.getLogger(
										PaymentInfoNode.this.getClass()
												.getSimpleName()).severe(
										"An exception occured: "
												+ e.getMessage());
							}
						}).build());
	}

	private void joinIncomingMessages() throws InterruptedException {
		if ((prnd.size() > 0) && (bind.size() > 0)) {
			Map<Integer, Object> message = new HashMap<Integer, Object>();

			message.put(KEY_PROCESS_RESERVATION_NODE_DONE, prnd.poll());
			message.put(KEY_BILLING_INFO_NODE_DONE, bind.poll());

			onMessageReceived(message);
		}
	}

	@Override
	protected void internalProcessMessage(Object message) {
		ProcessReservationNodeDone prndm = null;
		BillingInfoNodeDone bindm = null;

		try {
			prndm = (ProcessReservationNodeDone) ((Map<Integer, Object>) message)
					.get(KEY_PROCESS_RESERVATION_NODE_DONE);
			bindm = (BillingInfoNodeDone) ((Map<Integer, Object>) message)
					.get(KEY_BILLING_INFO_NODE_DONE);

			billingInfo = bindm.getBillingInfo();
			NodeBehavior.paymentInfoBehavior(billingInfo);

			gui.notify(null, billingInfo);
			gui.enable();
		} catch (Exception e) {
			Logger.getLogger(PaymentInfoNode.this.getClass().getSimpleName())
					.severe("An exception occured: " + e.getMessage());
		}
	}
	
	public static Props props() {
		return Props.create(PaymentInfoNode.class, PaymentInfoNode::new);
	}
}
