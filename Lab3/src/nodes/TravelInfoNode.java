package nodes;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.QueueingConsumer;

import model.TravelInfo;
import util.NodeBehavior;
import util.Serializer;

public class TravelInfoNode extends BasicAbstractNode {

	public static String EXCHANGE_NAME = "EXCHANGE_TIN";
	
	private TravelInfo travelInfo;
	
	private Integer correlationId = 0;
	

	public TravelInfoNode() throws Exception {
		super();
	}
	

	@Override
	protected void init() throws IOException {
		channel.exchangeDeclare("EXCHANGE_START", "fanout");

		String queueName = channel.queueDeclare().getQueue();
		
		channel.queueBind(queueName, "EXCHANGE_START", "");

		consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);
        
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");		
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();
		travelInfo = new TravelInfo();
		NodeBehavior.travelInfoBehavior(travelInfo);
		gui.notify(travelInfo, null);
	}	

	@Override
	public void next() throws IOException {
		BasicProperties props = new BasicProperties()
				.builder()
				.correlationId((++correlationId).toString())
				.build();
		
		channel.basicPublish(EXCHANGE_NAME, "", props, Serializer.serialize(travelInfo));
		channel.basicPublish("EXCHANGE_START", "", props, Serializer.serialize("Start"));
		
		synchronized (lock) {
			lock.notify();
		}
	}
	
	public static class Start {}

	public static class TravelInfoNodeDone {
		private TravelInfo travelInfo;

		public TravelInfoNodeDone(TravelInfo travelInfo) {
			super();
			this.travelInfo = travelInfo;
		}

		public TravelInfo getTravelInfo() {
			return travelInfo;
		}
	}

}
