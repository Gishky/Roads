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
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;
		
		drawGrain(g, getColor(), x, y, size, 40);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}
}
