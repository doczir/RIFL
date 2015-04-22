package util;

public class Message {

	public static final int TYPE_PROCESSING_START = 0;
	public static final int TYPE_PROCESSING_END = 1;
	
	private Class<?> sender;
	
	private int type;
	
	private long timestamp;
	
	
	public Message() {}

	public Message(Class<?> sender, int type, long timestamp) {
		super();
		this.sender = sender;
		this.type = type;
		this.timestamp = (long) ((timestamp - DroolsManager.epoch) / 1000000.0);
	}

	public Class<?> getSender() {
		return sender;
	}

	public void setSender(Class<?> sender) {
		this.sender = sender;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Message [sender=" + (sender == null ? "NULL" : sender.getSimpleName()) + ", type=" + type + ", timestamp="
				+ timestamp + "]";
	}
	
	
}
