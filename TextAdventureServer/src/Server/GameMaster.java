package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

import GameObjects.Entity;
import GameObjects.World;
import UDPServer.UDPServer;

public class GameMaster implements ActionListener {

	private static GameMaster master;
	private UDPServer server;

	private ArrayList<Entity> entities;

	public GameMaster(UDPServer server) {
		if (master == null) {
			master = this;
		}

		this.server = server;
		new Timer(50, this).start();
		entities = new ArrayList<Entity>();
		World.generateWorld();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity.action())
				sendToAll("entity;" + entity.getId() + ";" + (int) entity.getPos().getX() + ";"
						+ (int) entity.getPos().getY() + ";" + entity.getBreakCount(), false);
		}

	}

	public static void addEntity(Entity e) {
		master.entities.add(e);
		sendToAll("createEntity;" + e.getEntityIdentifier() + ";" + e.getId() + ";" + (int) e.getPos().getX() + ";"
				+ (int) e.getPos().getY(), true);
	}

	public static void removeEntity(Entity e) {
		master.entities.remove(e);
		System.out.println("removing: " + e.getId());
		sendToAll("removeEntity;" + e.getId(), true);
	}

	public static ArrayList<Entity> getEntities() {
		return master.entities;
	}

	public static void sendToAll(String message, boolean priority) {
		master.server.sendToAll(message, priority);
	}
}
