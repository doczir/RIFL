package nodes;

import java.util.concurrent.ArrayBlockingQueue;

import model.BillingInfo;
import nodes.BillingInfoNode.BillingInfoNodeDone;
import nodes.ProcessReservationNode.ProcessReservationNodeDone;
import channel.Channel;

public class PaymentInfoNode extends AbstractNode {

	private BillingInfo billingInfo;
	private Object lock = new Object();

	public PaymentInfoNode(Channel channel) {
		super(channel);

		ArrayBlockingQueue<ProcessReservationNodeDone> prnd = new ArrayBlockingQueue<ProcessReservationNodeDone>(
				10);
		ArrayBlockingQueue<BillingInfoNodeDone> bind = new ArrayBlockingQueue<BillingInfoNodeDone>(
				10);

		channel.add(ProcessReservationNodeDone.class, msg -> {
			try {
				prnd.put(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		channel.add(BillingInfoNodeDone.class, msg -> {
			try {
				bind.put(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		start(()->{
			BillingInfoNodeDone msg = null;
			try {
				prnd.take();
				msg = bind.take();
			} catch (Exception e) {
				e.printStackTrace();
			}
			billingInfo = msg.getBillingInfo();
			
			gui.notify(null, billingInfo);
			gui.enable();
			synchronized (lock) {
				try {
					lock.wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void next() {		
		synchronized (lock) {
			lock.notify();
		}
		gui.disable();
	}

}
