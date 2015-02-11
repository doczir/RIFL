package util;

import gui.GUI;
import messages.Start;
import nodes.ProcessReservationNode;
import nodes.TravelInfoNode;
import channel.Channel;

public class Main {


	public static void main(String[] args) throws InterruptedException {
		Channel channel = new Channel();
		
		
		TravelInfoNode travelInfoNode = new TravelInfoNode(channel);
		new GUI("TravelInfo", travelInfoNode);
		new GUI("ProcessReservationNode", new ProcessReservationNode(channel));
		
		channel.broadcast(new Start());
	}
	
}