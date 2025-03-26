package GameObjects.Entities.Debug;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import GameObjects.Blocks.Block;
import GameObjects.Entities.Entity;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Window.Panel;

public class DebugText extends Entity {

	private String text = "";

	private int lifeTime = 0;
	private Color color;

	public DebugText(JSONObject message) {
		pos = new Position(Double.parseDouble(message.get("x")),
				Double.parseDouble(message.get("y")));
		text = message.get("text");
		lifeTime = Integer.parseInt(message.get("lifeTime"));
		color = new Color(Integer.parseInt(message.get("red")), Integer.parseInt(message.get("green")),
				Integer.parseInt(message.get("blue")));
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (lifeTime-- < 0) {
			Panel.removeEntity(this);
		}
		if (!Panel.isStatistics())
			return;
		g.setColor(color);
		g.setFont(new Font("Monospaced", Font.PLAIN, 15));
		g.drawString(text,
				(int) pos.getX() - cameraX + Panel.windowWidth / 2 - g.getFontMetrics().stringWidth(text) / 2,
				(int) pos.getY() - cameraY + Panel.windowHeight / 2);
	}
}
