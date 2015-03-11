package nodes;

import java.io.IOException;

import model.TravelInfo;
import util.NodeBehavior;

public class TravelInfoNode extends AbstractNode {

	//in
	private static final String START = "start";
	
	//out
	private static final String TID = "travel_info_done";
	
	private TravelInfo travelInfo;

	public TravelInfoNode() throws IOException {
		super();
	}
	

	@Override
	protected void init() {
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
	public void next() {
//		channel.broadcast(new TravelInfoNodeDone(travelInfo));
//		channel.broadcast(new Start());
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
