package util;

import gui.GUI;
import gui.SMORGUI;

import javax.jms.BytesMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

import nodes.BillingInfoNode;
import nodes.DeliveryAddressNode;
import nodes.PaymentInfoNode;
import nodes.ProcessPaymentNode;
import nodes.ProcessReservationNode;
import nodes.SelectModeOfReciptNode;
import nodes.TravelInfoNode;

public class Main {


	public static String CHANNEL_ADDRESS;

	public static void main(String[] args) throws Exception {
		
		new GUI("TravelInfoNode", new TravelInfoNode());
		new GUI("ProcessReservationNode", new ProcessReservationNode());
		new GUI("BillingInfoNode", new BillingInfoNode());
		new GUI("PaymentInfoNode", new PaymentInfoNode());
		new GUI("ProcessPaymentNode", new ProcessPaymentNode());
		new SMORGUI("SelectModeOfReciptNode", new SelectModeOfReciptNode());
		new GUI("DeliveryAddress", new DeliveryAddressNode());
		

		// SEND FIRST START MESSAGE
		InitialContext initialContext = new InitialContext();
		
		QueueConnectionFactory qcf = (QueueConnectionFactory) initialContext.lookup("QueueConnectionFactory");
		
		QueueConnection connection = qcf.createQueueConnection();
		
		connection.start();
		
		QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		
		Queue queue = (Queue) initialContext.lookup("queue/start");
		
		QueueSender sender = session.createSender(queue);
		
		BytesMessage message = session.createBytesMessage();
		message.writeBytes(Serializer.serialize("Start"));
		
		sender.send(message);
	}
}