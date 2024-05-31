package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockIronOre extends Block {

	private int r, g, b;
	private int rb, gb, bb;

	public BlockIronOre(JSONObject block) {
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
		Color b = BlockStone.getDefaultColor();
		rb = (int) (b.getRed() + smeltingPercentage * 53);
		gb = (int) (b.getGreen() + smeltingPercentage * 49);
		bb = (int) (b.getBlue() + smeltingPercentage * 48);
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		drawGrain(g, new Color(rb, gb, bb), x, y, size, 10);

		g.setColor(BlockStone.getDefaultColor().brighter());
		g.drawRect(x, y, size, size);

		drawOre(g, new Color(r, this.g, b), x, y, size);
	}
}
