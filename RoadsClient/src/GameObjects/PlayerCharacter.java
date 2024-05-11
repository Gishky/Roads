package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import Window.Panel;

public class PlayerCharacter extends Entity {

	public PlayerCharacter(String id, String x, String y, String hppercent, String heldblock) {
		super(id, x, y, hppercent, heldblock);
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		g.setColor(Color.black);
		if (heldBlock != null) {
			g.setColor(heldBlock.getColor().darker());
		}
		g.fillOval(pos.getX() - 5 - cameraX + Panel.windowWidth / 2, pos.getY() - 5 - cameraY + Panel.windowHeight / 2,
				10, 10);

		super.draw(g, cameraX, cameraY);
	}

}
