package nodes;

import javax.jms.BytesMessage;
import javax.jms.Queue;
import javax.jms.QueueSender;

import model.TravelInfo;
import util.NodeBehavior;
import util.Serializer;

public class ProcessReservationNode extends BasicAbstractNode {

	private TravelInfo travelInfo;

	public static String QUEUE_NAME =  "queue/QUEUE_PRN";
	
	protected QueueSender sender_out;

	
	
	public ProcessReservationNode() throws Exception {
		super();
	}

	@Override
	protected void init() throws Exception {
		Queue queue_in = (Queue) initialContext.lookup(TravelInfoNode.QUEUE_NAME_PRN);
		Queue queue_out = (Queue) initialContext.lookup(QUEUE_NAME);
		
		receiver = session.createReceiver(queue_in);
		
		sender_out = session.createSender(queue_out);
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();
		travelInfo = (TravelInfo) message;
		NodeBehavior.processReservationBehavior(travelInfo);
		gui.notify(travelInfo, null);
	}
	
	@Override
	public void next() throws Exception {
		gui.disable();
		
		BytesMessage message = session.createBytesMessage();

		if (lastMessage.getStringProperty(JoinAbstractNode.KEY_CORRELATION_ID) != null) {
			message.setStringProperty(JoinAbstractNode.KEY_CORRELATION_ID, lastMessage.getStringProperty(JoinAbstractNode.KEY_CORRELATION_ID));
		}

		message.writeBytes(Serializer.serialize(travelInfo));

		sender_out.send(message);
		
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
