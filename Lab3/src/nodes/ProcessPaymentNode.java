package nodes;

import java.io.IOException;
import java.nio.channels.Channel;

import com.rabbitmq.client.QueueingConsumer;

import model.BillingInfo;
import nodes.PaymentInfoNode.PaymentInfoNodeDone;
import util.NodeBehavior;
import util.Serializer;

public class ProcessPaymentNode extends AbstractNode {
	
	private BillingInfo billingInfo;

	public static String EXCHANGE_NAME =  "EXCHANGE_PPN";
	
	public ProcessPaymentNode() throws IOException {
		super();
	}
	
	@Override
	protected void init() throws IOException {
		channel.exchangeDeclare(PaymentInfoNode.EXCHANGE_NAME, "fanout");

		String queueName = channel.queueDeclare().getQueue();
		
		channel.queueBind(queueName, PaymentInfoNode.EXCHANGE_NAME, "");

		QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        consumers.add(consumer);
        
        
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		
		
//		channel.add(PaymentInfoNodeDone.class, msg -> {
//			onMessageReceived(msg);
//		});
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
//		channel.broadcast(new ProcessPaymentNodeDone(billingInfo));
		
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
