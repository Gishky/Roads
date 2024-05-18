package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import Window.Panel;

public class BlockOven extends Block {

	public BlockOven() {
		breakThreshhold = 40;
		setC(new Color(80, 80, 80));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(getC());
		g.fillRect(x, y, size, size);

		g.setColor(g.getColor().brighter());
		g.drawRect(x, y, size, size);
		g.fillRect(x + size / 4, y + size / 4, size / 2, size / 5);
	}
	
	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(getC());
		g.fillRect(x, y, size, size);

		g.setColor(Color.black);
		g.drawRect(x, y, size, size);
		g.fillRect(x + size / 4, y + size / 4, size / 2, size / 5);
	}

}
