package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import Window.Panel;

public class BlockStone extends Block {

	public BlockStone() {
		breakThreshhold = 15;
		setC(new Color(100, 100, 100));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);
		g.setColor(getC());
		g.fillRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);

		g.setColor(g.getColor().brighter());
		g.drawRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(getC());
		g.fillRect(x, y, size, size);

		g.setColor(Color.black);
		g.drawRect(x, y, size, size);
	}
}
