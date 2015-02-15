package nodes;

import model.BillingInfo;
import nodes.TravelInfoNode.TravelInfoNodeDone;
import util.NodeBehavior;
import channel.Channel;

public class BillingInfoNode extends AbstractNode {

	private BillingInfo billingInfo;
	
	private Object lock = new Object();
	

	public BillingInfoNode(Channel channel) {
		super(channel);

		channel.add(TravelInfoNodeDone.class, msg -> {
			try {
				queue.put(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		start(()->{
			while (true) {
				try {
					queue.take();
				} catch (Exception e) {
					e.printStackTrace();
				}
				gui.enable();
				
				billingInfo = new BillingInfo();
				NodeBehavior.billingInfoBehavior(billingInfo);
				
				gui.notify(null, billingInfo);
				
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
		channel.broadcast(new BillingInfoNodeDone(billingInfo));
		synchronized (lock) {
			lock.notify();		
		}
	}

	public static class BillingInfoNodeDone {
		private BillingInfo billingInfo;

		public BillingInfoNodeDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}
		
		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}
