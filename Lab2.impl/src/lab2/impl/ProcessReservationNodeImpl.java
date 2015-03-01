package lab2.impl;

import lab2.inf.PaymentInfoNode;
import lab2.inf.ProcessReservationNode;

import commonosgi.gui.GUI;
import commonosgi.model.TravelInfo;
import commonosgi.util.NodeBehavior;

public class ProcessReservationNodeImpl extends AbstractNode implements
		ProcessReservationNode {

	private PaymentInfoNode pin;
	private TravelInfo travelInfo;

	public ProcessReservationNodeImpl() {
		super();

		new GUI("ProcessReservation node", this);
	}

	@Override
	public void next() {
		if (this.travelInfo != null && pin != null) {
			pin.processReservationFinished(travelInfo);

			travelInfo = null;

			gui.disable();
		}

		if (!queue.isEmpty()) {
			this.travelInfo = (TravelInfo) queue.poll();

			NodeBehavior.processReservationBehavior(this.travelInfo);

			gui.notify(travelInfo, null);

			gui.enable();
		}
		
		gui.setQueueSize(queue.size());		
	}

	@Override
	public void processReservation(TravelInfo travelInfo) {
		if (queue.isEmpty() && this.travelInfo == null) {
			this.travelInfo = travelInfo;

			NodeBehavior.processReservationBehavior(this.travelInfo);

			gui.notify(travelInfo, null);

			gui.enable();
		} else {
			queue.add(travelInfo);
		}
		
		gui.setQueueSize(queue.size());
	}

	public void setPin(PaymentInfoNode pin) {
		this.pin = pin;
	}

	public void unsetPin(PaymentInfoNode pin) {
		if (this.pin == pin) {
			pin = null;
		}
	}
}
