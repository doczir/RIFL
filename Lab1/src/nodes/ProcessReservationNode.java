package nodes;

import model.TravelInfo;
import nodes.TravelInfoNode.TravelInfoNodeDone;
import util.NodeBehavior;
import channel.Channel;

public class ProcessReservationNode extends AbstractNode {

	private TravelInfo travelInfo;

	
	public ProcessReservationNode(Channel channel) {
		super(channel);
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

		gui.enable();
		travelInfo = msg.getTravelInfo();
		NodeBehavior.processReservationBehavior(travelInfo);
		gui.notify(travelInfo, null);
	}
	
	@Override
	public void next() {
		gui.disable();
		channel.broadcast(new ProcessReservationNodeDone(travelInfo));
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
