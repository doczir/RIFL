package lab2.inf;

import commonosgi.model.BillingInfo;
import commonosgi.model.TravelInfo;

public interface PaymentInfoNode {

	public void getPaymentInfo(BillingInfo billingInfo);

	public void processReservationFinished(TravelInfo travelInfo);

}
