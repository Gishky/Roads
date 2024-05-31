package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockMachine extends Block {

	int dir = 1;

	public BlockMachine(JSONObject block) {
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

		drawGrain(g, getColor(), x, y, size, 10);

		g.setColor(BlockGold.getDefaultColor());
		if (activated) {
			g.setColor(Color.blue);
			activated = false;
		}
		g.fillRect(x, y, size * 2 / 6, size);

		g.setColor(darkerColor(g.getColor(), 20));
		drawPixel(g, x, y, 0, 0, size);
		drawPixel(g, x, y, 0, 2, size);
		drawPixel(g, x, y, 1, 3, size);
		drawPixel(g, x, y, 1, 5, size);
		drawPixel(g, x, y, 0, 0, size);

		g.setColor(brighterColor(g.getColor(), 40));
		drawPixel(g, x, y, 1, 1, size);
		drawPixel(g, x, y, 0, 5, size);
		drawPixel(g, x, y, 1, 4, size);

		g.setColor(BlockIron.getDefaultColor());
		g.fillRect(x + size * 2 / 6, y + size * 2 / 6, size * 4 / 6, size * 2 / 6);

		g.setColor(darkerColor(g.getColor(), 20));
		drawPixel(g, x, y, 5, 2, size);
		drawPixel(g, x, y, 2, 3, size);
		drawPixel(g, x, y, 4, 3, size);

		g.setColor(brighterColor(g.getColor(), 40));
		drawPixel(g, x, y, 3, 2, size);

		g.setTransform(old);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
