package UDPServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class UDPServer extends Thread {

	private static ArrayList<UDPServer> instances = new ArrayList<UDPServer>();

	private int port;
	private UDPClientObjectCreator creator;
	private ArrayList<UDPClientConnection> clients;
	private DatagramSocket socket;
	private boolean running;
	private String controlString;

	public UDPServer(String name, int port, UDPClientObjectCreator creator, String controlString) throws Exception {
		for (UDPServer server : instances) {
			if (server.getName() == name) {
				throw new Exception("Cannot start UDPServer; Server with given name already exists");
			}
		}

		this.creator = creator;
		this.port = port;
		this.controlString = controlString;
		setName(name);
		clients = new ArrayList<UDPClientConnection>();
		socket = new DatagramSocket(port);

		instances.add(this);
	}

	public void run() {
		running = true;

		while (running) {
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
				String received = new String(packet.getData(), 0, packet.getLength());
				UDPClientConnection client = getClientWithParameters(packet.getAddress(), packet.getPort());

				if (client != null) {
					client.receivedString(received);
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
		UDPClientConnection client = new UDPClientConnection(address, port, this);
		if (client.checkConnection(new String(packet.getData(), 0, packet.getLength()), controlString)) {
			addClient(client);

			UDPClientObject clientObject = creator.newClientObject();
			client.setClientObject(clientObject);
		}
	}

	public void addClient(UDPClientConnection clientConnection) {
		clients.add(clientConnection);
	}

	public void removeClient(UDPClientConnection clientConnection) {
		clients.remove(clientConnection);
	}

	public void sendToAll(String message, boolean priority) {
		for (UDPClientConnection client : clients) {
			client.sendMessage(message, priority);
		}
	}

	public static ArrayList<UDPServer> getInstances() {
		return instances;
	}

	public DatagramSocket getSocket() {
		return socket;
	}

	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}

	public int getPort() {
		return port;
	}

	public UDPClientConnection getClientWithParameters(InetAddress address, int port) {
		for (UDPClientConnection con : clients) {
			if (con.getAddress().equals(address) && con.getPort() == port) {
				return con;
			}
		}
		return null;
	}

	public void sendMessage(DatagramPacket packet) {
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopServer() {
		running = false;
	}
}