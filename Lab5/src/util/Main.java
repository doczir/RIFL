package util;

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

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			return;
		}

		Config config = ConfigFactory
				.parseString(
						"akka.actor.provider = \"akka.remote.RemoteActorRefProvider\" \n"
								+ "akka.remote.enabled-transports = [\"akka.remote.netty.tcp\"] \n"
								+ "akka.remote.netty.hostname = \"192.168.69.128\""
								+ "\n" + "akka.remote.netty.tcp.port=" + (5061 + Integer.parseInt(args[0])))
				.withFallback(ConfigFactory.load());

		ActorSystem system = ActorSystem.create("WorkflowCluster", config);

		ActorRef start = null;

		if (args[0].equals("1")) {
			system.actorOf(BillingInfoNode.props().withDeploy(
					new Deploy(new RemoteScope(new Address("akka.tcp",
							"WorkflowCluster", "192.168.69.128", 5062)))), "bin");
			start = system.actorOf(TravelInfoNode.props().withDeploy(
					new Deploy(new RemoteScope(new Address("akka.tcp",
							"WorkflowCluster", "192.168.69.128", 5062)))), "tin");
			system.actorOf(ProcessReservationNode.props().withDeploy(
					new Deploy(new RemoteScope(new Address("akka.tcp",
							"WorkflowCluster", "192.168.69.128", 5062)))), "prn");
			ActorRef actorOf = system.actorOf(
					PaymentInfoNode.props().withDeploy(
							new Deploy(new RemoteScope(new Address("akka.tcp",
									"WorkflowCluster", "192.168.69.128", 5061)))),
					"pin");
			system.actorOf(
					SelectModeOfReciptNode.props().withDeploy(
							new Deploy(new RemoteScope(new Address("akka.tcp",
									"WorkflowCluster", "192.168.69.128", 5061)))),
					"smorn");
			system.actorOf(
					DeliveryAddressNode.props().withDeploy(
							new Deploy(new RemoteScope(new Address("akka.tcp",
									"WorkflowCluster", "192.168.69.128", 5061)))),
					"dan");
			system.actorOf(
					ProcessPaymentNode.props().withDeploy(
							new Deploy(new RemoteScope(new Address("akka.tcp",
									"WorkflowCluster", "192.168.69.128", 5061)))),
					"ppn");
		}

		MessageHelper.map(BillingInfoNode.class,
				"akka.tcp://WorkflowCluster@192.168.69.128:5062/user/bin");
		MessageHelper.map(TravelInfoNode.class,
				"akka.tcp://WorkflowCluster@192.168.69.128:5062/user/tin");
		MessageHelper.map(ProcessReservationNode.class,
				"akka.tcp://WorkflowCluster@192.168.69.128:5062/user/prn");
		MessageHelper.map(PaymentInfoNode.class,
				"akka.tcp://WorkflowCluster@192.168.69.128:5061/user/pin");
		MessageHelper.map(SelectModeOfReciptNode.class,
				"akka.tcp://WorkflowCluster@192.168.69.128:5061/user/smorn");
		MessageHelper.map(DeliveryAddressNode.class,
				"akka.tcp://WorkflowCluster@192.168.69.128:5061/user/dan");
		MessageHelper.map(ProcessPaymentNode.class,
				"akka.tcp://WorkflowCluster@192.168.69.128:5061/user/ppn");

		if (start != null)
			start.tell(new Start(), null);
	}
}