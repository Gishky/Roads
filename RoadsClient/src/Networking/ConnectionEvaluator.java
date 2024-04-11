package Networking;

import java.util.LinkedList;

public class ConnectionEvaluator {
	private static LinkedList<Integer> ping = new LinkedList<Integer>();
	private static LinkedList<Long> packageLossTracker = new LinkedList<Long>();

	public static void addPackage(Long pingRequest) {
		packageLossTracker.add(pingRequest);
		if (packageLossTracker.size() >= 10) {
			packageLossTracker.removeFirst();
		}
	}

	public static void receivedPackage(Long pingTime) {
		int i = packageLossTracker.indexOf(pingTime);
		if (i != -1)
			packageLossTracker.set(i, (long) 0);
	}

	public static int getPackagePercentile() {
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

	public static void addPing(Long pingTimeReceived) {
		receivedPackage(pingTimeReceived);
		int delay = (int) (System.currentTimeMillis() - pingTimeReceived);
		ping.add(delay);
		if (ping.size() >= 10)
			ping.removeFirst();
	}

	public static int getPing() {
		if (ping.size() == 0)
			return 0;
		int avg = 0;
		for (int p : ping) {
			avg += p;
		}
		return avg / ping.size();
	}
}
