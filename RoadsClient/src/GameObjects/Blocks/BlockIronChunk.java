package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockIronChunk extends Block {

	public BlockIronChunk(JSONObject block) {
		setColor(new Color(153, 149, 148));
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
