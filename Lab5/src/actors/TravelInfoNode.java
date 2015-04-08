package actors;

import gui.GUI;
import model.TravelInfo;
import util.MessageHelper;
import util.NodeBehavior;
import akka.actor.Props;

public class TravelInfoNode extends AbstractNode {

	private TravelInfo travelInfo;

	public TravelInfoNode() {
		super();
		new GUI("TravelInfoNode", this);
	}

	@Override
	protected void internalProcessMessage(Object message) {
		gui.enable();
		travelInfo = new TravelInfo();
		NodeBehavior.travelInfoBehavior(travelInfo);
		gui.notify(travelInfo, null);
	}

	@Override
	public void next() {
		gui.disable();
		getContext().actorSelection(
				MessageHelper.getActorAddress(BillingInfoNode.class)).tell(
				new TravelInfoNodeDone(travelInfo), self());
		getContext().actorSelection(
				MessageHelper.getActorAddress(ProcessReservationNode.class))
				.tell(new TravelInfoNodeDone(travelInfo), self());
		self().tell(new Start(), self());
		super.next();
	}

	public static class Start {
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

	public static Props props() {
		return Props.create(TravelInfoNode.class, TravelInfoNode::new);
	}

}
