package nodes;

import gui.GUI;

import java.io.IOException;

import node.Node;
import util.Main;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class AbstractNode implements Node {	
	protected GUI gui;
	protected Object lock;
	protected Channel channel;
	

	public AbstractNode() throws IOException {
		lock = new Object();

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Main.CHANNEL_ADDRESS);
		Connection connection = factory.newConnection();
		channel = connection.createChannel();
		
		init();
	}

	@Override
	public void setGui(GUI gui) {
		this.gui = gui;
	}


	protected abstract void init() throws IOException;

	protected abstract void processMessage(Object message);
}
