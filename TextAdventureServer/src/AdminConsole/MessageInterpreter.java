package AdminConsole;

import GameObjects.Entity;
import GameObjects.PlayerCharacter;
import GameObjects.World;
import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import GameObjects.Blocks.BlockCoalOre;
import GameObjects.Blocks.BlockDirt;
import GameObjects.Blocks.BlockGold;
import GameObjects.Blocks.BlockGoldOre;
import GameObjects.Blocks.BlockGrass;
import GameObjects.Blocks.BlockIron;
import GameObjects.Blocks.BlockIronOre;
import GameObjects.Blocks.BlockOven;
import GameObjects.Blocks.BlockStone;
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
		default:
			return "unknown command: " + receivedMessage;
		}
	}

	private static String giveBlock(String command) {
		if(command.equals("?")) {
			AdminConsole.log("├─<playerid> <inventoryslot> <json of block> | eg.: 0 0 {id:4,fuel:400}", true);
			AdminConsole.log("└─<playerid> <inventoryslot> <blockname>     | eg.: 0 0 oven", true);
			return null;
		}
		PlayerCharacter p = null;
		try {
			int playerid = Integer.parseInt(command.split(" ")[0]);
			for (Entity e : GameMaster.getEntities()) {
				if (e.getId() == playerid) {
					p = (PlayerCharacter) GameMaster.getEntities().get(playerid);
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
				Block b = null;
				switch (json.get("id")) {
				case "0":
					b = new BlockAir(json);
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to Air";
				case "1":
					b = new BlockDirt(json);
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Dirt";
				case "2":
					b = new BlockGrass(json);
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Grass";
				case "3":
					b = new BlockStone(json);
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Stone";
				case "4":
					b = new BlockOven(json);
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Oven";
				case "5":
					b = new BlockCoalOre(json);
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  CoalOre";
				case "6":
					b = new BlockIronOre(json);
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  IronOre";
				case "7":
					b = new BlockIron(json);
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Iron";
				case "8":
					b = new BlockGoldOre(json);
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  GoldOre";
				case "9":
					b = new BlockGold(json);
					p.setInventoryBlock(b, invSlot);
					return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Gold";
				default:
					return "Error converting \"" + command + "\" to a block";
				}
			}
		} catch (Exception e) {

		}
		try {
			switch (command.split(" ")[0].toLowerCase()) {
			case "air":
				p.setInventoryBlock(new BlockAir(), invSlot);
				return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Air";
			case "dirt":
				p.setInventoryBlock(new BlockDirt(), invSlot);
				return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Dirt";
			case "grass":
				p.setInventoryBlock(new BlockGrass(), invSlot);
				return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Grass";
			case "stone":
				p.setInventoryBlock(new BlockStone(), invSlot);
				return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Stone";
			case "oven":
				p.setInventoryBlock(new BlockOven(), invSlot);
				return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Oven";
			case "coalore":
				p.setInventoryBlock(new BlockCoalOre(), invSlot);
				return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  CoalOre";
			case "ironore":
				p.setInventoryBlock(new BlockIronOre(), invSlot);
				return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  IronOre";
			case "iron":
				p.setInventoryBlock(new BlockIron(), invSlot);
				return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Iron";
			case "goldore":
				p.setInventoryBlock(new BlockGoldOre(), invSlot);
				return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  GoldOre";
			case "gold":
				p.setInventoryBlock(new BlockGold(), invSlot);
				return "Set Inventoryslot "+invSlot+" of Player "+p.getId()+" to  Gold";
			default:
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
		if(block.equals("?")) {
			AdminConsole.log("├─<json of block>         | eg.: 0 0 {x:500,y:140,id:4,fuel:400}", true);
			AdminConsole.log("└─<x> <y> <blockname>     | eg.: 0 0 oven", true);
			return null;
		}
		try {
			JSONObject json = new JSONObject(block);
			if (json.get("id") != null) {
				Block b = null;
				switch (json.get("id")) {
				case "0":
					b = new BlockAir(json);
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to Air";
				case "1":
					b = new BlockDirt(json);
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to Dirt";
				case "2":
					b = new BlockGrass(json);
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to Grass";
				case "3":
					b = new BlockStone(json);
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to Stone";
				case "4":
					b = new BlockOven(json);
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to Oven";
				case "5":
					b = new BlockCoalOre(json);
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to CoalOre";
				case "6":
					b = new BlockIronOre(json);
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to IronOre";
				case "7":
					b = new BlockIron(json);
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to Iron";
				case "8":
					b = new BlockGoldOre(json);
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to GoldOre";
				case "9":
					b = new BlockGold(json);
					World.setBlock(b.getX(), b.getY(), b);
					return "Block " + b.getX() + "/" + b.getY() + " set to Gold";
				default:
					return "Error converting \"" + block + "\" to a block";
				}
			}
		} catch (Exception e) {

		}
		try {
			int x = Integer.parseInt(block.split(" ")[0]);
			int y = Integer.parseInt(block.split(" ")[1]);
			switch (block.split(" ")[2].toLowerCase()) {
			case "air":
				World.setBlock(x, y, new BlockAir());
				return "Block " + x + "/" + y + " set to Air";
			case "dirt":
				World.setBlock(x, y, new BlockDirt());
				return "Block " + x + "/" + y + " set to Dirt";
			case "grass":
				World.setBlock(x, y, new BlockGrass());
				return "Block " + x + "/" + y + " set to Grass";
			case "stone":
				World.setBlock(x, y, new BlockStone());
				return "Block " + x + "/" + y + " set to Stone";
			case "oven":
				World.setBlock(x, y, new BlockOven());
				return "Block " + x + "/" + y + " set to Oven";
			case "coalore":
				World.setBlock(x, y, new BlockCoalOre());
				return "Block " + x + "/" + y + " set to CoalOre";
			case "ironore":
				World.setBlock(x, y, new BlockIronOre());
				return "Block " + x + "/" + y + " set to IronOre";
			case "iron":
				World.setBlock(x, y, new BlockIron());
				return "Block " + x + "/" + y + " set to Iron";
			case "goldore":
				World.setBlock(x, y, new BlockGoldOre());
				return "Block " + x + "/" + y + " set to GoldOre";
			case "gold":
				World.setBlock(x, y, new BlockGold());
				return "Block " + x + "/" + y + " set to Gold";
			default:
				return "command \"setblock\" returns no functionality for input \"" + block + "\"";
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
}
