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
		
		
		new GUI("TravelInfoNode", new TravelInfoNode(channel, true, Generator2.getInstance()));
		new GUI("ProcessReservationNode", new ProcessReservationNode(channel, true, Generator2.getInstance()));
//		new GUI("BillingInfoNode", new BillingInfoNode(channel, true, () -> (int) (Math.pow(Math.random(), 40) * 10000.0)));
		new GUI("BillingInfoNode", new BillingInfoNode(channel, true, Generator2.getInstance()));

		new GUI("PaymentInfoNode", new PaymentInfoNode(channel, true, Generator2.getInstance()));
		new GUI("ProcessPaymentNode", new ProcessPaymentNode(channel, true, Generator2.getInstance()));
		new SMORGUI("SelectModeOfReciptNode", new SelectModeOfReciptNode(channel, true, Generator2.getInstance(), -1.0));
		new GUI("DeliveryAddress", new DeliveryAddressNode(channel, true, Generator2.getInstance()));
		
		for (int i = 0; i < 5000; i++) {
			channel.broadcast(new Start(i));			
		}
		//	Thread.sleep(1000);
		//StatCollector.flush();
		Runtime.getRuntime().addShutdownHook(new Thread(StatCollector::flush));
	
	}
}