package nodes;

import gui.GUI;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import node.Node;
import channel.Channel;

public abstract class AbstractNode implements Node {

	protected GUI gui;
	protected Channel channel;
	protected BlockingQueue<Object> queue;
	protected Object lock;

	public AbstractNode(Channel channel) {
		this.channel = channel;
		this.queue = new ArrayBlockingQueue<Object>(10);
		lock = new Object();
		
		init();
		
		new Thread(() -> {
			while (true) {
				processMessage(nextMessage());
				
				synchronized (lock) {
					try {
						lock.wait();
					} catch (Exception e1) {
						// TODO hibakezeles!
						e1.printStackTrace();
					}
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
			// TODO hibakezeles LOG4J!
			e.printStackTrace();
		}
	}
	
	protected Object nextMessage() {
		Object result = null;
		
		try {
			result = queue.take();
			
			if (gui != null) {
				gui.setQueueSize(queue.size());
			}
		} catch (Exception e) {
			// TODO hibakezeles LOG4J
			e.printStackTrace();
		}
		
		return result;
	}
	
	protected abstract void init();
	
	protected abstract void processMessage(Object message);
}
