package Lab1;
import Lab0.Message;


public class TimeStampedMessage extends Message {
	
	private ClockService clockservice;
	
	/* This timestamp is used by both Logical(timestamp is simple int) & Vector(timestamp is int array) classes
	 * So, I'm using the type - Object
	 *  */
	private Object timeStamp;
	
	public TimeStampedMessage(String dest, String kind, Object data, ClockService clockservice, Object timeStamp) {
		super(dest, kind, data);
		this.clockservice = clockservice;
		this.timeStamp = timeStamp;
	}
	
	public ClockService getClockService(){
		return clockservice;
	}

	public Object getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Object timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public int compare(TimeStampedMessage message)
    {
            return clockservice.compare(timeStamp, message.getTimeStamp());
    }
	
	public boolean equal(TimeStampedMessage message)
    {
            return clockservice.equal(timeStamp, message.getTimeStamp());
    }
}
