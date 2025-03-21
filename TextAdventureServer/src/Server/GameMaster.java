package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.Timer;

import AdminConsole.AdminConsole;
import GameObjects.World;
import GameObjects.Blocks.Block;
import GameObjects.Entities.Entity;
import UDPServer.UDPServer;

public class GameMaster implements ActionListener {

	private static GameMaster master;
	private UDPServer server;
	private Timer t;

	private ArrayList<Entity> entities;
	private ArrayList<Block> blockUpdates;

	public GameMaster(UDPServer server, AdminConsole adminConsole) {
		if (master == null) {
			master = this;
		}

		this.server = server;
		t = new Timer(50, this);
		t.start();
		entities = new ArrayList<Entity>();
		blockUpdates = new ArrayList<Block>();
		World.generateWorld();
	}

	private long timestamp = 0;
	private ArrayList<Integer> serverTickRate = new ArrayList<Integer>();

	public static DecimalFormat decimalFormat = new DecimalFormat("#.####");

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		synchronized (blockUpdates) {
			for (Block b : blockUpdates) {
				blocks.add(b);
			}
			blockUpdates.clear();
		}
		for (Block b : blocks) {
			b.update();
		}

		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (entity.isDeleted())
				entities.remove(i);
			else if (entity.action()) {
				sendToAll("{action:updateEntity,entity:" + entity.toJSON() + "}", false);
			}
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

	public static void removeEntity(Entity e, Boolean silent) {
		master.entities.remove(e);
		e.deleteEntity();
		if (!silent)
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
			AdminConsole.log("Exception: " + e.getMessage(), false);
			for (StackTraceElement s : e.getStackTrace()) {
				AdminConsole.log("    " + s.toString(), false);
			}
		}
	}

	public static void updateBlock(Block b) {
		synchronized (master.blockUpdates) {
			if (!master.blockUpdates.contains(b))
				master.blockUpdates.add(b);
		}
	}
}
