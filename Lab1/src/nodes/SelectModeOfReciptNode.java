package nodes;

import model.BillingInfo;
import nodes.PaymentInfoNode.PaymentInfoNodeDone;
import util.NodeBehavior;
import channel.Channel;

public class SelectModeOfReciptNode extends AbstractNode {

	private Object lock = new Object();
	private boolean delivery;
	private BillingInfo billingInfo;

	public SelectModeOfReciptNode(Channel channel) {
		super(channel);

		channel.add(PaymentInfoNodeDone.class, msg -> {
			try {
				queue.put(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		start(() -> {
			while (true) {
				PaymentInfoNodeDone msg = null;
				try {
					msg = (PaymentInfoNodeDone) queue.take();
				} catch (Exception e) {
					e.printStackTrace();
				}
				gui.enable();
				
				billingInfo = msg.getBillingInfo();
				delivery = NodeBehavior.isDelivery();

				synchronized (lock) {
					try {
						lock.wait();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void next() {
		gui.disable();
		channel.broadcast(new SelectModeOfReciptDone(billingInfo, delivery));
		synchronized (lock) {
			lock.notify();
		}
	}
	
	public static class SelectModeOfReciptDone {
		private boolean isDelivery;
		private BillingInfo billingInfo;

		public SelectModeOfReciptDone(BillingInfo billingInfo, boolean isDelivery) {
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

}
