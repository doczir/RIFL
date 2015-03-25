package nodes;

import java.util.logging.Logger;

import util.Serializer;

import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public abstract class BasicAbstractNode extends AbstractNode {
	protected Delivery lastMessage;

	protected QueueingConsumer consumer;

	public BasicAbstractNode() throws Exception {
		super();

		if (consumer == null) {
			throw new Exception("A BasicAbstractNode must have a consumer!");
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

				while (true) {
					try {
						processMessage(nextMessage());

						synchronized (lock) {
							try {
								lock.wait();
							} catch (Exception e) {
								Logger.getLogger(
										BasicAbstractNode.this.getClass()
												.getSimpleName()).severe(
										"An exception occured: "
												+ e.getMessage());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						Logger.getLogger(this.getClass().getSimpleName())
								.severe("An exception occured: "
										+ e.getMessage());
					}
				}
			}
		}).start();
	}

	protected Object nextMessage() throws Exception {
		Object result = null;

		try {
			lastMessage = consumer.nextDelivery();

			result = Serializer.deserialize(lastMessage.getBody());

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
}
