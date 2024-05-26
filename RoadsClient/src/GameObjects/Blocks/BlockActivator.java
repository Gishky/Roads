package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockActivator extends Block {

	public BlockActivator(JSONObject block) {
		setColor(new Color(109, 59, 9));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);
		g.setColor(getColor());
		g.fillRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);

		g.setColor(new Color(255, 215, 0));
		g.fillRect(x * size - cameraX + Panel.windowWidth / 2 + size * 2 / 6,
				y * size - cameraY + Panel.windowHeight / 2 + size * 2 / 6, size * 2 / 6, size * 2 / 6);

		g.setColor(getColor().brighter());
		g.drawRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		g.setColor(new Color(255, 215, 0));
		g.fillRect(x + size * 2 / 6, y + size * 2 / 6, size * 2 / 6, size * 2 / 6);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
