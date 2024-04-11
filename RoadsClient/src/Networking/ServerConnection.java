package Networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

import GameObjects.World;

public class ServerConnection extends Thread {

	private int port = 61852;
	private InetAddress address;
	private DatagramSocket socket;

	public ServerConnection() {
		try {
			socket = new DatagramSocket();
			//address = InetAddress.getByName("localhost");
			address = InetAddress.getByName("80.109.230.74");

			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
			socket.send(packet);

			this.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			byte[] buf = new byte[256];

			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			String received = new String(packet.getData(), 0, packet.getLength());
			World.playerid = Integer.parseInt(received);
			while (true) {
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				received = new String(packet.getData(), 0, packet.getLength());
				MessageInterpreter.filterMessage(received);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		byte[] buf = message.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
