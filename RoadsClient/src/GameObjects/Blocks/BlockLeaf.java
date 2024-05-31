package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockLeaf extends Block {

	public BlockLeaf(JSONObject block) {
		setColor(new Color(0, 200, 0, 100));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(BlockAir.getSkyColor(x, y));
		g.fillRect(x, y, size, size);

		drawGrain(g, getColor(), x, y, size, 5);
	}
}
