package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.Timer;

import GameObjects.Entity;
import GameObjects.World;
import UDPServer.UDPServer;

public class GameMaster implements ActionListener {

	private static GameMaster master;
	private UDPServer server;
	private Timer t;

	private ArrayList<Entity> entities;

	private LocalDate currentDate;
	public GameMaster(UDPServer server) {
		if (master == null) {
			master = this;
		}
		
		currentDate = LocalDate.now();

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
		
		if(!LocalDate.now().getDayOfWeek().equals(currentDate.getDayOfWeek())) {
			restartServer();
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

	public static void restartServer() {
		try {
			Runtime.getRuntime().exec("sudo reboot");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
