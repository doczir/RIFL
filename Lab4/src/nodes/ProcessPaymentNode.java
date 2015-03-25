package nodes;

import java.io.IOException;

import model.BillingInfo;
import util.NodeBehavior;
import util.Serializer;

import com.rabbitmq.client.QueueingConsumer;

public class ProcessPaymentNode extends BasicAbstractNode {
	
	private BillingInfo billingInfo;

	public static String EXCHANGE_NAME =  "EXCHANGE_PPN";
	
	public ProcessPaymentNode() throws Exception {
		super();
	}
	
	@Override
	protected void init() throws IOException {
		channel.exchangeDeclare(PaymentInfoNode.EXCHANGE_NAME, "fanout");

		String queueName = channel.queueDeclare().getQueue();
		
		channel.queueBind(queueName, PaymentInfoNode.EXCHANGE_NAME, "");

		consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();
		billingInfo = (BillingInfo) message;
		NodeBehavior.processPaymentBehavior(billingInfo);
		gui.notify(null, billingInfo);
	}	

	@Override
	public void next() throws IOException {
		gui.disable();
		
		channel.basicPublish(EXCHANGE_NAME, "", null, Serializer.serialize(billingInfo));
		
		synchronized (lock) {
			lock.notify();		
		}	
	}
	
	public static class ProcessPaymentNodeDone {
		private BillingInfo billingInfo;

		public ProcessPaymentNodeDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}
		
		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}
