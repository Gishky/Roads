package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockGoldOre extends Block {

	private int rb, gb, bb;

	public BlockGoldOre(JSONObject block) {
		breakThreshhold = 24;
		setColor(new Color(255, 215, 0));
		rb = 100;
		gb = 100;
		bb = 100;

		if (block == null)
			return;

		int maxSmelt = 300;
		int currentSmelt = Integer.parseInt(block.get("smelt"));
		double smeltingPercentage = (double) (maxSmelt - currentSmelt) / maxSmelt;

		rb = (int) (100 + smeltingPercentage * 155);
		gb = (int) (100 + smeltingPercentage * 115);
		bb = (int) (100 + smeltingPercentage * -100);
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

		g.setColor(getColor());
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
