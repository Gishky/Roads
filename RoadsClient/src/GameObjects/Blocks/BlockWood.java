package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockWood extends Block {

	public BlockWood(JSONObject block) {
		setColor(new Color(109, 59, 9));
	}

	public static Color getDefaultColor() {
		return new Color(109, 59, 9);
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		drawWoodGrain(g, getColor(), x, y, size, 10);

		g.setColor(g.getColor().brighter());
		g.drawRect(x, y, size, size);
	}

}
