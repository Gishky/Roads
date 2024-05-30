package AdminConsole;

import GameObjects.World;
import GameObjects.Blocks.*;
import GameObjects.Entities.Entity;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import Server.GameMaster;

public class MessageInterpreter {

	public static String receivedMessage(String receivedMessage) {
		receivedMessage = receivedMessage.substring(receivedMessage.indexOf("/") + 1);
		String command = receivedMessage.split(" ")[0];

		switch (command.toLowerCase()) {
		case "?":
			AdminConsole.log("├─setblock - place a block in the world", true);
			AdminConsole.log("├─give     - give a block to a player", true);
			AdminConsole.log("└─?        - display this info card", true);
			return null;
		case "setblock":
			return updateWorld(receivedMessage.substring(command.length() + 1));
		case "give":
			return giveBlock(receivedMessage.substring(command.length() + 1));
		case "reboot":
			GameMaster.restartServer();
			return "restarting Server...";
		default:
			return "unknown command: " + receivedMessage;
		}
	}

	private static String giveBlock(String command) {
		if (command.equals("?")) {
			AdminConsole.log("/give", true);
			AdminConsole.log("├─<playerid> <inventoryslot> <json of block> | eg.: 0 0 {id:4,fuel:400}", true);
			AdminConsole.log("└─<playerid> <inventoryslot> <blockname>     | eg.: 0 0 oven", true);
			return null;
		}
		PlayerCharacter p = null;
		try {
			int playerid = Integer.parseInt(command.split(" ")[0]);
			for (Entity e : GameMaster.getEntities()) {
				if (e.getId() == playerid) {
					p = (PlayerCharacter) e;
					break;
				}
			}
			if (p == null)
				return playerid + " is not a valid Player ID";
		} catch (Exception e) {
			return command.split(" ")[0] + " is not a valid Player ID";
		}
		int invSlot = -1;
		try {
			invSlot = Integer.parseInt(command.split(" ")[1]);
		} catch (Exception e) {
			return "Cannot convert \"" + command.split(" ")[1] + "\" to inventory slot";
		}

		if (invSlot < 0 || invSlot > 4) {
			return "\"" + command.split(" ")[1] + "\" is outside of valid Inventory Range";
		}

		try {
			command = command.substring(command.split(" ")[0].length() + 1 + command.split(" ")[1].length() + 1);
		} catch (Exception e) {
			return "No Block information received";
		}

		try {
			JSONObject json = new JSONObject(command);
			if (json.get("id") != null) {
				Block b = getBlockFromIdentifier(json.get("id"), json);
				if (b.getId() != -1) {
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot " + invSlot + " of Player " + p.getId() + " to " + command;
				} else {
					return "Error converting \"" + command + "\" to a block";
				}
			}
		} catch (Exception e) {

		}
		try {
			Block b = getBlockFromIdentifier(command.split(" ")[0].toLowerCase(), null);
			if (b.getId() != -1) {
				p.setInventoryBlock(b, invSlot);
				return "Set Inventoryslot " + invSlot + " of Player " + p.getId() + " to "
						+ command.split(" ")[0].toLowerCase();
			} else {
				return "Error converting \"" + command + "\" to a block";
			}
		} catch (Exception e) {
			AdminConsole.log("Exception: " + e.getMessage(), false);
			for (int i = 0; i < e.getStackTrace().length; i++) {
				String s = "";
				if (i != e.getStackTrace().length - 1)
					s += "├─";
				else
					s += "└─";
				AdminConsole.log(s + e.getStackTrace()[i].toString(), true);
			}
			return null;
		}
	}

	private static String updateWorld(String block) {
		if (block.equals("?")) {
			AdminConsole.log("/setblock", true);
			AdminConsole.log("├─<json of block>         | eg.: {x:500,y:150,id:4,fuel:400}", true);
			AdminConsole.log("└─<x> <y> <blockname>     | eg.: 0 0 oven", true);
			return null;
		}
		try {
			JSONObject json = new JSONObject(block);
			if (json.get("id") != null) {
				Block b = getBlockFromIdentifier(json.get("id"), json);
				if (b.getId() != -1) {
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to " + block;
				} else {
					return "Error converting \"" + block + "\" to a block";
				}
			}
		} catch (Exception e) {

		}
		try {
			int x = Integer.parseInt(block.split(" ")[0]);
			int y = Integer.parseInt(block.split(" ")[1]);
			Block b = getBlockFromIdentifier(block.split(" ")[2].toLowerCase(), null);
			if (b.getId() != -1) {
				World.setBlock(x, y, b);
				return "Block " + x + "/" + y + " set to " + block.split(" ")[2].toLowerCase();
			} else {
				return "Error converting \"" + block + "\" to a block";
			}
		} catch (

		Exception e) {
			AdminConsole.log("Exception: " + e.getMessage(), false);
			for (int i = 0; i < e.getStackTrace().length; i++) {
				String s = "";
				if (i != e.getStackTrace().length - 1)
					s += "├─";
				else
					s += "└─";
				AdminConsole.log(s + e.getStackTrace()[i].toString(), true);
			}
			return null;
		}
	}

	public static Block getBlockFromIdentifier(String identifier, JSONObject json) {
		switch (identifier) {
		case "air":
		case "0":
			return new BlockAir(json);
		case "dirt":
		case "1":
			return new BlockDirt(json);
		case "grass":
		case "2":
			return new BlockGrass(json);
		case "stone":
		case "3":
			return new BlockStone(json);
		case "oven":
		case "4":
			return new BlockOven(json);
		case "coalore":
		case "5":
			return new BlockCoalOre(json);
		case "ironore":
		case "6":
			return new BlockIronOre(json);
		case "iron":
		case "7":
			return new BlockIron(json);
		case "goldore":
		case "8":
			return new BlockGoldOre(json);
		case "gold":
		case "9":
			return new BlockGold(json);
		case "wood":
		case "10":
			return new BlockWood(json);
		case "leaf":
		case "11":
			return new BlockLeaf(json);
		case "relay":
		case "12":
			return new BlockRelay(json);
		case "activator":
		case "13":
			return new BlockActivator(json);
		case "machine":
		case "14":
			return new BlockMachine(json);
		case "goldchunk":
		case "15":
			return new BlockGoldChunk(json);
		case "ironchunk":
		case "16":
			return new BlockIronChunk(json);
		case "chest":
		case "17":
			return new BlockChest(json);
		default:
			return new Block(json);
		}
	}
}
