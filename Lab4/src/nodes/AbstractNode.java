package nodes;

import gui.GUI;

import java.io.IOException;

import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

import node.Node;

public abstract class AbstractNode implements Node {	
	protected GUI gui;
	protected Object lock;
	protected InitialContext initialContext;
	protected QueueConnection connection;
	protected QueueSession session;
	

	public AbstractNode() throws Exception {
		lock = new Object();
		
		initialContext = new InitialContext();
		
		QueueConnectionFactory qcf = (QueueConnectionFactory) initialContext.lookup("QueueConnectionFactory");
		
		connection = qcf.createQueueConnection();
		
		connection.start();
		
		session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		
		init();
	}

	@Override
	public void setGui(GUI gui) {
		this.gui = gui;
	}


	protected abstract void init() throws Exception;

	protected abstract void processMessage(Object message);
}
