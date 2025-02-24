package HelperObjects;

import GameObjects.World;
import GameObjects.Blocks.Block;
import GameObjects.Entities.*;
import UDPClient.UDPMessageListener;
import Window.Frame;
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
			Frame.getFrame().setTitle((int) World.playerid + "");
			break;
		case "serverTickRate":
			Panel.setServerTickRate(Integer.parseInt(message.get("serverTickRate")));
			break;
		case "inventoryUpdate":
			updateInventory(message);
			break;
		case "activate":
			activateVFX(message);
			break;
		case "setCooldown":
			setCooldown(message);
			break;
		default:
			System.out.println("unknown command: " + receivedMessage);
		}
	}

	private void setCooldown(JSONObject message) {
		int id = Integer.parseInt(message.get("block"));
		if (World.playerInventory[id] != null) {
			World.playerInventoryCooldown[id] = System.currentTimeMillis() + Long.parseLong(message.get("cooldown"))
					- Panel.getServerConnection().getPing();
		}
	}

	private void activateVFX(JSONObject message) {
		World.getWorld()[Integer.parseInt(message.get("x"))][Integer.parseInt(message.get("y"))].activate();
	}

	private void updateInventory(JSONObject inv) {
		if (inv.get("heldid") != null)
			World.selectedInventory = Integer.parseInt(inv.get("heldid"));
		if (inv.get("0") != null)
			World.playerInventory[0] = Block.getBlockFromJSON(new JSONObject(inv.get("0")));
		if (inv.get("1") != null)
			World.playerInventory[1] = Block.getBlockFromJSON(new JSONObject(inv.get("1")));
		if (inv.get("2") != null)
			World.playerInventory[2] = Block.getBlockFromJSON(new JSONObject(inv.get("2")));
		if (inv.get("3") != null)
			World.playerInventory[3] = Block.getBlockFromJSON(new JSONObject(inv.get("3")));
		if (inv.get("4") != null)
			World.playerInventory[4] = Block.getBlockFromJSON(new JSONObject(inv.get("4")));
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
		if (Block.getBlockFromJSON(block) != null)
			World.setBlock(x, y, Block.getBlockFromJSON(block));
		else
			World.setBlock(x, y, new Block());
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
		case "chomper":
			Panel.getEntities().add(new Chomper(entity));
			break;
		}
	}
}
