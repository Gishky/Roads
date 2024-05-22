package AdminConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminConsoleConnection extends Thread {
	private PrintWriter out;
	private BufferedReader in;

	public void run() {
		synchronized (AdminConsole.getLog()) {
			for(String s : AdminConsole.getLog()) {
				sendMessage(s);
			}
		}
		String inputLine;
		try {
			inputLine = in.readLine();
			while ((inputLine = in.readLine()) != null) {
				AdminConsole.log(MessageInterpreter.receivedMessage(inputLine));
			}
		} catch (IOException e) {
			AdminConsole.removeConnection(this);
		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}

	public void setWriter(PrintWriter out) {
		this.out = out;
	}

	public void setReader(BufferedReader in) {
		this.in = in;
	}
}
