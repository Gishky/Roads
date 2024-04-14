package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import Window.Panel;

public class BlockDirt extends Block {

	public BlockDirt() {
		breakThreshhold = 25;
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);
		g.setColor(new Color(139, 69, 19));

		Color c = g.getColor();
		g.setColor(new Color((int) (c.getRed() * (1 - World.getBlockBreakCount(x, y) / breakThreshhold)),
				(int) (c.getGreen() * (1 - World.getBlockBreakCount(x, y) / breakThreshhold)),
				(int) (c.getBlue() * (1 - World.getBlockBreakCount(x, y) / breakThreshhold))));

		g.fillRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);

		g.setColor(g.getColor().brighter());
		g.drawRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);
	}
}
