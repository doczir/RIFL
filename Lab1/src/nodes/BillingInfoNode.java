package nodes;

import java.sql.Timestamp;
import java.util.Random;

import util.Generator;
import model.BillingInfo;
import model.TravelInfo;
import nodes.TravelInfoNode.TravelInfoNodeDone;
import channel.Channel;

public class BillingInfoNode extends AbstractNode {

	private BillingInfo billingInfo;
	
	private Random random = new Random();
	
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
				billingInfo.setBillingName(Generator.generateString(random, 15));
				billingInfo.setBillingAddress(Generator.generateString(random, 15));
				
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
		synchronized (lock) {
			lock.notify();		
		}
	}

	public static class BillingInfoNodeDone {
		BillingInfo billingInfo;

		public BillingInfoNodeDone(BillingInfo billingInfo) {
			super();
			this.billingInfo = billingInfo;
		}
		
		public BillingInfo getBillingInfo() {
			return billingInfo;
		}
	}
}
