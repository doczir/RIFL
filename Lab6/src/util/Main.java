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
		
		
		new GUI("TravelInfoNode", new TravelInfoNode(channel, false, () -> 0));
		new GUI("ProcessReservationNode", new ProcessReservationNode(channel, true, () -> 0));
		new GUI("BillingInfoNode", new BillingInfoNode(channel, true, () -> 0));
		new GUI("PaymentInfoNode", new PaymentInfoNode(channel, true, () -> 0));
		new GUI("ProcessPaymentNode", new ProcessPaymentNode(channel, true, () -> 0));
		new SMORGUI("SelectModeOfReciptNode", new SelectModeOfReciptNode(channel, true, () -> 0));
		new GUI("DeliveryAddress", new DeliveryAddressNode(channel, true, () -> 0));
		
		channel.broadcast(new Start(0));
	}
}