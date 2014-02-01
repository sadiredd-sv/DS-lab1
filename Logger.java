import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Logger
{
	private static final String logFile = "log/logged.txt";
	
	/* The foll. variables are accessed by the inner classes: LogHandler and QueueAdder */
	private Socket socket=null;
	private static String processName =null;
	private MessagePasser mp =null;
	
	public static void main(String[] args)
	{
		/* args[0]-- config file
		 * args[1]-- process name
		 *  */
		MessagePasser mp = new MessagePasser(args[0], args[1], null);//I havent checked your MessagePasser parameters. So please correct
		mp.init(); //Same here. Didnt check your method. Please call the appropriate one
		
		LogHandler handler = new LogHandler();
		processName = args[1];
		handler.start(); // Starts the LogHandler written at the bottom of this class

		String input = null;
		BufferedReader br = null;
		FileWriter fw = null;
		ArrayList<TimeStampedMessage> logInfo = new ArrayList<TimeStampedMessage>();

		try{
			br = new BufferedReader(new InputStreamReader(System.in));
			while(true)
			{
				/* Take input from user */
				System.out.print("Please enter the input: quit or log");
				input = br.readLine();
				if( input != null && (input.equals("quit")) )
					System.exit(1);
				
				if(input != null && (input.equals("log")) )
				{
					 /* You are returning Message object. Instead, if you can add those messages to an ArrayList 
					  * and return it, the following code works perfect for sorting those messages. Else, you can
					  * change this implementation:
					  */
						ArrayList<TimeStampedMessage> received = mp.receive(); 
						if(received.size() != 0)
							logInfo.addAll(received);

						Collections.sort(logInfo);
						fw = new FileWriter(logFile);
						
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
							fw.write(logInfo.get(i).toString() + "\n");
						}
						if(fw != null)
							fw.close();
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
	
	
	/* Works similar to a Receiver. It receives Messages from other nodes, stores them
	 * in receive_queue after passing through the rule check. 
	 * */
	public class LogHandler extends Thread
	{
		private ServerSocket serverSocket;

		public void run()
		{
			serverSocket = null;
			
			User user = mp.getUsers().get(processName); //Get the User object from the process Name passed
			try
			{
				serverSocket = new ServerSocket(user.getPort()); //Listen on the port of the process
				while(true)
				{
					socket = serverSocket.accept();
					QueueAdder qAdder = new QueueAdder();
					qAdder.start();
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(serverSocket != null)
					{
						serverSocket.close();
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/* QueueAdder: Adds to the receive queue */
	public class QueueAdder extends Thread
	{
		public void run()
		{
			ObjectInputStream stream = null;
			ConcurrentLinkedQueue<TimeStampedMessage> receiveQueue = mp.getReceiveQueue();
			TimeStampedMessage message = null;
			
			try
			{
				stream	= new ObjectInputStream(socket.getInputStream());
				while(true)
				{
					// It would lead to EOFException when the source process goes offline
					message = (TimeStampedMessage)stream.readObject(); 
					
					/* You will have to customize the following */
					String[] SrcDest = message.getSrc().split("\\$");
					
					if(SrcDest.length == 2)
					{
						message.set_source(SrcDest[0]);
						message.set_dest(SrcDest[1]);
					}
					receiveQueue.add(message);
				}
			}
			catch(EOFException x)
			{
				x.printStackTrace();
			}
			catch(Exception x)
			{
				x.printStackTrace();
			}
			
			finally
			{
				try
				{
					if(stream != null)
						stream.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
}

