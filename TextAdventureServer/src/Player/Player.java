package Player;

import java.util.ArrayList;

import Networking.ClientConnection;

public class Player {
	private String name = null;
	private String password = null;
	
	private ArrayList<ClientConnection> sessions = new ArrayList<ClientConnection>();
	
	public Player(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public void messageReceived(String message) {
		System.out.println(name+" received: "+message);
	}
	
	private void sendMessage(String message) {
		for(ClientConnection connection : sessions) {
			connection.sendMessage(message);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<ClientConnection> getSessions() {
		return sessions;
	}

	public void setSessions(ArrayList<ClientConnection> sessions) {
		this.sessions = sessions;
	}
	
}
