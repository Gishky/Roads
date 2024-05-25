package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.Timer;

import AdminConsole.AdminConsole;
import GameObjects.Entity;
import GameObjects.Firebolt;
import GameObjects.World;
import UDPServer.UDPServer;

public class GameMaster implements ActionListener {

	private static GameMaster master;
	private UDPServer server;
	private Timer t;

	private ArrayList<Entity> entities;

	private LocalDate currentDate;

	public GameMaster(UDPServer server, AdminConsole adminConsole) {
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

	private long timestamp = 0;
	private ArrayList<Integer> serverTickRate = new ArrayList<Integer>();

	public static DecimalFormat decimalFormat = new DecimalFormat("#.####");

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity.action()) {
				sendToAll("{action:updateEntity,entity:" + entity.toJSON() + "}", false);
			}
		}

		if (!LocalDate.now().getDayOfWeek().equals(currentDate.getDayOfWeek())) {
			restartServer();
		}

		serverTickRate.add((int) (System.currentTimeMillis() - timestamp));
		int avg = 0;
		for (int i : serverTickRate) {
			avg += i;
		}
		avg /= serverTickRate.size();
		serverTickRate.remove(0);
		timestamp = System.currentTimeMillis();
		if (timestamp % 10 == 0)
			sendToAll("{action:serverTickRate,serverTickRate:" + avg + "}", false);
	}

	public static void addEntity(Entity e) {
		master.entities.add(e);
		sendToAll("{action:createEntity,entity:" + e.toJSON() + "}", true);
	}

	public static void removeEntity(Entity e) {
		master.entities.remove(e);
		// System.out.println("removing: " + e.getId());
		sendToAll("{action:removeEntity,entity:{id:" + e.getId() + "}}", true);
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
			AdminConsole.log("Exception: "+e.getMessage(),false);
			for (StackTraceElement s : e.getStackTrace()) {
				AdminConsole.log("    "+s.toString(),false);
			}
		}
	}
}
