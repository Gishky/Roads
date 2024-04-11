package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.Timer;

import GameObjects.Entity;
import GameObjects.PlayerCharacter;
import GameObjects.World;
import Networking.Connection;

public class GameMaster implements ActionListener {

	private static GameMaster master;

	private ArrayList<Connection> clients;

	private ArrayList<Entity> entities;

	public GameMaster() {
		if (master == null) {
			master = this;
		}

		new Timer(50, this).start();
		entities = new ArrayList<Entity>();
		clients = new ArrayList<Connection>();
		World.generateWorld();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity.action())
				sendToAll("entity;" + entity.getId() + ";" + (int) entity.getPos().getX() + ";"
						+ (int) entity.getPos().getY());
		}

	}

	public static void addEntity(Entity e) {
		master.entities.add(e);
	}

	public static void removeEntity(Entity e) {
		sendToAll("removeEntity;" + e.getId());
		master.entities.remove(e);
	}

	public static void addListeners(Connection clientConnection) {
		master.clients.add(clientConnection);
	}

	public static void removeListener(Connection clientConnection) {
		master.clients.remove(clientConnection);
	}

	public static void sendToAll(String message) {
		for (Connection client : master.clients) {
			client.sendMessage(message);
		}
	}

	public static ArrayList<Entity> getEntities() {
		return master.entities;
	}

	public static ArrayList<Connection> getConnections() {
		return master.clients;
	}

	public static Connection hasConnection(InetAddress address, int port) {
		for (Connection con : master.clients) {
			if (con.getAddress().equals(address) && con.getPort() == port) {
				return con;
			}
		}
		return null;
	}

}
