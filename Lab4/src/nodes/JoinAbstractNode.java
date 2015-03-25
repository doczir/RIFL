package nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.logging.Logger;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.QueueReceiver;

import util.Serializer;

public abstract class JoinAbstractNode extends AbstractNode {

	public static final String KEY_CORRELATION_ID = "correlation_id";

	protected Queue<Object> queue;

	protected List<QueueReceiver> receivers;

	protected Map<QueueReceiver, Integer> receiverKeys;

	protected Map<QueueReceiver, Map<String, BytesMessage>> consumerMessages;

	public JoinAbstractNode() throws Exception {
		super();

		if (receivers == null || receivers.isEmpty()) {
			throw new Exception(
					"A JoinAbstractNode must have at least 2 consumers!");
		}

		if (receiverKeys == null || receiverKeys.isEmpty()) {
			throw new Exception(
					"A JoinAbstractNode must have at least 2 consumers and corresponding keys!");
		}

		for (final QueueReceiver consumer : receivers) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					while (true) {
						try {
							BytesMessage message = (BytesMessage) consumer
									.receive();

							if (message.getStringProperty(KEY_CORRELATION_ID) == null) {
								Logger.getLogger(
										JoinAbstractNode.this.getClass()
												.getSimpleName())
										.severe("The delivered message doesn't contain a correlation id => the message will be dropped...");
							} else {
								Logger.getLogger(
										this.getClass().getSimpleName()).info(
										"Message arrived!");

								Map<String, BytesMessage> messages = consumerMessages
										.get(consumer);
								if (messages == null) {
									messages = new HashMap<String, BytesMessage>();

									consumerMessages.put(consumer, messages);
								}

								messages.put(message
										.getStringProperty(KEY_CORRELATION_ID),
										message);

								checkCoherentMessages(message);
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
	}

	@Override
	protected void init() throws Exception {
		queue = new LinkedList<Object>();

		receivers = new ArrayList<QueueReceiver>();

		receiverKeys = new HashMap<QueueReceiver, Integer>();

		consumerMessages = new HashMap<QueueReceiver, Map<String, BytesMessage>>();
	}

	protected void checkCoherentMessages(BytesMessage message)
			throws ClassNotFoundException, IOException, JMSException {
		synchronized (lock) {
			String correlationId = message
					.getStringProperty(KEY_CORRELATION_ID);

			Logger.getLogger(this.getClass().getSimpleName()).info(
					"checkCoherentMessages::correlationId:" + correlationId);

			boolean foundAll = true;
			for (QueueReceiver consumer : receivers) {
				if (consumerMessages.get(consumer) == null
						|| !consumerMessages.get(consumer).containsKey(
								correlationId)) {
					foundAll = false;

					break;
				}
			}

			if (foundAll) {
				Logger.getLogger(this.getClass().getSimpleName()).info(
						"Coherent messages were found!");

				byte[] data;
				Map<Integer, Object> result = new HashMap<Integer, Object>();

				for (Entry<QueueReceiver, Map<String, BytesMessage>> entry : consumerMessages
						.entrySet()) {

					data = new byte[(int) entry.getValue().get(correlationId)
							.getBodyLength()];
					entry.getValue().get(correlationId).readBytes(data);

					result.put(receiverKeys.get(entry.getKey()),
							Serializer.deserialize(data));

					entry.getValue().remove(entry.getValue().get(correlationId));
				}

				queue.add(result);
				processMessage(null);
			}
		}
	}

}
