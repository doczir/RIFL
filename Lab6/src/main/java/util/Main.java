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
		new GUI("ProcessReservationNode", new ProcessReservationNode(channel, true, () -> 1000));
		new GUI("BillingInfoNode", new BillingInfoNode(channel, true, () -> (int) (Math.pow(Math.random(), 40) * 10000.0)));
		new GUI("PaymentInfoNode", new PaymentInfoNode(channel, true, () -> 2000));
		new GUI("ProcessPaymentNode", new ProcessPaymentNode(channel, true, () -> 1000));
		new SMORGUI("SelectModeOfReciptNode", new SelectModeOfReciptNode(channel, true, () -> 2000, 0.4));
		new GUI("DeliveryAddress", new DeliveryAddressNode(channel, true, () -> 3000));
		
		channel.broadcast(new Start(0));
	
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Saving log...");
			DroolsManager.getInstance().insert(new Message());
			DroolsManager.getInstance().setRunning(false);
			try {
				DroolsManager.getInstance().join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
	}
}