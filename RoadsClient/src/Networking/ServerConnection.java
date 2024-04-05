package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import GameObjects.World;

public class ServerConnection extends Thread {

	private PrintWriter out;
	private BufferedReader in;

	public ServerConnection() {
		try {
			Socket clientSocket = new Socket("80.109.230.74", 61852);
			//Socket clientSocket = new Socket("localhost", 61852);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		String inputLine;
		try {
			World.playerid = Integer.parseInt(in.readLine());
			while ((inputLine = in.readLine()) != null) {
				for(String s: inputLine.split("\n")) {
					MessageInterpreter.filterMessage(s);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
