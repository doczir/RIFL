package actors;

import gui.GUI;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import node.Node;
import akka.actor.AbstractActor;
import akka.japi.pf.FI.UnitApply;
import akka.japi.pf.ReceiveBuilder;

public abstract class AbstractNode extends AbstractActor implements Node {

	protected GUI gui;
	protected Queue<Object> queue;
	protected boolean processing = false;

	public AbstractNode() {
		this.queue = new LinkedList<Object>();

		init();
	}

	@Override
	public void setGui(GUI gui) {
		this.gui = gui;
	}

	protected synchronized void onMessageReceived(Object msg) {
		if (!queue.isEmpty() || processing)
			queue.add(msg);
		else
			processMessage(msg);

		if (gui != null) {
			gui.setQueueSize(queue.size());
		}
	}

	protected Object nextMessage() {
		Object result = null;
		
		if (queue.isEmpty())
			result = null;
		else
			result = queue.poll();
		
		if (gui != null) {
			gui.setQueueSize(queue.size());
		}
		
		return result;
	}

	protected void init() {
		receive(ReceiveBuilder.matchAny(new UnitApply<Object>() {
			@Override
			public void apply(Object o) throws Exception {
				onMessageReceived(o);
			}
		}).build());
	}

	protected final void processMessage(Object message) {
		if (message != null) {
			processing = true;
			internalProcessMessage(message);
		}
	}

	@Override
	public synchronized void next() {
		processing = false;
		processMessage(nextMessage());
	}

	protected abstract void internalProcessMessage(Object message);
}
