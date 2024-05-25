package AdminConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class AdminConsole extends Thread {

	private final int port;
	private static ArrayList<String> log = new ArrayList<String>();
	private static ArrayList<AdminConsoleConnection> connections = new ArrayList<AdminConsoleConnection>();

	public AdminConsole(int port) {
		this.port = port;
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// listen for connections
		while (serverSocket != null) {
			Socket connection;
			try {
				connection = serverSocket.accept();
				AdminConsoleConnection adminConnection = new AdminConsoleConnection();
				adminConnection.setWriter(new PrintWriter(connection.getOutputStream(), true));
				adminConnection.setReader(new BufferedReader(new InputStreamReader(connection.getInputStream())));
				adminConnection.start();
				synchronized (connections) {
					connections.add(adminConnection);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void log(String s, boolean supressTime) {
		LocalTime time = LocalTime.now();
		synchronized (log) {
			if (!supressTime)
				s = time.getHour() + ":" + time.getMinute() + "-" + s;
			log.add(s);
			synchronized (connections) {
				for (AdminConsoleConnection con : connections) {
					con.sendMessage(s);
				}
			}
		}
	}

	public static void removeConnection(AdminConsoleConnection con) {
		synchronized (connections) {
			connections.remove(con);
		}
	}

	public static ArrayList<String> getLog() {
		return log;
	}
}
