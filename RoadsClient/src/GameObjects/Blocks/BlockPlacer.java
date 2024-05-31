package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockPlacer extends Block {

	int dir = 1;

	public BlockPlacer(JSONObject block) {
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
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		switch (dir) {
		case 1:
			xPoints[0] = x;
			xPoints[1] = x + size;
			xPoints[2] = x;
			yPoints[0] = y;
			yPoints[1] = y + size / 2;
			yPoints[2] = y + size;
			break;
		case 2:
			xPoints[0] = x;
			xPoints[1] = x + size / 2;
			xPoints[2] = x + size;
			yPoints[0] = y;
			yPoints[1] = y + size;
			yPoints[2] = y;
			break;
		case 3:
			xPoints[0] = x + size;
			xPoints[1] = x;
			xPoints[2] = x + size;
			yPoints[0] = y;
			yPoints[1] = y + size / 2;
			yPoints[2] = y + size;
			break;
		case 4:
			xPoints[0] = x;
			xPoints[1] = x + size / 2;
			xPoints[2] = x + size;
			yPoints[0] = y + size;
			yPoints[1] = y;
			yPoints[2] = y + size;
			break;
		}
		g.fillPolygon(xPoints, yPoints, 3);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		g.setColor(new Color(255, 215, 0));
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		switch (dir) {
		case 1:
			xPoints[0] = x;
			xPoints[1] = x + size;
			xPoints[2] = x;
			yPoints[0] = y;
			yPoints[1] = y + size / 2;
			yPoints[2] = y + size;
			break;
		case 2:
			xPoints[0] = x;
			xPoints[1] = x + size / 2;
			xPoints[2] = x + size;
			yPoints[0] = y;
			yPoints[1] = y + size;
			yPoints[2] = y;
			break;
		case 3:
			xPoints[0] = x + size;
			xPoints[1] = x;
			xPoints[2] = x + size;
			yPoints[0] = y;
			yPoints[1] = y + size / 2;
			yPoints[2] = y + size;
			break;
		case 4:
			xPoints[0] = x;
			xPoints[1] = x + size / 2;
			xPoints[2] = x + size;
			yPoints[0] = y + size;
			yPoints[1] = y;
			yPoints[2] = y + size;
			break;
		}
		g.fillPolygon(xPoints, yPoints, 3);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
