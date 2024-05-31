package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockCoalOre extends Block {

	public BlockCoalOre(JSONObject block) {
		setColor(Color.black);

		if (block == null)
			return;

		int maxFuel = 200;
		int currentFuel = Integer.parseInt(block.get("fuel"));
		int colorValue = (maxFuel - currentFuel) * 100 / maxFuel;

		setColor(new Color(colorValue, colorValue, colorValue));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		drawGrain(g, BlockStone.getDefaultColor(), x, y, size,10);

		g.setColor(g.getColor().brighter());
		g.drawRect(x, y, size, size);

		drawOre(g, getColor(), x, y, size);
	}
}
