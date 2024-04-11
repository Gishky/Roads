package Networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import GameObjects.Block;
import GameObjects.Entity;
import GameObjects.PlayerCharacter;
import GameObjects.World;
import Server.GameMaster;

public class PortListener extends Thread {

	private DatagramSocket socket;

	public PortListener(int port) {
		System.out.println(port);
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
				String received = new String(packet.getData(), 0, packet.getLength());
				Connection con = GameMaster.hasConnection(packet.getAddress(), packet.getPort());
				if (con != null) {
					con.getPlayerCharacter().messageReceived(received);
				} else {
					newClient(packet);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void newClient(DatagramPacket packet) {
		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		Connection client = new Connection(address, port, socket);
		GameMaster.addListeners(client);

		client.sendMessage("" + Entity.getNextID());
		for (Entity e : GameMaster.getEntities()) {
			client.sendMessage("createEntity;" + e.getEntityIdentifier() + ";" + e.getId() + ";"
					+ (int) e.getPos().getX() + ";" + (int) e.getPos().getY());
		}
		PlayerCharacter p = new PlayerCharacter(client);
		client.setPlayerCharacter(p);
		GameMaster.addEntity(p);
		
		for (int i = 0; i < 100; i++)
			client.sendMessage("createWorld;" + World.getWorld().length + ";" + World.getWorld()[0].length);
	}
}