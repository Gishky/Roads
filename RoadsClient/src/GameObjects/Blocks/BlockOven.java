package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockOven extends Block {

	private int fuelPercentage = 0;

	public BlockOven(JSONObject block) {
		breakThreshhold = 40;
		setC(new Color(80, 80, 80));

		if (block == null)
			return;

		fuelPercentage = Integer.parseInt(block.get("fuel"));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(getC());
		g.fillRect(x, y, size, size);

		g.setColor(getC().darker());
		g.fillRect(x, y + size - size * fuelPercentage / 100, size, size * fuelPercentage / 100);

		g.setColor(getC().brighter());
		g.drawRect(x, y, size, size);
		g.fillRect(x + size / 4, y + size / 4, size / 2, size / 5);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(getC());
		g.fillRect(x, y, size, size);

		g.setColor(getC().darker());
		g.fillRect(x, y + size - size * fuelPercentage / 100, size, size * fuelPercentage / 100);

		g.setColor(getC().brighter());
		g.drawRect(x, y, size, size);
		g.fillRect(x + size / 4, y + size / 4, size / 2, size / 5);
	}

}
