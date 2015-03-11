package util;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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


	public static String CHANNEL_ADDRESS;

	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
		
		if (args.length != 1) {
			return;
		}
		
		CHANNEL_ADDRESS = args[0];
		
		new GUI("TravelInfoNode", new TravelInfoNode());
		new GUI("ProcessReservationNode", new ProcessReservationNode());
		new GUI("BillingInfoNode", new BillingInfoNode());
		new GUI("PaymentInfoNode", new PaymentInfoNode());
		new GUI("ProcessPaymentNode", new ProcessPaymentNode());
		new SMORGUI("SelectModeOfReciptNode", new SelectModeOfReciptNode());
		new GUI("DeliveryAddress", new DeliveryAddressNode());
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Main.CHANNEL_ADDRESS);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.basicPublish("EXCHANGE_START", "", null, Serializer.serialize("Start"));

	}
}