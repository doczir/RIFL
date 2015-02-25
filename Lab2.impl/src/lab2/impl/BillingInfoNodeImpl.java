package lab2.impl;

import lab2.inf.BillingInfoNode;

import commonosgi.gui.GUI;

public class BillingInfoNodeImpl extends AbstractNode implements BillingInfoNode {
	
	public BillingInfoNodeImpl() {
		System.out.println("BIN constructor");
	}
	
	@Override
	public void createBillingInfo() {
		System.out.println("BIN createBillingInfo");
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(GUI gui) {
		// TODO Auto-generated method stub
		
	}

}
