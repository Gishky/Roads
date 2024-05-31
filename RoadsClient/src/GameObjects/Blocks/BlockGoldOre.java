package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockGoldOre extends Block {

	private int rb, gb, bb;

	public BlockGoldOre(JSONObject block) {
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
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		drawGrain(g, new Color(rb, gb, bb), x, y, size, 10);

		g.setColor(BlockStone.getDefaultColor().brighter());
		g.drawRect(x, y, size, size);

		drawOre(g, getColor(), x, y, size);
	}
}
