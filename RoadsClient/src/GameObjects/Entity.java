package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.Position;

public class Entity {

	protected int id;

	protected Position pos;


	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public void action() {

	}
	
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		g.setColor(Color.gray);
		g.fillOval(pos.getX()-2-cameraX, pos.getY()-2-cameraY, 4, 4);
	}

	public int getId() {
		return id;
	}

}
