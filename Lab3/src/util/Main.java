package util;

import java.io.IOException;

import gui.GUI;
import gui.SMORGUI;
import nodes.BillingInfoNode;
import nodes.DeliveryAddressNode;
import nodes.PaymentInfoNode;
import nodes.ProcessPaymentNode;
import nodes.ProcessReservationNode;
import nodes.SelectModeOfReciptNode;
import nodes.TravelInfoNode;

public class Main {


	public static void main(String[] args) throws InterruptedException, IOException {		
		new GUI("TravelInfoNode", new TravelInfoNode());
		new GUI("ProcessReservationNode", new ProcessReservationNode());
		new GUI("BillingInfoNode", new BillingInfoNode());
		new GUI("PaymentInfoNode", new PaymentInfoNode());
		new GUI("ProcessPaymentNode", new ProcessPaymentNode());
		new SMORGUI("SelectModeOfReciptNode", new SelectModeOfReciptNode());
		new GUI("DeliveryAddress", new DeliveryAddressNode());
	}
}