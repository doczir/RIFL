package util;

public class Message {

	private int id;
	private Class<?> sender;
	
	private long timestamp;
	private long dur;
	
	public Message() {}

	public Message(int id, Class<?> sender, long timestamp) {
		super();
		this.id = id;
		this.sender = sender;
		this.timestamp = timestamp;
		this.dur = 0L;
	}

	public int getId() {
		return id;
	}

	public Class<?> getSender() {
		return sender;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	
	
	public long getDur() {
		return dur;
	}

	public void setDur(long duration) {
		this.dur = duration;
	}

	@Override
	public String toString() {
		return "Message [sender=" + (sender == null ? "NULL" : sender.getSimpleName()) + ", timestamp="
				+ timestamp + ", duration="	+ dur + "]";
	}
	
	
}
