package UDPClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.Timer;

public class UDPConnectionEvaluator implements ActionListener {
	private LinkedList<Integer> ping = new LinkedList<Integer>();
	private LinkedList<Long> packageLossTracker = new LinkedList<Long>();

	private UDPServerConnection server;

	public UDPConnectionEvaluator(UDPServerConnection server) {
		this.server = server;
		Timer pingTimer = new Timer(100, this);
		pingTimer.start();
	}

	public void addPackage(Long pingRequest) {
		packageLossTracker.add(pingRequest);
		if (packageLossTracker.size() > 11) {
			packageLossTracker.removeFirst();
		}
	}

	private void receivedPackage(Long pingTime) {
		int i = packageLossTracker.indexOf(pingTime);
		if (i != -1) {
			try {
				packageLossTracker.set(i, (long) 0);
			} catch (IndexOutOfBoundsException e) {
				
			}
		}
	}

	public int getPackagePercentile() {
		int received = 0;
		int size = (int) (packageLossTracker.size() - 1);

		if (size == 0) {
			return 100;
		}

		for (int i = 0; i < size; i++) {
			long sentTime = packageLossTracker.get(i);
			if (sentTime == 0) {
				received++;
			}
		}
		return received * 100 / size;
	}

	public void addPing(Long pingTimeReceived) {
		receivedPackage(pingTimeReceived);
		int delay = (int) (System.currentTimeMillis() - pingTimeReceived);
		ping.add(delay);
		if (ping.size() > 10)
			ping.removeFirst();
	}

	public int getPing() {
		if (ping.size() == 0)
			return 0;
		int avg = 0;
		for (int p : ping) {
			avg += p;
		}
		return avg / ping.size();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Long currentMillis = System.currentTimeMillis();
		server.sendMessage("NetworkPingRequest;" + currentMillis, false);
		addPackage(currentMillis);
	}
}
