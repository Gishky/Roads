package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import Player.Player;
import Server.MainThread;

public class ClientConnection extends Thread {
    private PrintWriter out;
    private BufferedReader in;
    
    private Player player = null;

    public void run(){
    	String inputLine;
    	try {
        	inputLine = in.readLine();
			while((inputLine = in.readLine()) != null) {
				if(player == null) {
					String name = inputLine.split(";")[0];
					String password = inputLine.split(";")[1];
					if((player = MainThread.getPlayer(name, password)) == null) {
						player = new Player(name,password);
						MainThread.createPlayer(player);
					}
				}else {
					player.messageReceived(inputLine);
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
