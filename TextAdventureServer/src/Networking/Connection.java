package Networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import GameObjects.Entity;
import GameObjects.PlayerCharacter;
import Server.GameMaster;

public class Connection {
	private InetAddress address;
	private int port;
	private DatagramSocket socket;

	private PlayerCharacter character;

	public Connection(InetAddress address, int port, DatagramSocket socket) {
		this.address = address;
		this.port = port;
		this.socket = socket;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public void sendMessage(String message) {
		System.out.println("sending: "+message);
		String dString = message;
		byte[] buf = dString.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			GameMaster.removeListener(this);
			GameMaster.removeEntity(character);
		}
	}

	public void setPlayerCharacter(PlayerCharacter p) {
		character = p;
	}

	public PlayerCharacter getPlayerCharacter() {
		return character;
	}
}
