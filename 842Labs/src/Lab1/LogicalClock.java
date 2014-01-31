package Lab1;

public class LogicalClock implements ClockService<Integer, TimeStampedMessage> {

	private int timeStamp=0;
	
	public LogicalClock(){}
	
	@Override
	public Integer increment() {
		timeStamp = timeStamp+1;
		return timeStamp;
	}

	@Override
	public Integer getTimeStamp() {
		return timeStamp;
	}

	@Override
	public int compare(Integer message1, Integer message2) {
        if((message1 - message2) < 0)
                return -1;
        else if((message1 - message2) > 0)
                return 1;
        else
                return 0;
	}
	
	/* Synchronize the timeStamp of logical clock with the timestamp in the TimeStampedMessage object */
	@Override
	public void clockSynchronize(TimeStampedMessage msg) {
		timeStamp = Math.max((Integer)msg.getTimeStamp(), timeStamp) + 1;
	}

	@Override
	public boolean equal(Integer message1, Integer message2) {
		return message1 == message2;
	}

}
