package util;

import gui.GUI;
import messages.Start;
import nodes.TravelInfoNode;
import channel.Channel;

public class Main {

	public static void main(String[] args) {
		Channel channel = new Channel();
		
		TravelInfoNode travelInfoNode = new TravelInfoNode(channel);
		new GUI("TravelInfo", travelInfoNode);
		
		channel.broadcast(new Start());
	}
	
}