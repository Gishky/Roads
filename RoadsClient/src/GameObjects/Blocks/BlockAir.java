package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import Window.Panel;

public class BlockAir extends Block {
	
	public BlockAir() {
		setC(new Color(100, 100, 255));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);
		g.setColor(getC());
		g.fillRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);
		
	}
}
