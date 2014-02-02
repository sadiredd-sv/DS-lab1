package Lab1;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import Lab0.Listener;
import Lab0.Message;
import Lab0.MessagePasser;

public class Logger
{
	private static final String logFile = "log/logged.txt";
	
	/* The foll. variables are accessed by the inner classes: LogHandler and QueueAdder */
	private Socket socket=null;
	private static String processName =null;
	private MessagePasser mp =null;
	
	public static void main(String[] args) throws IOException
	{
		/* args[0]-- config file
		 * args[1]-- process name
		 *  */
		MessagePasser mp = new MessagePasser(args[0], args[1], args[2]);//I havent checked your MessagePasser parameters. So please correct
		//mp.init(); //Same here. Didnt check your method. Please call the appropriate one
		
		Listener listener = new Listener();
		listener.start();
		
		/*LogHandler handler = new LogHandler();
		processName = args[1];
		handler.start(); // Starts the LogHandler written at the bottom of this class*/

		String input = null;
		BufferedReader br = null;
		

		try{
			br = new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				/* Take input from user */
				System.out.println("Please enter the input: quit or log");
				input = br.readLine();
				if( input != null && (input.equals("quit")) )
					System.exit(1);
				
				if(input != null && (input.equals("log")) )
				{
					 /* You are returning Message object. Instead, if you can add those messages to an ArrayList 
					  * and return it, the following code works perfect for sorting those messages. Else, you can
					  * change this implementation:
					  */
						ArrayList<TimeStampedMessage> logInfo = new ArrayList<TimeStampedMessage>();
						for (Message message : MessagePasser.deliverQueue) {
							logInfo.add((TimeStampedMessage)message);
						};

						Collections.sort(logInfo);
						
						boolean flag = false;
						for(int i = 0; i < logInfo.size(); i++)
						{
							if(flag == false && i+1 < logInfo.size())
							{
								// Messages are concurrent
								if(logInfo.get(i).compare(logInfo.get(i+1)) == 0 && logInfo.get(i).equal(logInfo.get(i+1)) == false) 
								{
									flag = true;
									System.out.println("{");
								}
							}
							System.out.println(i + ": " + logInfo.get(i));
							if(flag == true && i+1 < logInfo.size())
							{
								// Messages are not concurrent
								if(logInfo.get(i).compare(logInfo.get(i+1)) != 0)
								{
									flag = false;
									System.out.println("}");
								}
							}
							if(flag == true && i+1 == logInfo.size())
							{
								flag = false;
								System.out.print("}");
								System.out.println("");
							}
						}
				}
				else
					System.out.println("Enter log or quit\n");
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			try{
				if(br != null)
					br.close();
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
	}
	
}

