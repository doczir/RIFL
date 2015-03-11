package nodes;

import gui.GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import node.Node;
import util.Main;
import util.Serializer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public abstract class AbstractNode implements Node {	
	protected GUI gui;
	protected Object lock;
	protected Channel channel;
	
	protected List<QueueingConsumer> consumers = null;
	

	public AbstractNode() throws IOException {
		lock = new Object();

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Main.CHANNEL_ADDRESS);
		Connection connection = factory.newConnection();
		channel = connection.createChannel();

		consumers = new ArrayList<QueueingConsumer>();
		
		init();

		new Thread(() -> {
			while (true) {
				try {
					processMessage(nextMessage());

					synchronized (lock) {
						try {
							lock.wait();
						} catch (Exception e) {
							Logger.getLogger(
									AbstractNode.this.getClass().getSimpleName())
									.severe("An exception occured: "
											+ e.getMessage());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Logger.getLogger(this.getClass().getSimpleName()).severe(
							"An exception occured: " + e.getMessage());
				}
			}
		}).start();
	}

	@Override
	public void setGui(GUI gui) {
		this.gui = gui;
	}


	protected Object nextMessage() throws Exception {
		Object result = null;

		try {
			//result = queue.take();
			result = Serializer.deserialize(consumers.get(0).nextDelivery().getBody());

			if (gui != null) {
				gui.setQueueSize(-1);
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getSimpleName()).severe(
					"An exception occured: " + e.getMessage());
			
			e.printStackTrace();
		}

		return result;
	}

	protected abstract void init() throws IOException;

	protected abstract void processMessage(Object message);
}
