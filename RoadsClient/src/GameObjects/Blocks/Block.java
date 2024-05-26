package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;

public class Block {
	public static int size = 30;

	private Color c = Color.pink;

	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		/*
		 * if (x % 40 == 0 && y % 10 == 0) {
		 * 
		 * g.setColor(Color.black); g.setFont(new Font("Arial", Font.PLAIN, 20));
		 * g.drawString((x * size - 100) + "", x * size - cameraX + Panel.windowWidth /
		 * 2 - 100, y * size - cameraY + Panel.windowHeight / 2 - 100);
		 * 
		 * g.drawString((y * size - 100) + "", x * size - cameraX + Panel.windowWidth /
		 * 2 - 100, y * size - cameraY + Panel.windowHeight / 2 - 100 + 15); }
		 */
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(Color.pink);
		g.fillRect(x, y, size, size);
	}

	public static Block getBlockFromID(String id) {
		switch (id) {
		case "0":
			return new BlockAir(null);
		case "1":
			return new BlockDirt(null);
		case "2":
			return new BlockGrass(null);
		case "3":
			return new BlockStone(null);
		case "4":
			return new BlockOven(null);
		case "5":
			return new BlockCoalOre(null);
		case "6":
			return new BlockIronOre(null);
		case "7":
			return new BlockIron(null);
		case "8":
			return new BlockGoldOre(null);
		case "9":
			return new BlockGold(null);
		case "10":
			return new BlockWood(null);
		case "11":
			return new BlockLeaf(null);
		case "12":
			return new BlockRelay(null);
		case "13":
			return new BlockActivator(null);
		case "14":
			return new BlockMachine(null);
		default:
			return null;
		}
	}

	public static Block getBlockFromJSON(JSONObject block) {
		switch (block.get("id")) {
		case "0":
			return new BlockAir(block);
		case "1":
			return new BlockDirt(block);
		case "2":
			return new BlockGrass(block);
		case "3":
			return new BlockStone(block);
		case "4":
			return new BlockOven(block);
		case "5":
			return new BlockCoalOre(block);
		case "6":
			return new BlockIronOre(block);
		case "7":
			return new BlockIron(block);
		case "8":
			return new BlockGoldOre(block);
		case "9":
			return new BlockGold(block);
		case "10":
			return new BlockWood(block);
		case "11":
			return new BlockLeaf(block);
		case "12":
			return new BlockRelay(block);
		case "13":
			return new BlockActivator(block);
		case "14":
			return new BlockMachine(block);
		default:
			return null;
		}
	}

	public Color getColor() {
		return c;
	}

	public void setColor(Color c) {
		this.c = c;
	}
}
