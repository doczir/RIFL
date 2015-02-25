package commonosgi.model;

public class BillingInfo {

	private String creditCardOwner;
	
	private String creditCardNr;
	
	private String billingName;
	
	private String billingAddress;
	
	private String deliveryAddress;
	
	private boolean inFullDischarge;

	public String getCreditCardOwner() {
		return creditCardOwner;
	}

	public void setCreditCardOwner(String creditCardOwner) {
		this.creditCardOwner = creditCardOwner;
	}

	public String getCreditCardNr() {
		return creditCardNr;
	}

	public void setCreditCardNr(String creditCardNr) {
		this.creditCardNr = creditCardNr;
	}

	public String getBillingName() {
		return billingName;
	}

	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public boolean isInFullDischarge() {
		return inFullDischarge;
	}

	public void setInFullDischarge(boolean inFullDischarge) {
		this.inFullDischarge = inFullDischarge;
	}
}
