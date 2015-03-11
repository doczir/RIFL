package nodes;

import java.io.IOException;

import com.rabbitmq.client.QueueingConsumer;

import model.TravelInfo;
import nodes.TravelInfoNode.TravelInfoNodeDone;
import util.NodeBehavior;
import util.Serializer;

public class ProcessReservationNode extends AbstractNode {

	private TravelInfo travelInfo;

	public static String EXCHANGE_NAME =  "EXCHANGE_PRN";
	
	
	public ProcessReservationNode() throws IOException {
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
		
//		channel.add(TravelInfoNodeDone.class, (msg) -> {
//			onMessageReceived(msg);
//		});
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
//		channel.broadcast(new ProcessReservationNodeDone(travelInfo));
		
		channel.basicPublish(EXCHANGE_NAME, "", null, Serializer.serialize(travelInfo));
		
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
