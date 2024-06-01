package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockWood extends Block {

	private int fuel = 50;
	private boolean coal = false;

	public BlockWood(JSONObject block) {
		setColor(new Color(109, 59, 9));

		if (block == null)
			return;

		if (block.get("fuel") != null)
			fuel = Integer.parseInt(block.get("fuel"));
		if (block.get("coal") != null)
			coal = block.get("coal").equals("true");
	}

	public static Color getDefaultColor() {
		return new Color(109, 59, 9);
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		Color c = getColor();
		if (coal)
			c = interpolateColor(interpolateColor(getColor(), Color.black, 0.7),
					interpolateColor(getColor(), Color.gray, 0.7), 1 - ((double) fuel) / 600);
		else
			c = interpolateColor(getColor(), interpolateColor(getColor(), Color.black, 0.8), ((double) fuel) / 600);

		g.setColor(c);
		g.fillRect(x, y, size, size);

		drawWoodGrain(g, c, x, y, size, 10);

		g.setColor(c.brighter());
		g.drawRect(x, y, size, size);
	}

}
