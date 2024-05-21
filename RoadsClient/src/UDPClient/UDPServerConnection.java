package UDPClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Timer;

public class UDPServerConnection extends Thread implements ActionListener {

	private int port;
	private InetAddress address;
	private DatagramSocket socket;
	private String serverAddress;

	private boolean running;

	private UDPConnectionEvaluator eval;
	private UDPMessageListener listener;

	private String receivedString;

	private final String PRIORITY_MARK = "PRIORITY;";
	private final String PRIORITY_RESPONSE = "RECEIVEDPRIORITY;";

	private ArrayList<String> priorityMessages = new ArrayList<String>();
	private Timer requiredPackageTimer;

	public UDPServerConnection(String serverAddress, int serverPort, UDPMessageListener listener) {
		this.port = serverPort;
		this.listener = listener;
		this.serverAddress = serverAddress;

		requiredPackageTimer = new Timer(100, this);
		requiredPackageTimer.start();
	}

	public boolean startConnection(String controlString) {
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName(serverAddress);

			sendMessage(controlString, true);

			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);

			receivedString = new String(packet.getData(), 0, packet.getLength());
			if (receivedString.equals(PRIORITY_MARK + "Connection Approved")) {
				System.out.println("receiving: " + receivedString);
				receivedString = receivedString.substring(PRIORITY_MARK.length());
				sendMessage(PRIORITY_RESPONSE + receivedString, false);

				eval = new UDPConnectionEvaluator(this);
				System.out.println("approved");
				running = true;
				this.start();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("not approved");
		requiredPackageTimer.stop();
		running = false;
		return false;
	}

	public void run() {
		try {
			byte[] buf = new byte[256];

			while (running) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				receivedString = new String(packet.getData(), 0, packet.getLength());

				if (receivedString.startsWith("NetworkPingRequest")) {
					eval.addPing(Long.parseLong(receivedString.split(";")[1]));
				} else if (receivedString.startsWith(PRIORITY_RESPONSE)) {
					synchronized (priorityMessages) {
						priorityMessages.remove(PRIORITY_MARK + receivedString.substring(PRIORITY_RESPONSE.length()));
					}
				} else {
					if (receivedString.startsWith(PRIORITY_MARK)) {
						System.out.println("receiving: " + receivedString);
						receivedString = receivedString.substring(PRIORITY_MARK.length());
						sendMessage(PRIORITY_RESPONSE + receivedString, false);
					}
					listener.receivedMessage(receivedString);
				}
				receivedString = "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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

	public void sendMessage(String message, boolean priority) {
		if (priority) {
			message = PRIORITY_MARK + message;
			System.out.println("sending: " + message);
			synchronized (priorityMessages) {
				priorityMessages.add(message);
			}
		}
		byte[] buf = message.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getPing() {
		return eval.getPing();
	}

	public int getPackagePercentile() {
		return eval.getPackagePercentile();
	}
}
