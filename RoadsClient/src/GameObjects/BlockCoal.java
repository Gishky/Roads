package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import Window.Panel;

public class BlockCoal extends Block {

	public BlockCoal() {
		breakThreshhold = 25;
		c = new Color(100, 100, 100);
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(c);
		g.fillRect(x, y, size, size);

		g.setColor(g.getColor().brighter());
		g.drawRect(x, y, size, size);

		g.setColor(Color.black);
		g.fillRect(x + size * 2 / 6, y + size * 3 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 4 / 6, size / 6, size / 6);
		g.fillRect(x + size * 3 / 6, y + size * 1 / 6, size / 6, size / 6);
		//g.fillRect(x + size * 2 / 6, y + size * 2 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 2 / 6, size / 6, size / 6);
		g.fillRect(x + size * 4 / 6, y + size * 4 / 6, size / 6, size / 6);
	}
}
