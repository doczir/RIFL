package nodes;

import gui.GUI;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.IntSupplier;
import java.util.logging.Logger;

import node.Node;
import channel.Channel;

public abstract class AbstractNode implements Node {

	protected GUI gui;
	protected Channel channel;
	protected BlockingQueue<Object> queue;
	protected Object lock;
	
	protected int id;
	protected boolean automatic;
	protected IntSupplier delay;

	public AbstractNode(Channel channel, boolean automatic, IntSupplier delay) {
		this.channel = channel;
		this.automatic = automatic;
		this.delay = delay;
		this.queue = new ArrayBlockingQueue<Object>(10);
		lock = new Object();
		
		init();
		
		new Thread(() -> {
			while (true) {
				try {
					processMessage(nextMessage());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				synchronized (lock) {
					try {
						lock.wait();
					} catch (Exception e) {
						Logger.getLogger(AbstractNode.this.getClass().getSimpleName()).severe("An exception occured: " + e.getMessage());
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
			Logger.getLogger(this.getClass().getSimpleName()).severe("An exception occured: " + e.getMessage());
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
			Logger.getLogger(this.getClass().getSimpleName()).severe("An exception occured: " + e.getMessage());
		}
		
		return result;
	}
	
	protected abstract void init();
	
	protected abstract void processMessage(Object message) throws InterruptedException;
}