package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import Window.Panel;

public class BlockDirt extends Block{
	@Override
	public void draw(int x, int y, Graphics2D g) {
		super.draw(x, y, g);
		g.setColor(new Color(139, 69, 19));
		g.fillRect(x * size - World.cameraX + Panel.windowWidth / 2,
				y * size - World.cameraY + Panel.windowHeight / 2, size, size);
	}
}
