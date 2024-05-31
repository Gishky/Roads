package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockPlacer extends Block {

	int dir = 1;

	public BlockPlacer(JSONObject block) {
		setColor(BlockWood.getDefaultColor().brighter());

		if (block != null)
			dir = Integer.parseInt(block.get("dir"));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		AffineTransform old = g.getTransform();
		g.rotate(Math.toRadians((dir - 1) * 90), x + size / 2, y + size / 2);

		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		g.setColor(BlockGold.getDefaultColor());
		if (activated) {
			g.setColor(Color.blue);
			activated = false;
		}

		g.fillRect(x, y + size * 2 / 6, size, size * 2 / 6);
		g.fillRect(x, y, size * 1 / 6, size);
		drawPixel(g, x, y, 1, 1, size);

		g.setColor(brighterColor(g.getColor(), 20));
		drawPixel(g, x, y, 1, 4, size);
		drawPixel(g, x, y, 0, 1, size);
		drawPixel(g, x, y, 4, 2, size);
		drawPixel(g, x, y, 2, 3, size);
		drawPixel(g, x, y, 5, 2, size);
		g.setColor(darkerColor(g.getColor(), 40));
		drawPixel(g, x, y, 0, 4, size);
		drawPixel(g, x, y, 2, 2, size);
		drawPixel(g, x, y, 4, 2, size);
		drawPixel(g, x, y, 1, 3, size);

		g.setColor(BlockIron.getDefaultColor());
		drawPixel(g, x, y, 0, 2, size);
		g.setColor(brighterColor(g.getColor(), 20));
		drawPixel(g, x, y, 0, 3, size);

		g.setTransform(old);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
