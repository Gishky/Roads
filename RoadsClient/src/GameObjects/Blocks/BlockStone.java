package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockStone extends Block {

	public BlockStone(JSONObject block) {
		setColor(new Color(100, 100, 100));
	}

	public static Color getDefaultColor() {
		return new Color(100, 100, 100);
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		drawGrain(g, getColor(), x, y, size,10);

		g.setColor(g.getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
