package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class Block {
	public static int size = 30;

	private long abilityCooldown = 0;
	private Color c = Color.pink;
	protected boolean activated = false;

	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(Color.pink);
		g.fillRect(x, y, size, size);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		int originalSize = Block.size;
		Block.size = size;
		int xInv = (x - Panel.windowWidth / 2) / Block.size;
		int yInv = (y - Panel.windowHeight / 2) / Block.size;
		int xErr = x - (xInv * size + Panel.windowWidth / 2);
		int yErr = y - (yInv * size + Panel.windowHeight / 2);
		draw(xInv, yInv, g, -xErr, -yErr);
		Block.size = originalSize;

		if (System.currentTimeMillis() >= abilityCooldown)
			return;

		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect(x, y, size + 1, size + 1);

		g.setColor(Color.white);

		g.setFont(new Font("Monospaced", Font.BOLD, 30));
		int seconds = (int) (abilityCooldown - System.currentTimeMillis()) / 1000;
		int secondsSize = g.getFontMetrics().stringWidth("" + seconds);
		g.drawString("" + seconds, x + size / 2 - secondsSize / 2, y + size / 2 + 10);

		g.setFont(new Font("Monospaced", Font.PLAIN, 20));
		int miliseconds = (int) ((abilityCooldown - System.currentTimeMillis()) / 100) - seconds * 10;
		g.drawString("" + miliseconds, x + size / 2 + secondsSize / 2, y + size / 2 - 2);
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

	public void drawGrain(Graphics2D g, Color c, int x, int y, int size, int diff) {
		g.setColor(c);

		g.fillRect(x, y, size, size);

		g.setColor(brighterColor(c, diff));
		drawPixel(g, x, y, 1, 0, size);
		drawPixel(g, x, y, 3, 4, size);
		drawPixel(g, x, y, 2, 2, size);
		drawPixel(g, x, y, 5, 4, size);
		drawPixel(g, x, y, 1, 3, size);

		g.setColor(darkerColor(c, diff));
		drawPixel(g, x, y, 5, 3, size);
		drawPixel(g, x, y, 4, 0, size);
		drawPixel(g, x, y, 0, 3, size);
		drawPixel(g, x, y, 2, 5, size);
		drawPixel(g, x, y, 1, 5, size);
		drawPixel(g, x, y, 3, 1, size);
	}

	public void drawOre(Graphics2D g, Color c, int x, int y, int size) {
		g.setColor(c);
		drawPixel(g, x, y, 2, 3, size);
		drawPixel(g, x, y, 1, 4, size);
		drawPixel(g, x, y, 3, 1, size);
		drawPixel(g, x, y, 1, 2, size);
		drawPixel(g, x, y, 4, 4, size);
	}

	public void drawWoodGrain(Graphics2D g, Color c, int x, int y, int size, int diff) {
		g.setColor(brighterColor(c, diff));
		g.fillRect(x + 3 * size / 6, y + 2 * size / 6, size / 6, size * 2 / 6);
		g.fillRect(x + 1 * size / 6, y + 4 * size / 6, size / 6, size * 2 / 6);
		g.fillRect(x + 4 * size / 6, y + 0 * size / 6, size / 6, size * 2 / 6);
		g.fillRect(x + 1 * size / 6, y + 0 * size / 6, size / 6, size * 2 / 6);
		g.setColor(darkerColor(c, diff));
		g.fillRect(x + 3 * size / 6, y + 4 * size / 6, size / 6, size * 2 / 6);
		g.fillRect(x + 2 * size / 6, y + 1 * size / 6, size / 6, size * 2 / 6);
		g.fillRect(x + 0 * size / 6, y + 2 * size / 6, size / 6, size * 2 / 6);
		g.fillRect(x + 5 * size / 6, y + 4 * size / 6, size / 6, size * 2 / 6);
	}

	protected Color darkerColor(Color c, int diff) {
		return new Color(Math.max(0, c.getRed() - diff), Math.max(0, c.getGreen() - diff),
				Math.max(0, c.getBlue() - diff), c.getAlpha());
	}

	protected Color brighterColor(Color c, int diff) {
		return new Color(Math.min(255, c.getRed() + diff), Math.min(255, c.getGreen() + diff),
				Math.min(255, c.getBlue() + diff), c.getAlpha());
	}

	public void drawPixel(Graphics2D g, int x, int y, int xPixel, int yPixel, int size) {
		g.fillRect(x + xPixel * size / 6, y + yPixel * size / 6, size / 6, size / 6);
	}

	public static Color getDefaultColor() {
		return Color.pink;
	}

	public long getAbilityCooldown() {
		return abilityCooldown;
	}

	public void setAbilityCooldown(long abilityCooldown) {
		this.abilityCooldown = abilityCooldown;
	}
}
