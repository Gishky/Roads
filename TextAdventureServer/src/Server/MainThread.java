package Server;

import Networking.PortListener;

public class MainThread {

	public static void main(String[] args) {
		PortListener listener = new PortListener(61852);
		listener.start();
	}
	
}
