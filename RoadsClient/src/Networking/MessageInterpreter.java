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
		String[] messageParts = message.split(";");

		switch (messageParts[0]) {
		case "createEntity":
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
