package nodes;

import java.sql.Timestamp;
import java.util.Random;

import messages.Start;
import model.TravelInfo;
import util.Generator;
import channel.Channel;

public class TravelInfoNode extends AbstractNode {

	private TravelInfo travelInfo;

	public TravelInfoNode(Channel channel) {
		super(channel);

		Random random = new Random();

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
				travelInfo.setId(random.nextInt());
				travelInfo.setDestination(Generator.generateString(random, 10));
				travelInfo.setOrigin(Generator.generateString(random, 10));
				travelInfo.setNrOfPassengers(random.nextInt());
				travelInfo.setTravelDate(Generator.generateDate(random,
						Timestamp.valueOf("2013-01-01 00:00:00").getTime(),
						Timestamp.valueOf("2013-12-31 00:58:00").getTime()));
				travelInfo.setReturnDate(Generator.generateDate(random,
						Timestamp.valueOf("2014-01-01 00:00:00").getTime(),
						Timestamp.valueOf("2014-12-31 00:58:00").getTime()));
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
