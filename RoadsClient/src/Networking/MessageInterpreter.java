package Networking;

import GameObjects.BlockAir;
import GameObjects.BlockDirt;
import GameObjects.BlockGrass;
import GameObjects.BlockStone;
import GameObjects.Entity;
import GameObjects.PlayerCharacter;
import GameObjects.World;
import HelperObjects.Position;
import Window.Panel;

public class MessageInterpreter {

	public static void filterMessage(String message) {
		System.out.println(message);
		String[] messageParts = message.split(";");
		if (messageParts.length == 0) {
			return;
		}

		switch (messageParts[0]) {
		case "ping":
			ConnectionEvaluator.addPing(Long.parseLong(messageParts[1]));
			break;
		case "createEntity":
			// System.out.println(message);
			createEntity(messageParts);
			break;
		case "entity":
			updateEntity(messageParts);
			break;
		case "createWorld":
			World.createWorld(messageParts[1], messageParts[2]);
			break;
		case "block":
			updateWorld(messageParts);
			break;
		case "removeEntity":
			removeEntity(messageParts[1]);
			break;
		case "key":
			updateServerKeyboard(message);
			break;
		}
	}

	private static void updateServerKeyboard(String message) {
		Panel.getServerKeyboard().inputReceived(message);
	}

	private static void removeEntity(String id) {
		for (Entity e : Panel.getEntities()) {
			if (("" + e.getId()).equals(id)) {
				Panel.getEntities().remove(e);
				return;
			}
		}
	}

	private static void updateWorld(String[] messageParts) {
		int x = Integer.parseInt(messageParts[1]);
		int y = Integer.parseInt(messageParts[2]);

		switch (Integer.parseInt(messageParts[3])) {
		case 0:
			World.setBlock(x, y, new BlockAir());
			;
			break;
		case 1:
			World.setBlock(x, y, new BlockDirt());
			;
			break;
		case 2:
			World.setBlock(x, y, new BlockGrass());
			;
			break;
		case 3:
			World.setBlock(x, y, new BlockStone());
			;
			break;
		}
	}

	private static void updateEntity(String[] messageParts) {
		for (Entity e : Panel.getEntities()) {
			if (("" + e.getId()).equals(messageParts[1])) {
				e.setPos(new Position(messageParts[2], messageParts[3]));
				if (e.getId() == World.playerid) {
					World.cameraX = Integer.parseInt(messageParts[2]);
					World.cameraY = Integer.parseInt(messageParts[3]);
				}
				return;
			}
		}

	}

	private static void createEntity(String[] messageParts) {
		switch (messageParts[1]) {
		case "player":
			Panel.getEntities().add(new PlayerCharacter(messageParts[2], messageParts[3], messageParts[4]));
			break;
		}
	}
}
