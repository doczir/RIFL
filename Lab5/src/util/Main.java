package util;

import scala.Console;
import actors.BillingInfoNode;
import actors.DeliveryAddressNode;
import actors.PaymentInfoNode;
import actors.ProcessPaymentNode;
import actors.ProcessReservationNode;
import actors.SelectModeOfReciptNode;
import actors.TravelInfoNode;
import actors.TravelInfoNode.Start;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Deploy;
import akka.remote.RemoteScope;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Main {

	public static String CHANNEL_ADDRESS;
	public static String IP_ADDRESS_0 = "127.0.0.1";
	public static String IP_ADDRESS_1 = "127.0.0.1";
	public static int DEFAULT_PORT_0 = 5061;
	public static int DEFAULT_PORT_1 = 5062;
	public static String SYSTEM_0 = "SYSTEM_0";
	public static String SYSTEM_1 = "SYSTEM_1";

	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			return;
		}



		ActorRef start = null;

		if (args[0].equals("0")) {
			Config config = ConfigFactory
					.parseString(
							"akka.actor.provider = \"akka.remote.RemoteActorRefProvider\","
									+ "akka.remote.transport = \"akka.remote.netty.NettyRemoteTransport\","
									+ "akka.remote.netty.tcp.hostname = \"" + IP_ADDRESS_0 + "\","
									+ "akka.remote.netty.tcp.port=" + DEFAULT_PORT_0)
					.withFallback(ConfigFactory.load());

			ActorSystem system = ActorSystem.create(SYSTEM_0, config);

			start = system.actorOf(TravelInfoNode.props(), "tin");
			system.actorOf(BillingInfoNode.props(), "bin");
			system.actorOf(ProcessReservationNode.props(), "prn");

			if (start != null)
				start.tell(new Start(), null);

		} else if (args[0].equals("1")) {
			Config config = ConfigFactory
					.parseString(
							"akka.actor.provider = \"akka.remote.RemoteActorRefProvider\" \n"
									+ "akka.remote.transport = \"akka.remote.netty.NettyRemoteTransport\" \n"
									+ "akka.remote.netty.tcp.hostname = \"" + IP_ADDRESS_1 + "\" \n"
									+ "akka.remote.netty.tcp.port=" + DEFAULT_PORT_1)
					.withFallback(ConfigFactory.load());

			ActorSystem system = ActorSystem.create(SYSTEM_1, config);
			
			system.actorOf(PaymentInfoNode.props(), "pin");
			system.actorOf(SelectModeOfReciptNode.props(), "smorn");
			system.actorOf(DeliveryAddressNode.props(), "dan");
			system.actorOf(ProcessPaymentNode.props(), "ppn");
		}
		
		MessageHelper.map(BillingInfoNode.class,
				"akka.tcp://"+SYSTEM_0+"@"+IP_ADDRESS_0+":"+DEFAULT_PORT_0+"/user/bin");
		MessageHelper.map(TravelInfoNode.class,
				"akka.tcp://"+SYSTEM_0+"@"+IP_ADDRESS_0+":"+DEFAULT_PORT_0+"/user/tin");
		MessageHelper.map(ProcessReservationNode.class,
				"akka.tcp://"+SYSTEM_0+"@"+IP_ADDRESS_0+":"+DEFAULT_PORT_0+"/user/prn");
		MessageHelper.map(PaymentInfoNode.class,
				"akka.tcp://"+SYSTEM_1+"@"+IP_ADDRESS_1+":"+DEFAULT_PORT_1+"/user/pin");
		MessageHelper.map(SelectModeOfReciptNode.class,
				"akka.tcp://"+SYSTEM_1+"@"+IP_ADDRESS_1+":"+DEFAULT_PORT_1+"/user/smorn");
		MessageHelper.map(DeliveryAddressNode.class,
				"akka.tcp://"+SYSTEM_1+"@"+IP_ADDRESS_1+":"+DEFAULT_PORT_1+"/user/dan");
		MessageHelper.map(ProcessPaymentNode.class,
				"akka.tcp://"+SYSTEM_1+"@"+IP_ADDRESS_1+":"+DEFAULT_PORT_1+"/user/ppn");
		
	}
}