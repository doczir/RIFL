package nodes;

import util.NodeBehavior;
import model.BillingInfo;
import nodes.PaymentInfoNode.PaymentInfoNodeDone;
import nodes.ProcessReservationNode.ProcessReservationNodeDone;
import channel.Channel;

public class ProcessPaymentNode extends AbstractNode {
	
	private Object lock = new Object();
	private BillingInfo billingInfo;

	public ProcessPaymentNode(Channel channel) {
		super(channel);

		channel.add(PaymentInfoNodeDone.class, msg -> {
			try {
				queue.put(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		start(() -> {
			while(true) {
				PaymentInfoNodeDone msg = null;
				try {
					msg = (PaymentInfoNodeDone) queue.take();
				} catch (Exception e) {
					e.printStackTrace();
				}
				gui.enable();
				billingInfo = msg.getBillingInfo();
				NodeBehavior.processPaymentBehavior(billingInfo);
				gui.notify(null, billingInfo);
				synchronized (lock ) {
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
		channel.broadcast(new ProcessPaymentNodeDone(billingInfo));
		synchronized (lock) {
			lock.notify();		
		}	
	}
	
	public static class ProcessPaymentNodeDone {
		private BillingInfo billingInfo;

		public ProcessPaymentNodeDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}
		
		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
	
	
}
