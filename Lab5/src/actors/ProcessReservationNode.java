package actors;

import gui.GUI;
import model.TravelInfo;
import util.MessageHelper;
import util.NodeBehavior;
import actors.TravelInfoNode.TravelInfoNodeDone;
import akka.actor.Props;

public class ProcessReservationNode extends AbstractNode {

	private TravelInfo travelInfo;

	public ProcessReservationNode() {
		super();
		new GUI("ProcessReservationNode", this);
	}
	
	@Override
	protected void internalProcessMessage(Object message) {
		TravelInfoNodeDone msg = (TravelInfoNodeDone) message;

		gui.enable();
		travelInfo = msg.getTravelInfo();
		NodeBehavior.processReservationBehavior(travelInfo);
		gui.notify(travelInfo, null);
	}

	@Override
	public void next() {
		gui.disable();

		getContext().actorSelection(
				MessageHelper.getActorAddress(PaymentInfoNode.class)).tell(
				new ProcessReservationNodeDone(travelInfo), self());
		super.next();
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
	
	public static Props props() {
		return Props.create(ProcessReservationNode.class, ProcessReservationNode::new);
	}
}
