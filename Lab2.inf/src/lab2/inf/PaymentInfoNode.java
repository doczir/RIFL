package lab2.inf;

import commonosgi.model.BillingInfo;

public interface PaymentInfoNode {

	public void getPaymentInfo(BillingInfo billingInfo);

	public void processReservationFinished();

}
