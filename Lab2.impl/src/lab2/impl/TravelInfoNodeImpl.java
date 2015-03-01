package lab2.impl;

import lab2.inf.BillingInfoNode;
import lab2.inf.ProcessReservationNode;
import lab2.inf.TravelInfoNode;

import commonosgi.gui.GUI;
import commonosgi.model.TravelInfo;
import commonosgi.util.NodeBehavior;

public class TravelInfoNodeImpl extends AbstractNode implements TravelInfoNode {

	private BillingInfoNode bin;
	private ProcessReservationNode prn;
	private TravelInfo travelInfo;

	public TravelInfoNodeImpl() {
		super();

		new GUI("TravelInfo node", this);

		createTravelInfo();

		gui.enable();
	}

	@Override
	public void next() {
		if (bin != null) {
			bin.createBillingInfo();
		}

		if (prn != null) {
			prn.processReservation(travelInfo);
		}

		createTravelInfo();
	}

	@Override
	public void createTravelInfo() {
		travelInfo = new TravelInfo();
		NodeBehavior.travelInfoBehavior(travelInfo);

		gui.notify(travelInfo, null);
	}

	public void setBin(BillingInfoNode bin) {
		this.bin = bin;
	}

	public void setPrn(ProcessReservationNode prn) {
		this.prn = prn;
	}

	public void unsetBin(BillingInfoNode bin) {
		if (this.bin == bin) {
			this.bin = null;
		}
	}

	public void unsetPrn(ProcessReservationNode prn) {
		if (this.prn == prn) {
			this.prn = null;
		}
	}
}
