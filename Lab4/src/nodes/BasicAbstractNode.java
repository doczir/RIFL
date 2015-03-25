package nodes;

import java.util.logging.Logger;

import javax.jms.BytesMessage;
import javax.jms.QueueReceiver;

import util.Serializer;

public abstract class BasicAbstractNode extends AbstractNode {
	protected BytesMessage lastMessage;

	protected QueueReceiver receiver;

	public BasicAbstractNode() throws Exception {
		super();

		if (receiver == null) {
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
			lastMessage = (BytesMessage) receiver.receive();

			byte[] data = new byte[(int) lastMessage.getBodyLength()];
			lastMessage.readBytes(data);
			
			result = Serializer.deserialize(data);

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
