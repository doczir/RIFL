package nodes;

import java.io.IOException;

import com.rabbitmq.client.QueueingConsumer;

import model.TravelInfo;
import util.NodeBehavior;
import util.Serializer;

public class TravelInfoNode extends AbstractNode {

	public static String EXCHANGE_NAME = "EXCHANGE_TIN";
	
	private TravelInfo travelInfo;

	public TravelInfoNode() throws IOException {
		super();
	}
	

	@Override
	protected void init() throws IOException {
		channel.exchangeDeclare("EXCHANGE_START", "fanout");

		String queueName = channel.queueDeclare().getQueue();
		
		channel.queueBind(queueName, "EXCHANGE_START", "");

		QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, consumer);

        consumers.add(consumer);
        
        
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");		
		
		
//		channel.add(Start.class, (msg) -> {
//			onMessageReceived(msg);
//		});
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
//		channel.broadcast(new TravelInfoNodeDone(travelInfo));
//		channel.broadcast(new Start());
		
		channel.basicPublish(EXCHANGE_NAME, "", null, Serializer.serialize(travelInfo));
		channel.basicPublish("EXCHANGE_START", "", null, Serializer.serialize("Start"));
		
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
