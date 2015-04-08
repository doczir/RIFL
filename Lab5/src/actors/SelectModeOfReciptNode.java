package actors;

import gui.GUI;
import gui.SMORGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.BillingInfo;
import util.MessageHelper;
import actors.PaymentInfoNode.PaymentInfoNodeDone;
import akka.actor.Props;

public class SelectModeOfReciptNode extends AbstractNode {

	private boolean delivery;
	private BillingInfo billingInfo;
	
	public SelectModeOfReciptNode() {
		super();
		new SMORGUI("SelectModeOfReciptNode", this);
	}

	@Override
	protected void internalProcessMessage(Object message) {
		PaymentInfoNodeDone msg = (PaymentInfoNodeDone) message;

		gui.enable();

		billingInfo = msg.getBillingInfo();

		gui.notify(null, billingInfo);
	}

	@Override
	public void next() {
		gui.disable();
		if (delivery)
			getContext().actorSelection(
					MessageHelper.getActorAddress(DeliveryAddressNode.class))
					.tell(new SelectModeOfReciptDone(billingInfo, delivery),
							self());
		super.next();
	}

	public void setReceiptDelivery() {
		if (billingInfo != null) {
			delivery = true;

			billingInfo.setDeliveryAddress("-->");

			gui.notify(null, billingInfo);
		}
	}

	public void setReceiptInPersonal() {
		if (billingInfo != null) {
			delivery = false;

			billingInfo.setDeliveryAddress("-");

			gui.notify(null, billingInfo);
		}
	}

	@Override
	public void setGui(GUI gui) {
		super.setGui(gui);

		if (gui instanceof SMORGUI) {
			JButton receiptInPersonalBtn = ((SMORGUI) gui)
					.getReceiptInPersonalBtn();
			JButton receiptDeliveryBtn = ((SMORGUI) gui)
					.getReceiptDeliveryBtn();

			receiptInPersonalBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setReceiptInPersonal();
				}
			});

			receiptDeliveryBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setReceiptDelivery();
				}
			});
		}
	}

	public static class SelectModeOfReciptDone {
		private boolean isDelivery;
		private BillingInfo billingInfo;

		public SelectModeOfReciptDone(BillingInfo billingInfo,
				boolean isDelivery) {
			super();
			this.billingInfo = billingInfo;
			this.isDelivery = isDelivery;
		}

		public boolean isDelivery() {
			return isDelivery;
		}

		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
	
	public static Props props() {
		return Props.create(SelectModeOfReciptNode.class, SelectModeOfReciptNode::new);
	}
}
