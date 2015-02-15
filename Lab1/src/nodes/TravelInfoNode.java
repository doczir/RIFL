package nodes;

import messages.Start;
import model.TravelInfo;
import util.NodeBehavior;
import channel.Channel;

public class TravelInfoNode extends AbstractNode {

	private TravelInfo travelInfo;

	public TravelInfoNode(Channel channel) {
		super(channel);

		channel.add(Start.class, (msg) -> {
			try {
				queue.put(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		start(() -> {
			while (true) {
				try {
					queue.take();
				} catch (Exception e) {
					e.printStackTrace();
				}
				gui.enable();
				travelInfo = new TravelInfo();
				NodeBehavior.travelInfoBehavior(travelInfo);
				gui.notify(travelInfo, null);
			}
		});
	}

	@Override
	public void next() {
		channel.broadcast(new TravelInfoNodeDone(travelInfo));
		channel.broadcast(new Start());
	}

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
