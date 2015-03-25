package model;

import java.io.Serializable;
import java.util.Date;

public class TravelInfo implements Serializable {
	private int id;
	private String origin;
	private String destination;
	private Date travelDate;
	private Date returnDate;
	private int nrOfPassengers;
	private boolean reservationAccepted;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(Date travelDate) {
		this.travelDate = travelDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public int getNrOfPassengers() {
		return nrOfPassengers;
	}

	public void setNrOfPassengers(int nrOfPassengers) {
		this.nrOfPassengers = nrOfPassengers;
	}

	public boolean isReservationAccepted() {
		return reservationAccepted;
	}

	public void setReservationAccepted(boolean reservationAccepted) {
		this.reservationAccepted = reservationAccepted;
	}

}
