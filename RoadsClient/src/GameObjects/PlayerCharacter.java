package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.Position;
import Window.Panel;

public class PlayerCharacter extends Entity {

	public PlayerCharacter(String id, String x, String y) {
		this.id = Integer.parseInt(id);
		pos = new Position(Integer.parseInt(x), Integer.parseInt(y));
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		g.setColor(Color.black);
		if (id == World.playerid) {
			cameraX = World.cameraX;
			cameraY = World.cameraY;
		}
		g.fillOval(pos.getX() - 5 - cameraX + Panel.windowWidth / 2, pos.getY() - 5 - cameraY + Panel.windowHeight / 2,
				10, 10);
	}

}
