package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockIronOre extends Block {

	private int r, g, b;
	private int rb, gb, bb;

	public BlockIronOre(JSONObject block) {
		breakThreshhold = 20;
		setColor(new Color(77, 30, 0));
		r = 77;
		g = 30;
		b = 0;
		rb = 100;
		gb = 100;
		bb = 100;

		if (block == null)
			return;

		int maxSmelt = 400;
		int currentSmelt = Integer.parseInt(block.get("smelt"));
		double smeltingPercentage = (double) (maxSmelt - currentSmelt) / maxSmelt;
		r = (int) (77 + smeltingPercentage * 76);
		g = (int) (30 + smeltingPercentage * 119);
		b = (int) (smeltingPercentage * 148);
		rb = (int) (100 + smeltingPercentage * 53);
		gb = (int) (100 + smeltingPercentage * 49);
		bb = (int) (100 + smeltingPercentage * 48);
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(new Color(rb, gb, bb));
		g.fillRect(x, y, size, size);

		g.setColor(g.getColor().brighter());
		g.drawRect(x, y, size, size);

		g.setColor(new Color(r, this.g, b));
		g.fillRect(x + size * 2 / 6, y + size * 3 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 4 / 6, size / 6, size / 6);
		g.fillRect(x + size * 3 / 6, y + size * 1 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 2 / 6, size / 6, size / 6);
		g.fillRect(x + size * 4 / 6, y + size * 4 / 6, size / 6, size / 6);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(new Color(100, 100, 100));
		g.fillRect(x, y, size, size);

		g.setColor(g.getColor().brighter());
		g.drawRect(x, y, size, size);

		g.setColor(getColor());
		g.fillRect(x + size * 2 / 6, y + size * 3 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 4 / 6, size / 6, size / 6);
		g.fillRect(x + size * 3 / 6, y + size * 1 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 2 / 6, size / 6, size / 6);
		g.fillRect(x + size * 4 / 6, y + size * 4 / 6, size / 6, size / 6);
	}
}
