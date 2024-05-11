package HelperObjects;

import java.awt.Color;
import java.util.Random;

import GameObjects.Block;
import GameObjects.BlockAir;
import GameObjects.Entity;
import GameObjects.Firebolt;
import GameObjects.OvenEntity;
import GameObjects.PlayerCharacter;
import GameObjects.World;
import UDPClient.UDPMessageListener;
import Window.Panel;

public class MessageInterpreter implements UDPMessageListener {

	public void receivedMessage(String message) {
		String[] messageParts = message.split(";");
		if (messageParts.length == 0) {
			return;
		}

		switch (messageParts[0]) {
		case "createEntity":
			createEntity(messageParts);
			break;
		case "entity":
			updateEntity(messageParts);
			break;
		case "createWorld":
			createWorld(messageParts);
			break;
		case "block":
			updateWorld(messageParts);
			break;
		case "removeEntity":
			removeEntity(messageParts[1]);
			break;
		case "id":
			World.playerid = Integer.parseInt(messageParts[1]);
			break;
		default:
			System.out.println("unknown command: " + message);
		}
	}

	private void createWorld(String[] messageParts) {
		int columns = Integer.parseInt(messageParts[1]);
		int rows = Integer.parseInt(messageParts[2]);
		World.createWorld(columns, rows);
	}

	private static void removeEntity(String id) {
		for (Entity e : Panel.getEntities()) {
			if (("" + e.getId()).equals(id)) {
				e.setDelete(true);
				return;
			}
		}
	}

	private static void updateWorld(String[] messageParts) {
		int x = Integer.parseInt(messageParts[1]);
		int y = Integer.parseInt(messageParts[2]);

		if (World.getWorld()[x][y] != null && !(World.getWorld()[x][y] instanceof BlockAir)) {
			Random r = new Random();
			Color c = World.getWorld()[x][y].getColor().brighter();
			for (int i = 0; i < 50; i++) {
				Particle p = new Particle(x * Block.size + r.nextDouble() * Block.size,
						y * Block.size + r.nextDouble() * Block.size, 0, 0, r.nextDouble() * 0.5 - 0.3,
						r.nextDouble() * 0.5, c);
				p.setLifetime(r.nextInt(10) + 3);
				Panel.addParticle(p);
			}
		}

		World.setBlock(x, y, Block.getBlockFromID(messageParts[3]));
	}

	private static void updateEntity(String[] messageParts) {
		for (Entity e : Panel.getEntities()) {
			if (("" + e.getId()).equals(messageParts[1])) {
				e.setPos(new Position(messageParts[2], messageParts[3]));
				e.setBreakCount(Integer.parseInt(messageParts[4]));
				e.setHPPercent(Integer.parseInt(messageParts[5]));
				e.setHeldBlock(Block.getBlockFromID(messageParts[6]));
				if (e.getId() == World.playerid) {
					World.cameraX = Integer.parseInt(messageParts[2]);
					World.cameraY = Integer.parseInt(messageParts[3]);
				}
				return;
			}
		}

	}

	private static void createEntity(String[] messageParts) {

		if (Panel.existsEntityID(messageParts[2])) {
			return;
		}
		for (int index = 0; index < Panel.getRemovedEntityIDs().size(); index++) {
			if ((Panel.getRemovedEntityIDs().get(index) + "").equals(messageParts[2])) {
				System.out.println("Removed Entity before adding");
				Panel.getRemovedEntityIDs().remove(index);
				return;
			}
		}

		switch (messageParts[1]) {
		case "player":
			Panel.getEntities().add(new PlayerCharacter(messageParts[2], messageParts[3], messageParts[4],
					messageParts[5], messageParts[6]));
			break;
		case "firebolt":
			Panel.getEntities().add(
					new Firebolt(messageParts[2], messageParts[3], messageParts[4], messageParts[5], messageParts[6]));
			break;
		case "oven":
			Panel.getEntities().add(new OvenEntity(messageParts[2], messageParts[3], messageParts[4], messageParts[5],
					messageParts[6]));
			break;
		}
	}
}
