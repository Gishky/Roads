package HelperObjects;

import GameObjects.Entity;
import GameObjects.Firebolt;
import GameObjects.OvenAbilityJet;
import GameObjects.OvenEntity;
import GameObjects.PlayerCharacter;
import GameObjects.World;
import GameObjects.Blocks.Block;
import UDPClient.UDPMessageListener;
import Window.Panel;

public class MessageInterpreter implements UDPMessageListener {

	public void receivedMessage(String receivedMessage) {
		if (!receivedMessage.contains("}")) {
			return;
		}
		JSONObject message = new JSONObject(receivedMessage);

		switch (message.get("action")) {
		case "createEntity":
			createEntity(new JSONObject(message.get("entity")));
			break;
		case "updateEntity":
			updateEntity(new JSONObject(message.get("entity")));
			break;
		case "createWorld":
			createWorld(message);
			break;
		case "setBlock":
			updateWorld(new JSONObject(message.get("block")));
			break;
		case "removeEntity":
			removeEntity(new JSONObject(message.get("entity")).get("id"));
			break;
		case "playerID":
			World.playerid = (int) Integer.parseInt(message.get("playerID"));
			break;
		case "serverTickRate":
			Panel.setServerTickRate(Integer.parseInt(message.get("serverTickRate")));
			break;
		default:
			System.out.println("unknown command: " + receivedMessage);
		}
	}

	private void createWorld(JSONObject world) {
		int columns = Integer.parseInt(world.get("length"));
		int rows = Integer.parseInt(world.get("height"));
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

	private static void updateWorld(JSONObject block) {
		int x = Integer.parseInt(block.get("x"));
		int y = Integer.parseInt(block.get("y"));

		World.setBlock(x, y, Block.getBlockFromID(block.get("id")));
	}

	private static void updateEntity(JSONObject entity) {
		for (int i = 0; i < Panel.getEntities().size(); i++) {
			Entity e = Panel.getEntities().get(i);
			if (("" + e.getId()).equals(entity.get("id"))) {
				e.updateEntity(entity);
				return;
			}
		}

	}

	private static void createEntity(JSONObject entity) {

		if (Panel.existsEntityID(entity.get("id"))) {
			return;
		}
		for (int index = 0; index < Panel.getRemovedEntityIDs().size(); index++) {
			if ((Panel.getRemovedEntityIDs().get(index) + "").equals(entity.get("id"))) {
				System.out.println("Removed Entity before adding");
				Panel.getRemovedEntityIDs().remove(index);
				return;
			}
		}

		switch (entity.get("type")) {
		case "player":
			Panel.getEntities().add(new PlayerCharacter(entity));
			break;
		case "firebolt":
			Panel.getEntities().add(new Firebolt(entity));
			break;
		case "oven":
			Panel.getEntities().add(new OvenEntity(entity));
			break;
		case "ovenAbility":
			Panel.getEntities().add(new OvenAbilityJet(entity));
			break;
		}
	}
}
