package Lab0;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tester {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {
		if (args.length != 3) {
			System.out.println("You should specify configuration file, hostname and clock type");
			System.out.println("Usage: 'configurationFile' 'hostname'");
			return;
		}

		System.out.println("I am " + args[1]);
		MessagePasser parser = new MessagePasser(args[0], args[1], args[2]);
		Listener listener = new Listener();
		listener.start();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out
				.println("The format should be: send receiver messageKind messageBody OR receive");
		while (true) {
			String command = br.readLine();
			String[] arguments = command.split(" ");
			if (arguments.length != 4 && arguments.length != 1) {
				System.out
						.println("The format should be: send receiver messageKind messageBody OR receive");
			} else {
				if (arguments.length == 4) {
					parser.send(generateMessage(arguments));
				} else {
					parser.receive();
				}
			}
		}

	}

	public static Message generateMessage(String[] arguments) {
		return new Message(arguments[1], arguments[2], arguments[3]);
	}
}
