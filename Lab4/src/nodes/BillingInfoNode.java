package nodes;

import java.io.IOException;

import model.BillingInfo;
import util.NodeBehavior;
import util.Serializer;

import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

public class BillingInfoNode extends BasicAbstractNode {
	private BillingInfo billingInfo;

	public static String EXCHANGE_NAME = "EXCHANGE_BIN";
	
	public BillingInfoNode() throws Exception {
		super();
	}

	@Override
	protected void init() throws IOException {
		channel.exchangeDeclare(TravelInfoNode.EXCHANGE_NAME, "fanout");

		String queueName = channel.queueDeclare().getQueue();
		
		channel.queueBind(queueName, TravelInfoNode.EXCHANGE_NAME, "");

		consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();

		billingInfo = new BillingInfo();
		NodeBehavior.billingInfoBehavior(billingInfo);

		gui.notify(null, billingInfo);
	}

	@Override
	public void next() throws IOException {
		gui.disable();
		
		BasicProperties props = null;
		if (lastMessage.getProperties() != null && lastMessage.getProperties().getCorrelationId() != null) {
			props = new BasicProperties()
					.builder()
					.correlationId(lastMessage.getProperties().getCorrelationId())
					.build();
		}
		
		channel.basicPublish(EXCHANGE_NAME, "", props, Serializer.serialize(billingInfo));
		
		synchronized (lock) {
			lock.notify();
		}
	}

	public static class BillingInfoNodeDone {
		private BillingInfo billingInfo;

		public BillingInfoNodeDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}
