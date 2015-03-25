package nodes;

import javax.jms.BytesMessage;
import javax.jms.Queue;
import javax.jms.QueueSender;

import model.TravelInfo;
import util.NodeBehavior;
import util.Serializer;

public class TravelInfoNode extends BasicAbstractNode {

	public static String QUEUE_NAME_BIN = "queue/QUEUE_TIN_BIN";
	
	public static String QUEUE_NAME_PRN = "queue/QUEUE_TIN_PRN";
	
	private TravelInfo travelInfo;
	
	private Integer correlationId = 0;
	
	private QueueSender sender_start;
	
	protected QueueSender sender_out_bin;
	
	protected QueueSender sender_out_prn;
	

	public TravelInfoNode() throws Exception {
		super();
	}
	

	@Override
	protected void init() throws Exception {
		Queue queue_start = (Queue) initialContext.lookup("queue/start");
		
		Queue queue_out_bin = (Queue) initialContext.lookup(QUEUE_NAME_BIN);
		
		Queue queue_out_prn = (Queue) initialContext.lookup(QUEUE_NAME_PRN);
		
		receiver = session.createReceiver(queue_start);
		
		sender_out_bin = session.createSender(queue_out_bin);
		
		sender_out_prn = session.createSender(queue_out_prn);
		
		sender_start = session.createSender(queue_start);
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();
		travelInfo = new TravelInfo();
		NodeBehavior.travelInfoBehavior(travelInfo);
		gui.notify(travelInfo, null);
	}	

	@Override
	public void next() throws Exception {
		BytesMessage message = session.createBytesMessage();
		message.setStringProperty(JoinAbstractNode.KEY_CORRELATION_ID, (++correlationId).toString());
		message.writeBytes(Serializer.serialize(travelInfo));
		
		sender_out_bin.send(message);
		sender_out_prn.send(message);

		BytesMessage sm = session.createBytesMessage();
		sm.writeBytes(Serializer.serialize("Start"));
		sender_start.send(sm);
		
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
