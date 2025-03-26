package GameObjects.Entities.Debug;

import java.awt.Color;
import java.awt.Graphics2D;

import GameObjects.Blocks.Block;
import GameObjects.Entities.Entity;
import HelperObjects.JSONObject;
import Window.Panel;

public class DebugLine extends Entity {

	private double xFrom, xTo, yFrom, yTo;

	private int lifeTime = 0;
	private Color color;

	public DebugLine(JSONObject message) {
		xFrom = Double.parseDouble(message.get("xFrom")) * Block.size;
		xTo = Double.parseDouble(message.get("xTo")) * Block.size;
		yFrom = Double.parseDouble(message.get("yFrom")) * Block.size;
		yTo = Double.parseDouble(message.get("yTo")) * Block.size;
		lifeTime = Integer.parseInt(message.get("lifeTime"));
		color = new Color(Integer.parseInt(message.get("red")), Integer.parseInt(message.get("green")),
				Integer.parseInt(message.get("blue")));
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (lifeTime-- < 0) {
			Panel.removeEntity(this);
		}
		if(!Panel.isStatistics())
			return;
		g.setColor(color);

		g.drawLine((int) xFrom - cameraX + Panel.windowWidth / 2, (int) yFrom - cameraY + Panel.windowHeight / 2,
				(int) xTo - cameraX + Panel.windowWidth / 2, (int) yTo - cameraY + Panel.windowHeight / 2);
		g.setColor(new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()));
		g.drawOval((int) xFrom - cameraX + Panel.windowWidth / 2 - 1, (int) yFrom - cameraY + Panel.windowHeight / 2, 2,
				2);
	}
}
