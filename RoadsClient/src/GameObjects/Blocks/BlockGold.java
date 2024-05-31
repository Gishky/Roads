package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockGold extends Block {

	public BlockGold(JSONObject block) {
		setColor(new Color(255, 215, 0));
	}

	public static Color getDefaultColor() {
		return new Color(255, 215, 0);
	}
	
	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(getColor());
		g.fillRect(x, y, size, size);
		
		drawWoodGrain(g, getColor(), x, y, size, 5);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
