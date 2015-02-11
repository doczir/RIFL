package nodes;

import model.TravelInfo;
import nodes.TravelInfoNode.TravelInfoNodeDone;
import channel.Channel;

public class ProcessReservationNode extends AbstractNode {

	private TravelInfo travelInfo;
	private Object lock = new Object();

	public ProcessReservationNode(Channel channel) {
		super(channel);

		channel.add(TravelInfoNodeDone.class, (msg) -> {
			try {
				queue.put(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		start(() -> {
			{
				while (true) {
					TravelInfoNodeDone msg = null;
					try {
						msg = (TravelInfoNodeDone) queue.take();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					gui.enable();
					travelInfo = msg.getTravelInfo();
					travelInfo.setReservationAccepted(true);
					gui.notify(travelInfo, null);
					synchronized (lock) {
						try {
							lock.wait();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
	}

	@Override
	public void next() {
		gui.disable();
		channel.broadcast(new ProcessReservationNodeDone(travelInfo));
		synchronized (lock) {
			lock.notify();		
		}		
	}
	
	public static class ProcessReservationNodeDone {
		TravelInfo travelInfo;
		
		public ProcessReservationNodeDone(TravelInfo travelInfo) {
			super();
			this.travelInfo = travelInfo;
		}



		public TravelInfo getTravelInfo() {
			return travelInfo;
		}
	}

}
