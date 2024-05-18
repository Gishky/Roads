package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockCoalOre extends Block {

	public BlockCoalOre(JSONObject block) {
		breakThreshhold = 20;
		setC(Color.black);

		if (block == null)
			return;

		int maxFuel = 200;
		int currentFuel = Integer.parseInt(block.get("fuel"));
		int colorValue = (maxFuel - currentFuel) * 100 / maxFuel;

		setC(new Color(colorValue, colorValue, colorValue));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(new Color(100, 100, 100));
		g.fillRect(x, y, size, size);

		g.setColor(g.getColor().brighter());
		g.drawRect(x, y, size, size);

		g.setColor(getC());
		g.fillRect(x + size * 2 / 6, y + size * 3 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 4 / 6, size / 6, size / 6);
		g.fillRect(x + size * 3 / 6, y + size * 1 / 6, size / 6, size / 6);
		// g.fillRect(x + size * 2 / 6, y + size * 2 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 2 / 6, size / 6, size / 6);
		g.fillRect(x + size * 4 / 6, y + size * 4 / 6, size / 6, size / 6);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(new Color(100, 100, 100));
		g.fillRect(x, y, size, size);

		g.setColor(Color.black);
		g.drawRect(x, y, size, size);

		g.setColor(getC());
		g.fillRect(x + size * 2 / 6, y + size * 3 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 4 / 6, size / 6, size / 6);
		g.fillRect(x + size * 3 / 6, y + size * 1 / 6, size / 6, size / 6);
		// g.fillRect(x + size * 2 / 6, y + size * 2 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 2 / 6, size / 6, size / 6);
		g.fillRect(x + size * 4 / 6, y + size * 4 / 6, size / 6, size / 6);
	}
}
