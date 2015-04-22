package nodes;

import model.TravelInfo;
import util.NodeBehavior;
import channel.BaseMessage;
import channel.Channel;

public class TravelInfoNode extends AbstractNode {

	private static int i = 0;
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
		id = ((Start) message).getId();
		travelInfo = new TravelInfo();
		NodeBehavior.travelInfoBehavior(travelInfo);
		gui.notify(travelInfo, null);
	}

	@Override
	public void next() {
		channel.broadcast(new TravelInfoNodeDone(travelInfo, id));
		channel.broadcast(new Start(++i));
		synchronized (lock) {
			lock.notify();
		}
	}

	public static class Start extends BaseMessage {

		public Start(int id) {
			setId(id);
		}
	}

	public static class TravelInfoNodeDone extends BaseMessage {
		private TravelInfo travelInfo;

		public TravelInfoNodeDone(TravelInfo travelInfo, int id) {
			super();
			this.travelInfo = travelInfo;
			setId(id);
		}

		public TravelInfo getTravelInfo() {
			return travelInfo;
		}
	}

}
