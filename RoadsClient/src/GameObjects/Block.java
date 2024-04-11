package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import Window.Panel;

public class Block {
	protected static int size = 20;

	private int updateCount = 0;

	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		updateCount--;
		int xDraw = x * size - cameraX + Panel.windowWidth / 2;
		int yDraw = y * size - cameraY + Panel.windowHeight / 2;
		if (updateCount <= 0 && xDraw >= -size * 2 && xDraw <= Panel.windowWidth + size * 2 && yDraw >= -size * 2
				&& yDraw <= Panel.windowHeight + size * 2) {
			Panel.getServerConnection().sendMessage("block;" + x + ";" + y);
			updateCount = 100;
		}
	}

}
