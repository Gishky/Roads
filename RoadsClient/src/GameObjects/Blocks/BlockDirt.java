package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockDirt extends Block {

	public BlockDirt(JSONObject block) {
		setColor(new Color(139, 69, 19));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		drawGrain(g, getColor(), x, y,size,10);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
