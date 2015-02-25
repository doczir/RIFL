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
		
		System.out.println("TIN constructor");
		
		new GUI("TravelInfo node", this);
		
		createTravelInfo();
		
		gui.enable();
	}
	
	@Override
	public void next() {
		System.out.println("TIN next");
		
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
		System.out.println("TIN createTravelInfo");
		
		
		travelInfo = new TravelInfo();		
		NodeBehavior.travelInfoBehavior(travelInfo);
		
		gui.notify(travelInfo, null);
	}

	public void setBin(BillingInfoNode bin) {
		System.out.println("TIN setBIN");
		
		
		this.bin = bin;
	}

	public void setPrn(ProcessReservationNode prn) {
		System.out.println("TIN setPRN");
		
		
		this.prn = prn;
	}
	
	public void unsetBin(BillingInfoNode bin) {
		System.out.println("TIN unsetBIN");
		
		
		if (this.bin == bin) {
			this.bin = null;
		}
	}

	public void unsetPrn(ProcessReservationNode prn) {
		System.out.println("TIN unsetPRN");
		
		
		if (this.prn == prn) {
			this.prn = null;
		}
	}
}
