package messages;

import model.TravelInfo;

public class TravelInfoNodeDone {
	private TravelInfo travelInfo;
	
	public TravelInfoNodeDone(TravelInfo travelInfo) {
		this.travelInfo = travelInfo;
	}



	public TravelInfo getTravelInfo() {
		return travelInfo;
	}
}
