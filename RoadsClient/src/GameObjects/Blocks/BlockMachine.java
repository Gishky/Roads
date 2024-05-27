package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockMachine extends Block {
	int dir = 0;

	public BlockMachine(JSONObject block) {
		setColor(new Color(109, 59, 9));

		if (block != null)
			dir = Integer.parseInt(block.get("dir"));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		g.setColor(new Color(255, 215, 0));
		if (activated) {
			g.setColor(Color.blue);
			activated = false;
		}
		switch (dir) {
		case 1:
			g.fillRect(x, y, size * 2 / 6, size);
			break;
		case 2:
			g.fillRect(x, y, size, size * 2 / 6);
			break;
		case 3:
			g.fillRect(x + size * 4 / 6, y, size * 2 / 6, size);
			break;
		case 4:
			g.fillRect(x, y + size * 4 / 6, size, size * 2 / 6);
			break;
		}

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		g.setColor(new Color(255, 215, 0));
		if (activated) {
			g.setColor(Color.blue);
			activated = false;
		}
		switch (dir) {
		case 1:
			g.fillRect(x, y, size * 2 / 6, size);
			break;
		case 2:
			g.fillRect(x, y, size, size * 2 / 6);
			break;
		case 3:
			g.fillRect(x + size * 4 / 6, y, size * 2 / 6, size);
			break;
		case 4:
			g.fillRect(x, y + size * 4 / 6, size, size * 2 / 6);
			break;
		}

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
