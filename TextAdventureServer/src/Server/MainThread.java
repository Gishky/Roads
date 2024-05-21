package Server;

import HelperObjects.PlayerCharacterCreator;
import UDPServer.UDPServer;

public class MainThread {

	public static String gameVersion = "1.1.1";

	public static void main(String[] args) {
		UDPServer server = null;
		try {
			server = new UDPServer("RoadsServer", 61852, new PlayerCharacterCreator(), gameVersion);
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new GameMaster(server);
	}

}
