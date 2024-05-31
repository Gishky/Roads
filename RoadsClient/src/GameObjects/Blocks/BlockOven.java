package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockOven extends Block {

	private int fuelPercentage = 0;

	public BlockOven(JSONObject block) {
		setColor(new Color(80, 80, 80));

		if (block == null)
			return;

		fuelPercentage = Integer.parseInt(block.get("fuel"));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		drawGrain(g, getColor(), x, y, size,10);

		g.setColor(getColor().brighter());
		g.fillRect(x + size / 4, y + size / 4, size / 2, size / 5);
		int slotsize = size / 9;
		for (int slots = 0; slots < 4; slots++) {
			g.fillRect(x + slotsize * (1 + 2 * slots) + 1, y + size * 3 / 5, slotsize, size / 4);
		}

		g.setColor(getColor().darker());
		for (int slots = 0; slots < 4; slots++) {
			if (fuelPercentage > slots * 25) {
				int slotheight = (int) ((double) size / 4 * Math.min(25, fuelPercentage - slots * 25) / 25);
				g.fillRect(x + slotsize * (1 + 2 * slots) + 1, y + size * 3 / 5 + size / 4 - slotheight, slotsize,
						slotheight);
			}
		}

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
