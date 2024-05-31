package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;

public class Block {
	public static int size = 30;

	private Color c = Color.pink;
	protected boolean activated = false;

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

	public static Block getBlockFromID(String id, JSONObject json) {
		switch (id) {
		case "0":
			return new BlockAir(json);
		case "1":
			return new BlockDirt(json);
		case "2":
			return new BlockGrass(json);
		case "3":
			return new BlockStone(json);
		case "4":
			return new BlockOven(json);
		case "5":
			return new BlockCoalOre(json);
		case "6":
			return new BlockIronOre(json);
		case "7":
			return new BlockIron(json);
		case "8":
			return new BlockGoldOre(json);
		case "9":
			return new BlockGold(json);
		case "10":
			return new BlockWood(json);
		case "11":
			return new BlockLeaf(json);
		case "12":
			return new BlockRelay(json);
		case "13":
			return new BlockActivator(json);
		case "14":
			return new BlockMachine(json);
		case "15":
			return new BlockGoldChunk(json);
		case "16":
			return new BlockIronChunk(json);
		case "17":
			return new BlockChest(json);
		case "18":
			return new BlockPlacer(json);
		default:
			return null;
		}
	}

	public static Block getBlockFromJSON(JSONObject block) {
		return getBlockFromID(block.get("id"), block);
	}

	public Color getColor() {
		return c;
	}

	public void setColor(Color c) {
		this.c = c;
	}

	public static Color interpolateColor(Color start, Color end, double transition) {
		int r = (int) (start.getRed() * (1 - transition) + end.getRed() * transition);
		int g = (int) (start.getGreen() * (1 - transition) + end.getGreen() * transition);
		int b = (int) (start.getBlue() * (1 - transition) + end.getBlue() * transition);
		return new Color(r, g, b);
	}

	public void activate() {
		activated = true;
	}
}
