package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.Timer;

import GameObjects.Entity;
import GameObjects.World;
import HelperObjects.PlayerCharacterCreator;
import UDPServer.UDPServer;

public class GameMaster implements ActionListener {

	private static GameMaster master;
	private UDPServer server;
	private Timer t;

	private ArrayList<Entity> entities;

	public GameMaster(UDPServer server) {
		if (master == null) {
			master = this;
		}

		this.server = server;
		t = new Timer(50, this);
		t.start();
		entities = new ArrayList<Entity>();
		World.generateWorld();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity.action())
				sendToAll("entity;" + entity.getId() + ";" + (int) entity.getPos().getX() + ";"
						+ (int) entity.getPos().getY() + ";" + entity.getBreakCount() + ";" + entity.getHPPercentile()
						+ ";" + entity.getHeldBlockId(), false);
		}

		LocalDateTime t = LocalDateTime.now();
		if (t.getHour() == 0) { // restart server
			UDPServer.getInstances().get(0).stopServer();

			UDPServer server = null;
			try {
				server = new UDPServer("RoadsServer", 61852, new PlayerCharacterCreator());
				server.start();
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			master = null;
			new GameMaster(server);
		}
	}

	public static void addEntity(Entity e) {
		master.entities.add(e);
		sendToAll("createEntity;" + e.getEntityIdentifier() + ";" + e.getId() + ";" + (int) e.getPos().getX() + ";"
				+ (int) e.getPos().getY() + ";" + e.getHPPercentile(), true);
	}

	public static void removeEntity(Entity e) {
		master.entities.remove(e);
		// System.out.println("removing: " + e.getId());
		sendToAll("removeEntity;" + e.getId(), true);
	}

	public static ArrayList<Entity> getEntities() {
		return master.entities;
	}

	public static void sendToAll(String message, boolean priority) {
		master.server.sendToAll(message, priority);
	}
}
