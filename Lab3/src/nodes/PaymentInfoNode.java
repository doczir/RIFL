package nodes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import model.BillingInfo;
import model.TravelInfo;
import nodes.BillingInfoNode.BillingInfoNodeDone;
import nodes.ProcessReservationNode.ProcessReservationNodeDone;
import util.NodeBehavior;
import util.Serializer;

import com.rabbitmq.client.QueueingConsumer;

public class PaymentInfoNode extends AbstractNode {
	private static final int KEY_TRAVEL_INFO = 0;
	private static final int KEY_BILLING_INFO = 1;
	
	private BillingInfo billingInfo;

	private ArrayBlockingQueue<ProcessReservationNodeDone> prnd;
	
	private ArrayBlockingQueue<BillingInfoNodeDone> bind;
	
	
	public static String EXCHANGE_NAME = "EXCHANGE_PIN";
	
	
	public PaymentInfoNode() throws IOException {
		super();
	}

	@Override
	public void next() throws IOException {
		gui.disable();
//		channel.broadcast(new PaymentInfoNodeDone(billingInfo));
		
		channel.basicPublish(EXCHANGE_NAME, "", null, Serializer.serialize(billingInfo));
		
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
	protected void init() throws IOException {
		String queueName = null;
		QueueingConsumer consumer = null;
		
		channel.exchangeDeclare(BillingInfoNode.EXCHANGE_NAME, "fanout");
		channel.exchangeDeclare(ProcessReservationNode.EXCHANGE_NAME, "fanout");

		queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, BillingInfoNode.EXCHANGE_NAME, "");
		consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        consumers.add(consumer);

		queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, ProcessReservationNode.EXCHANGE_NAME, "");
		consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        consumers.add(consumer);

        
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	}

	private void joinIncomingMessages() throws InterruptedException {
		if ((prnd.size() > 0) && (bind.size() > 0)) {
			Map<Integer, Object> message = new HashMap<Integer, Object>();
			
			message.put(KEY_TRAVEL_INFO, prnd.take());
			message.put(KEY_BILLING_INFO, bind.take());
		}
	}
	
	@Override
	protected void processMessage(Object message) {
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

	@Override
	protected Object nextMessage() throws Exception {
		// TODO Auto-generated method stub
		Map<Integer, Object> message = new HashMap<Integer, Object>();
		
		message.put(KEY_BILLING_INFO, Serializer.deserialize(consumers.get(0).nextDelivery().getBody()));
		message.put(KEY_TRAVEL_INFO, Serializer.deserialize(consumers.get(1).nextDelivery().getBody()));
		
		return message;
	}
}
