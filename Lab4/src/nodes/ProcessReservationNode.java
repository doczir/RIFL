package nodes;

import java.io.IOException;

import model.TravelInfo;
import util.NodeBehavior;
import util.Serializer;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.QueueingConsumer;

public class ProcessReservationNode extends BasicAbstractNode {

	private TravelInfo travelInfo;

	public static String EXCHANGE_NAME =  "EXCHANGE_PRN";
	
	
	public ProcessReservationNode() throws Exception {
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
		travelInfo = (TravelInfo) message;
		NodeBehavior.processReservationBehavior(travelInfo);
		gui.notify(travelInfo, null);
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
		
		channel.basicPublish(EXCHANGE_NAME, "", props, Serializer.serialize(travelInfo));
		
		synchronized (lock) {
			lock.notify();		
		}		
	}
	
	public static class ProcessReservationNodeDone {
		private TravelInfo travelInfo;
		
		public ProcessReservationNodeDone(TravelInfo travelInfo) {
			super();
			this.travelInfo = travelInfo;
		}

		public TravelInfo getTravelInfo() {
			return travelInfo;
		}
	}
}
