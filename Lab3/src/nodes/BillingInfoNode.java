package nodes;

import java.io.IOException;

import model.BillingInfo;
import util.NodeBehavior;
import util.Serializer;

import com.rabbitmq.client.QueueingConsumer;

public class BillingInfoNode extends AbstractNode {
	private BillingInfo billingInfo;

	public static String EXCHANGE_NAME = "EXCHANGE_BIN";
	
	public BillingInfoNode() throws IOException {
		super();
	}

	@Override
	protected void init() throws IOException {
		channel.exchangeDeclare(TravelInfoNode.EXCHANGE_NAME, "fanout");

		String queueName = channel.queueDeclare().getQueue();
		
		channel.queueBind(queueName, TravelInfoNode.EXCHANGE_NAME, "");

		QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        consumers.add(consumer);
        
        
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
		
		// channel.broadcast(new BillingInfoNodeDone(billingInfo));
		channel.basicPublish(EXCHANGE_NAME, "", null, Serializer.serialize(billingInfo));
		
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
