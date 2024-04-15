package UDPServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.Timer;

public class UDPClientConnection implements ActionListener {

	private UDPServer server;
	private InetAddress address;
	private int port;
	private String receivedString;

	private UDPClientObject clientObject;

	private final String MESSAGE_START = "BEGIN;";
	private final String MESSAGE_END = ";END";
	private final String PRIORITY_MARK = "PRIORITY;";
	private final String PRIORITY_RESPONSE = "RECEIVEDPRIORITY;";

	private ArrayList<String> priorityMessages = new ArrayList<String>();
	private Timer requiredPackageTimer;

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
		if (priority) {
			message = PRIORITY_MARK + message;
			priorityMessages.add(message);
		}
		message = MESSAGE_START + message + MESSAGE_END;
		byte[] buf = message.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		server.sendMessage(packet);
	}

	private int connectionTimeoutCounter = 100;

	@Override
	public void actionPerformed(ActionEvent e) {
		connectionTimeoutCounter--;
		if (connectionTimeoutCounter <= 0) {
			server.removeClient(this);
			clientObject.disconnected();
			requiredPackageTimer.stop();
		}

		if (priorityMessages.size() < 1)
			return;
		for (int i = 0; i < priorityMessages.size(); i++) {
			if (priorityMessages.get(i) != null) {
				sendMessage(priorityMessages.get(i), false);
			} else {
				priorityMessages.remove(i);
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

	public void receivedString(String received) {
		connectionTimeoutCounter = 10;
		receivedString += received;
		if (!receivedString.endsWith(MESSAGE_END)) {
			return;
		}
		receivedString = receivedString.substring(receivedString.indexOf(MESSAGE_START) + MESSAGE_START.length(),
				receivedString.length() - MESSAGE_END.length());
		if (receivedString.startsWith("NetworkPingRequest")) {
			sendMessage(receivedString, false);
		} else if (receivedString.startsWith(PRIORITY_RESPONSE)) {
			priorityMessages.remove(PRIORITY_MARK + receivedString.substring(PRIORITY_RESPONSE.length()));
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
	}
}
