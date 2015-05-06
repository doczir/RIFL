package nodes;

import java.util.function.IntSupplier;

import model.TravelInfo;
import nodes.TravelInfoNode.TravelInfoNodeDone;
import util.NodeBehavior;
import util.StatCollector;
import channel.BaseMessage;
import channel.Channel;

public class ProcessReservationNode extends AbstractNode {

	private TravelInfo travelInfo;

	
	public ProcessReservationNode(Channel channel, boolean automatic, IntSupplier delay) {
		super(channel, automatic, delay);
	}

	@Override
	protected void init() {
		channel.add(TravelInfoNodeDone.class, (msg) -> {
			onMessageReceived(msg);
		});
	}

	@Override
	protected void processMessage(Object message) {
		TravelInfoNodeDone msg = (TravelInfoNodeDone) message;
		id = msg.getId();
		gui.enable();
		travelInfo = msg.getTravelInfo();
		NodeBehavior.processReservationBehavior(travelInfo);
		gui.notify(travelInfo, null);
	}
	
	@Override
	public void next() {
		super.next();
		
		gui.disable();
		channel.broadcast(new ProcessReservationNodeDone(travelInfo, id));
		synchronized (lock) {
			lock.notify();		
		}		
	}
	
	public static class ProcessReservationNodeDone extends BaseMessage {
		private TravelInfo travelInfo;
		
		public ProcessReservationNodeDone(TravelInfo travelInfo, int id) {
			super();
			this.travelInfo = travelInfo;
			setId(id);
		}

		public TravelInfo getTravelInfo() {
			return travelInfo;
		}
	}
}
