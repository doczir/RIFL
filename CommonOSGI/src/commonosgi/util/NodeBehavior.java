package commonosgi.util;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import commonosgi.model.BillingInfo;
import commonosgi.model.TravelInfo;

public class NodeBehavior {

	private NodeBehavior() {
	}

	public static void travelInfoBehavior(TravelInfo travelInfo) {
		Random random = ThreadLocalRandom.current();
		travelInfo.setId(random.nextInt());
		travelInfo.setDestination(Generator.generateString(random, 10));
		travelInfo.setOrigin(Generator.generateString(random, 10));
		travelInfo.setNrOfPassengers(random.nextInt());
		travelInfo.setTravelDate(Generator.generateDate(random, Timestamp
				.valueOf("2013-01-01 00:00:00").getTime(),
				Timestamp.valueOf("2013-12-31 00:58:00").getTime()));
		travelInfo.setReturnDate(Generator.generateDate(random, Timestamp
				.valueOf("2014-01-01 00:00:00").getTime(),
				Timestamp.valueOf("2014-12-31 00:58:00").getTime()));
	}

	public static void processReservationBehavior(TravelInfo travelInfo) {
		travelInfo.setReservationAccepted(true);
	}

	public static void billingInfoBehavior(BillingInfo billingInfo) {
		Random random = ThreadLocalRandom.current();
		billingInfo.setBillingName(Generator.generateString(random, 15));
		billingInfo.setBillingAddress(Generator.generateString(random, 15));
	}

	public static void paymentInfoBehavior(BillingInfo billingInfo) {
		Random random = ThreadLocalRandom.current();
		billingInfo.setCreditCardNr(Generator.generateString(random, 30));
		billingInfo.setCreditCardOwner(Generator
				.generateString(random, 15));		
	}

	public static void processPaymentBehavior(BillingInfo billingInfo) {
		billingInfo.setInFullDischarge(true);		
	}

	public static boolean isDelivery() {
		Random random = ThreadLocalRandom.current();
		return random.nextBoolean();
	}

	public static void deliveryAddressBehavior(BillingInfo billingInfo) {
		Random random = ThreadLocalRandom.current();
		billingInfo.setDeliveryAddress(Generator.generateString(random, 30));
	}

}
