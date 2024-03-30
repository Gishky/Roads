package Networking;

import java.net.*;
import java.io.*;

public class PortListener extends Thread{
	
	private final int port;
	
	public PortListener(int port) {
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
				ClientConnection clientConnection = new ClientConnection();
				clientConnection.setWriter(new PrintWriter(connection.getOutputStream(), true));
				clientConnection.setReader(new BufferedReader(new InputStreamReader(connection.getInputStream())));
				clientConnection.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
}