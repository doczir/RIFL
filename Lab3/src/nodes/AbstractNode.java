package nodes;

import gui.GUI;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import util.Main;
import node.Node;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class AbstractNode implements Node {

	protected GUI gui;
	protected BlockingQueue<Object> queue;
	protected Object lock;
	protected Channel channel;

	public AbstractNode() throws IOException {
		this.queue = new ArrayBlockingQueue<Object>(10);
		lock = new Object();

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Main.CHANNEL_ADDRESS);
		Connection connection = factory.newConnection();
		channel = connection.createChannel();

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

	protected void onMessageReceived(Object msg) {
		try {
			queue.put(msg);

			if (gui != null) {
				gui.setQueueSize(queue.size());
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getSimpleName()).severe(
					"An exception occured: " + e.getMessage());
		}
	}

	protected Object nextMessage() throws IOException {
		Object result = null;

		try {
			result = queue.take();

			if (gui != null) {
				gui.setQueueSize(queue.size());
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass().getSimpleName()).severe(
					"An exception occured: " + e.getMessage());
		}

		return result;
	}

	protected abstract void init();

	protected abstract void processMessage(Object message);
}
