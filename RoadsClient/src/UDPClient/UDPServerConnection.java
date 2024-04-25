package UDPClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.Timer;

public class UDPServerConnection extends Thread implements ActionListener {

	private int port;
	private InetAddress address;
	private DatagramSocket socket;

	private UDPConnectionEvaluator eval;
	private UDPMessageListener listener;

	private String receivedString;

	private final String PRIORITY_MARK = "PRIORITY;";
	private final String PRIORITY_RESPONSE = "RECEIVEDPRIORITY;";

	private ArrayList<String> priorityMessages = new ArrayList<String>();

	public UDPServerConnection(String serverAddress, int serverPort, UDPMessageListener listener) {
		this.port = serverPort;
		this.listener = listener;
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName(serverAddress);

			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
			socket.send(packet);

			this.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		eval = new UDPConnectionEvaluator(this);

		Timer requiredPackageTimer = new Timer(100, this);
		requiredPackageTimer.start();
	}

	public void run() {
		try {
			byte[] buf = new byte[256];

			while (true) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				receivedString = new String(packet.getData(), 0, packet.getLength());

				if (receivedString.startsWith("NetworkPingRequest")) {
					eval.addPing(Long.parseLong(receivedString.split(";")[1]));
				} else if (receivedString.startsWith(PRIORITY_RESPONSE)) {
					try {
						priorityMessages.remove(PRIORITY_MARK + receivedString.substring(PRIORITY_RESPONSE.length()));
					} catch (Exception e) {

					}
				} else {
					if (receivedString.startsWith(PRIORITY_MARK)) {
						System.out.println("receiving: "+receivedString);
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
		for (int i = 0; i < priorityMessages.size(); i++) {
			if(i>10) 
				return;
			try {
				if (priorityMessages.get(i) != null) {
					System.out.println("sending: "+priorityMessages.get(i));
					sendMessage(priorityMessages.get(i), false);
				} else {
					priorityMessages.remove(i);
				}
			} catch (Exception ex) {

			}
		}

	}

	public void sendMessage(String message, boolean priority) {
		if (priority) {
			message = PRIORITY_MARK + message;
			System.out.println("sending: "+message);
			priorityMessages.add(message);
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
