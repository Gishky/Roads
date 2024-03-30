package Server;

import java.util.ArrayList;

import Networking.PortListener;
import Player.Player;

public class MainThread {
	
	private static ArrayList<Player> players = new ArrayList<Player>();

	public static void main(String[] args) {
		PortListener listener = new PortListener(61852);
		listener.start();
	}
	
	public static void createPlayer(Player p) {
		players.add(p);
	}
	
	public static Player getPlayer(String name, String password) {
		for(Player p: players) {
			if(p.getName().equals(name) && p.getPassword().equals(password)) {
				return p;
			}
		}
		
		return null;
	}
}
