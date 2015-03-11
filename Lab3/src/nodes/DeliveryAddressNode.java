package nodes;

import gui.SMORGUI;

import java.io.IOException;

import model.BillingInfo;
import nodes.SelectModeOfReciptNode.SelectModeOfReciptDone;
import util.NodeBehavior;
import util.Serializer;

import com.rabbitmq.client.QueueingConsumer;

public class DeliveryAddressNode extends AbstractNode {

	private BillingInfo billingInfo;
	
	public static String EXCHANGE_NAME = "EXCHANGE_DAN";
	

	public DeliveryAddressNode() throws IOException {
		super();
	}

	@Override
	protected void init() throws IOException {
		channel.exchangeDeclare(SelectModeOfReciptNode.EXCHANGE_NAME, "fanout");

		String queueName = channel.queueDeclare().getQueue();
		
		channel.queueBind(queueName, SelectModeOfReciptNode.EXCHANGE_NAME, "");

		QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        consumers.add(consumer);
        
        
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();

		billingInfo = (BillingInfo) message;

		NodeBehavior.deliveryAddressBehavior(billingInfo);

		gui.notify(null, billingInfo);
	}	

	@Override
	public void next() throws IOException {
		gui.disable();
//		channel.broadcast(new DeliveryAddressDone(billingInfo));
		
		channel.basicPublish(EXCHANGE_NAME, "", null, Serializer.serialize(billingInfo));
		
		synchronized (lock) {
			lock.notify();
		}
	}

	public static class DeliveryAddressDone {

		private BillingInfo billingInfo;

		public DeliveryAddressDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}