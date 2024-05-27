package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockGoldChunk extends Block {

	public BlockGoldChunk(JSONObject block) {
		setColor(new Color(255, 215, 0));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(getColor());
		g.fillRect(x, y, size, size);
		
		g.setColor(getColor().darker());
		g.fillRect(x + size * 2 / 6, y + size * 3 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 4 / 6, size / 6, size / 6);
		g.fillRect(x + size * 3 / 6, y + size * 1 / 6, size / 6, size / 6);
		g.fillRect(x + size * 1 / 6, y + size * 2 / 6, size / 6, size / 6);
		g.fillRect(x + size * 4 / 6, y + size * 4 / 6, size / 6, size / 6);

		g.setColor(g.getColor().brighter());
		g.drawRect(x, y, size, size);
	}
	
	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		g.setColor(g.getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
