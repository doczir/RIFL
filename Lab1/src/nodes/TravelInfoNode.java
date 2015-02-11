package nodes;

import java.sql.Timestamp;
import java.util.Random;

import messages.Start;
import messages.TravelInfoNodeDone;
import model.TravelInfo;
import node.Node;
import util.Generator;
import channel.Channel;

public class TravelInfoNode implements Node {

	private Channel channel;
	private TravelInfo travelInfo;

	public TravelInfoNode(Channel channel) {
		this.channel = channel;

		Random random = new Random();

		channel.add(Start.class, (msg) -> {
			travelInfo = new TravelInfo();
			travelInfo.setId(random.nextInt());
			travelInfo.setDestination(Generator.generateString(random, 10));
			travelInfo.setOrigin(Generator.generateString(random, 10));
			travelInfo.setNrOfPassengers(random.nextInt());
			travelInfo.setTravelDate(Generator.generateDate(random, Timestamp
					.valueOf("2013-01-01 00:00:00").getTime(), Timestamp
					.valueOf("2013-12-31 00:58:00").getTime()));
			travelInfo.setReturnDate(Generator.generateDate(random, Timestamp
					.valueOf("2014-01-01 00:00:00").getTime(), Timestamp
					.valueOf("2014-12-31 00:58:00").getTime()));
		});
	}

	@Override
	public void next() {
		channel.broadcast(new TravelInfoNodeDone(travelInfo));
	}

	@Override
	public void setGui() {

	}

}
