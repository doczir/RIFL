package util;

import gui.GUI;
import gui.SMORGUI;
import nodes.BillingInfoNode;
import nodes.DeliveryAddressNode;
import nodes.PaymentInfoNode;
import nodes.ProcessPaymentNode;
import nodes.ProcessReservationNode;
import nodes.SelectModeOfReciptNode;
import nodes.TravelInfoNode;
import nodes.TravelInfoNode.Start;
import channel.Channel;

public class Main {


	public static void main(String[] args) throws InterruptedException {
		Channel channel = new Channel();
		
		
		new GUI("TravelInfoNode", new TravelInfoNode(channel));
		new GUI("ProcessReservationNode", new ProcessReservationNode(channel));
		new GUI("BillingInfoNode", new BillingInfoNode(channel));
		new GUI("PaymentInfoNode", new PaymentInfoNode(channel));
		new GUI("ProcessPaymentNode", new ProcessPaymentNode(channel));
		new SMORGUI("SelectModeOfReciptNode", new SelectModeOfReciptNode(channel));
		new GUI("DeliveryAddress", new DeliveryAddressNode(channel));
		
		channel.broadcast(new Start());
	}
}