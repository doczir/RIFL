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

import util.Serializer;

import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public abstract class JoinAbstractNode extends AbstractNode {
	
	protected Queue<Object> queue;

	protected List<QueueingConsumer> consumers;
	
	protected Map<QueueingConsumer, Integer> consumerKeys;
	
	protected Map<QueueingConsumer, Map<String, Delivery>> consumerMessages; 
	

	public JoinAbstractNode() throws Exception {
		super();
		
		if (consumers == null || consumers.isEmpty()) {
			throw new Exception("A JoinAbstractNode must have at least 2 consumers!");
		}
		
		if (consumerKeys == null || consumerKeys.isEmpty()) {
			throw new Exception("A JoinAbstractNode must have at least 2 consumers and corresponding keys!");
		}
		
		for (QueueingConsumer consumer : consumers) {
			new Thread(() -> {
				while (true) {
					try {
						Delivery delivery = consumer.nextDelivery();
						
						if (delivery.getProperties() == null || delivery.getProperties().getCorrelationId() == null) {
							Logger.getLogger(
									JoinAbstractNode.this.getClass().getSimpleName())
									.severe("The delivered message doesn't contain a correlation id => the message will be dropped...");
						} else {
							Logger.getLogger(this.getClass().getSimpleName()).info("Message arrived!");
							
							Map<String, Delivery> messages = consumerMessages.get(consumer);
							if (messages == null) {
								messages = new HashMap<String, Delivery>();
								
								consumerMessages.put(consumer, messages);
							}
							
							messages.put(delivery.getProperties().getCorrelationId(), delivery);
							
							checkCoherentMessages(delivery);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Logger.getLogger(this.getClass().getSimpleName()).severe(
								"An exception occured: " + e.getMessage());
					}
				}
			}).start();				
		}
	}

	@Override
	protected void init() throws IOException {
		queue = new LinkedList<Object>();
		
		consumers = new ArrayList<QueueingConsumer>();
		
		consumerKeys = new HashMap<QueueingConsumer, Integer>();
		
		consumerMessages = new HashMap<QueueingConsumer, Map<String, Delivery>>();
	}

	protected void checkCoherentMessages(Delivery message) throws ClassNotFoundException, IOException {
		synchronized (lock) {
			String correlationId = message.getProperties().getCorrelationId();
	
			Logger.getLogger(this.getClass().getSimpleName()).info("checkCoherentMessages::correlationId:" + correlationId);
			
			boolean foundAll = true;
			for (QueueingConsumer consumer : consumers) {
				if (consumerMessages.get(consumer) == null || !consumerMessages.get(consumer).containsKey(correlationId)) {
					foundAll = false;
					
					break;
				}
			}
	
			if (foundAll) {
				Logger.getLogger(this.getClass().getSimpleName()).info("Coherent messages were found!");
				
				Map<Integer, Object> result = new HashMap<Integer, Object>();
				
				for (Entry<QueueingConsumer, Map<String, Delivery>> entry : consumerMessages.entrySet()) {
					result.put(consumerKeys.get(entry.getKey()), Serializer.deserialize(entry.getValue().get(correlationId).getBody()));
					
					entry.getValue().remove(entry.getValue().get(correlationId));
				}
				
				queue.add(result);
				processMessage(null);
			}
		}
	}

}
