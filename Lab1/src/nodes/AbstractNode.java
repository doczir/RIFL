package nodes;

import gui.GUI;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import messages.Start;
import node.Node;
import channel.Channel;

public abstract class AbstractNode implements Node {

	protected GUI gui;
	protected Channel channel;
	protected BlockingQueue queue;

	public AbstractNode(Channel channel) {
		this.channel = channel;
		this.queue = new ArrayBlockingQueue<Object>(10);
	}

	protected void start(Runnable r) {
		new Thread(r).start();
	}

	@Override
	public void setGui(GUI gui) {
		this.gui = gui;
	}

}
