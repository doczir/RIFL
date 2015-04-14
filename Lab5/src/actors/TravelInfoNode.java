package actors;

import java.io.Serializable;

import gui.GUI;
import model.TravelInfo;
import util.MessageHelper;
import util.NodeBehavior;
import akka.actor.Props;
import akka.japi.Creator;

public class TravelInfoNode extends AbstractNode {

	private TravelInfo travelInfo;

	public TravelInfoNode() {
		super();
		new GUI("TravelInfoNode", this);
	}

	@Override
	protected void internalProcessMessage(Object message) {
		travelInfo = new TravelInfo();
		NodeBehavior.travelInfoBehavior(travelInfo);
		gui.notify(travelInfo, null);
		gui.enable();
	}

	@Override
	public synchronized void next() {
		gui.disable();
		getContext().actorSelection(
				MessageHelper.getActorAddress(BillingInfoNode.class)).tell(
				new TravelInfoNodeDone(travelInfo), self());
		getContext().actorSelection(
				MessageHelper.getActorAddress(ProcessReservationNode.class))
				.tell(new TravelInfoNodeDone(travelInfo), self());
		super.next();
		self().tell(new Start(), self());
	}

	public static class Start implements Serializable  {
	}

	public static class TravelInfoNodeDone implements Serializable {
		private TravelInfo travelInfo;

		public TravelInfoNodeDone(TravelInfo travelInfo) {
			super();
			this.travelInfo = travelInfo;
		}

		public TravelInfo getTravelInfo() {
			return travelInfo;
		}
	}

	public static Props props() {
		return Props.create(TravelInfoNode.class,
				new Creator<TravelInfoNode>() {

					@Override
					public TravelInfoNode create() throws Exception {
						return new TravelInfoNode();
					}
				});
	}

}
