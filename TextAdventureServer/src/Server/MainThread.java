package Server;

import AdminConsole.AdminConsole;
import HelperObjects.PlayerCharacterCreator;
import UDPServer.UDPServer;

public class MainThread {

	public static String gameVersion = "v1.5";

	public static void main(String[] args) {
		UDPServer server = null;
		AdminConsole adminConsole = null;
		try {
			adminConsole = new AdminConsole(61852);
			adminConsole.setName("AdminConsole");
			adminConsole.start();
			AdminConsole.log("starting Server...", false);
			server = new UDPServer("UDPServer", 61852, new PlayerCharacterCreator(), gameVersion);
			server.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AdminConsole.log("starting GameMaster...", false);
		AdminConsole.log("", true);
		new GameMaster(server, adminConsole);
	}

}
