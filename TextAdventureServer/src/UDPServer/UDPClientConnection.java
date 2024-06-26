package UDPServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Timer;

public class UDPClientConnection implements ActionListener {

	private UDPServer server;
	private InetAddress address;
	private int port;

	private UDPClientObject clientObject;

	private final String PRIORITY_MARK = "PRIORITY;";
	private final String PRIORITY_RESPONSE = "RECEIVEDPRIORITY;";

	private ArrayList<String> priorityMessages = new ArrayList<String>();
	private Timer requiredPackageTimer;

	private boolean connectionApproved = false;

	public UDPClientConnection(InetAddress address, int port, UDPServer server) {
		this.server = server;
		this.address = address;
		this.port = port;

		requiredPackageTimer = new Timer(100, this);
		requiredPackageTimer.start();
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public void sendMessage(String message, boolean priority) {
		if (!connectionApproved)
			return;
		if (priority) {
			message = PRIORITY_MARK + message;
			synchronized (priorityMessages) {
				priorityMessages.add(message);
			}
		}
		byte[] buf = message.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		server.sendMessage(packet);
	}

	private int connectionTimeoutCounter = 100;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!connectionApproved)
			return;
		connectionTimeoutCounter--;
		if (connectionTimeoutCounter <= 0) {
			disconnect();
		}

		synchronized (priorityMessages) {
			for (int i = 0; i < Math.min(10, priorityMessages.size()); i++) {
				if (priorityMessages.get(0) != null) {
					sendMessage(priorityMessages.get(0), false);
					Collections.rotate(priorityMessages, -1);
				} else {
					priorityMessages.remove(0);
				}
			}
		}
	}

	public void setClientObject(UDPClientObject clientobject) {
		clientObject = clientobject;
		clientObject.setClientConnection(this);
	}

	public UDPClientObject getClientObject() {
		return clientObject;
	}

	public void receivedString(String receivedString) {
		if (!connectionApproved)
			return;
		connectionTimeoutCounter = 10;
		if (receivedString.startsWith("NetworkPingRequest")) {
			sendMessage(receivedString, false);
		} else if (receivedString.startsWith(PRIORITY_RESPONSE)) {
			synchronized (priorityMessages) {
				priorityMessages.remove(PRIORITY_MARK + receivedString.substring(PRIORITY_RESPONSE.length()));
			}
		} else {
			if (receivedString.startsWith(PRIORITY_MARK)) {
				receivedString = receivedString.substring(PRIORITY_MARK.length());
				sendMessage(PRIORITY_RESPONSE + receivedString, false);
			}
			clientObject.receivedMessage(receivedString);
		}

		receivedString = "";
	}

	public UDPServer getServer() {
		return server;
	}

	public void disconnect() {
		server.removeClient(this);
		requiredPackageTimer.stop();
		if (clientObject != null)
			clientObject.disconnected();
	}

	public boolean checkConnection(String received, String controlString) {
		if (!received.equals(PRIORITY_MARK + controlString)) {
			String message = "Connection Denied";
			byte[] buf = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
			server.sendMessage(packet);
			disconnect();
			return false;
		} else {
			String message = "Connection Approved";
			message = PRIORITY_MARK + message;
			priorityMessages.add(message);
			byte[] buf = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
			server.sendMessage(packet);
			connectionApproved = true;
			return true;
		}
	}
}
