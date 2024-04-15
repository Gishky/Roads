package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.Position;

public class Entity {

	protected int id;
	protected boolean removed = false;

	protected Position pos;

	protected int breakCount;

	public Entity(String id, String x, String y) {
		this.id = Integer.parseInt(id);
		pos = new Position(Integer.parseInt(x), Integer.parseInt(y));
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public void action() {

	}

	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (removed)
			return;
		g.setColor(Color.gray);
		g.fillOval(pos.getX() - 2 - cameraX, pos.getY() - 2 - cameraY, 4, 4);
	}

	public int getId() {
		return id;
	}

	public void setBreakCount(int count) {
		this.breakCount = count;
	}

	public int getBreakCount() {
		return breakCount;
	}

	public void setRemoved(boolean isRemoved) {
		removed = isRemoved;
	}
}
