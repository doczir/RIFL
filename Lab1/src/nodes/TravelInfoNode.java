package nodes;

import model.TravelInfo;
import util.NodeBehavior;
import channel.Channel;

public class TravelInfoNode extends AbstractNode {

	private TravelInfo travelInfo;

	public TravelInfoNode(Channel channel) {
		super(channel);
	}
	

	@Override
	protected void init() {
		channel.add(Start.class, (msg) -> {
			onMessageReceived(msg);
		});
	}

	@Override
	protected void processMessage(Object message) {
		gui.enable();
		travelInfo = new TravelInfo();
		NodeBehavior.travelInfoBehavior(travelInfo);
		gui.notify(travelInfo, null);
	}	

	@Override
	public void next() {
		channel.broadcast(new TravelInfoNodeDone(travelInfo));
		channel.broadcast(new Start());
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
