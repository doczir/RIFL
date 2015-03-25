package nodes;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import model.BillingInfo;
import model.TravelInfo;
import util.NodeBehavior;
import util.Serializer;

import com.rabbitmq.client.QueueingConsumer;

public class PaymentInfoNode extends JoinAbstractNode {
	private static final int KEY_TRAVEL_INFO = 0;
	private static final int KEY_BILLING_INFO = 1;
	
	private BillingInfo billingInfo;
	
	
	public static String EXCHANGE_NAME = "EXCHANGE_PIN";
	
	
	public PaymentInfoNode() throws Exception {
		super();
	}

	@Override
	public void next() throws IOException {
		synchronized (lock) {
			channel.basicPublish(EXCHANGE_NAME, "", null, Serializer.serialize(billingInfo));
			
			billingInfo = null;
			
			if (queue.isEmpty()) {
				gui.disable();
			} else {
				processMessage(null);
			}
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
	protected void init() throws IOException {
		super.init();
		
		String queueName = null;
		QueueingConsumer consumer = null;
		
		channel.exchangeDeclare(BillingInfoNode.EXCHANGE_NAME, "fanout");
		channel.exchangeDeclare(ProcessReservationNode.EXCHANGE_NAME, "fanout");

		queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, BillingInfoNode.EXCHANGE_NAME, "");
		consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        consumers.add(consumer);
        consumerKeys.put(consumer, KEY_BILLING_INFO);

		queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, ProcessReservationNode.EXCHANGE_NAME, "");
		consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        consumers.add(consumer);
        consumerKeys.put(consumer, KEY_TRAVEL_INFO);

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	}
	
	@Override
	protected void processMessage(Object message) {
		synchronized (lock) {
			if (billingInfo != null && !queue.isEmpty()) {
				return;
			}
			
			message = queue.poll();
			
			TravelInfo ti = null;
			BillingInfo bi = null;
			
			try {
				ti = (TravelInfo) ((Map<Integer, Object>) message).get(KEY_TRAVEL_INFO);
				bi = (BillingInfo) ((Map<Integer, Object>) message).get(KEY_BILLING_INFO);
	
				billingInfo = bi;				
				NodeBehavior.paymentInfoBehavior(billingInfo);
	
				gui.notify(null, billingInfo);
				gui.enable();
			} catch (Exception e) {
				Logger.getLogger(PaymentInfoNode.this.getClass().getSimpleName()).severe("An exception occured: " + e.getMessage());
			}
		}
	}
}
