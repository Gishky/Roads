package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.Position;
import Window.Panel;

public class PlayerCharacter extends Entity {

	public PlayerCharacter(String id, String x, String y, String hppercent) {
		super(id,x,y,hppercent);
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		super.draw(g, cameraX, cameraY);
		
		g.setColor(Color.black);
		g.fillOval(pos.getX() - 5 - cameraX + Panel.windowWidth / 2, pos.getY() - 5 - cameraY + Panel.windowHeight / 2,10, 10);
	}

}
