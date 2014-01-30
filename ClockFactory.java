
public class ClockFactory {
	
	 private ClockService clockservice = null;
	 private MessagePasser mp;
     
     public ClockFactory(MessagePasser mp)
     {
             this.mp = mp;
     }

     public ClockService getClockType()
     {
    	 /* We have 2 ways of specifying logical or vector clock:
    	  * 1. Add in the config.yaml file
    	  * 2. Mention this when we run: java -jar abc.jar config.yaml process_name logical/vector
    	  * 
    	  * Either case, we need to set some String variable as logical/vector and call some method
    	  * in MessagePasser to get the value assigned in this String. Here this method is 
    	  * "mp.getClockType()"
    	  *  */
             if(clockservice == null)
             {
                     if(mp.getClockType().equals("logical"))
                     {
                    	 clockservice = new LogicalClock();
                     }
                     else if(mp.getClockType().equals("Vector"))
                     {
                    	 /* param1: Total number of users(processes) to create a timeStamp array of that size
                    	  * param2: Id of the user(process)
                    	  */
                    	 clockservice = new VectorClock(mp.getUsers().size(), mp.getId());
                     }
                     else
                     {
                             System.err.println("Error in clock type");
                             System.exit(1);
                     }
             }
             return clockservice;
     }

}
