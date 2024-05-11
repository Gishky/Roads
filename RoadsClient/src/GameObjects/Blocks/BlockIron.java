package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import Window.Panel;

public class BlockIron extends Block {

	public BlockIron() {
		breakThreshhold = 100;
		setC(new Color(153, 149, 148));
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
	}
}
