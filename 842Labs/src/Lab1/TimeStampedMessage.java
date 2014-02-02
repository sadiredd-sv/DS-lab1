package Lab1;
import Lab0.Message;


public class TimeStampedMessage extends Message implements Comparable{
	
	private ClockService clockservice;
	
	/* This timestamp is used by both Logical(timestamp is simple int) & Vector(timestamp is int array) classes
	 * So, I'm using the type - Object
	 *  */
	private Object timeStamp;
	
	public TimeStampedMessage(String dest, String kind, String data, ClockService clockservice) {
		super(dest, kind, data);
		this.clockservice = clockservice;
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

	public int compareTo(TimeStampedMessage message) {
		return clockservice.compare(timeStamp, message.getTimeStamp());
	}

	@Override
	public int compareTo(Object o) {
		return clockservice.compare(timeStamp, ((TimeStampedMessage)o).getTimeStamp());
	}
	
	@Override
	public String toString() {
		String ans = super.toString();
		ans = ans + ", timestamp: " + clockservice.getTimeStampString(timeStamp);
		return ans;
	}
}
