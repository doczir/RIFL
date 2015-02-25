package lab2.impl;

import java.util.LinkedList;
import java.util.Queue;

import commonosgi.gui.GUI;
import commonosgi.node.Node;

public abstract class AbstractNode implements Node {

	protected Queue queue;
	protected GUI gui;
	

	public AbstractNode() {
		super();
		
		this.queue = new LinkedList();
	}

	@Override
	public void setGui(GUI gui) {
		this.gui = gui;
		// TODO Auto-generated method stub

	}

}
