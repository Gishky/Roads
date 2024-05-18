package GameObjects;

import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import HelperObjects.Position;
import Window.Panel;

public class Entity {

	protected int id;

	protected Position pos;

	protected boolean delete = false;

	public Entity(String id, String x, String y) {
		this.id = Integer.parseInt(id);
		pos = new Position(Double.parseDouble(x), Double.parseDouble(y));
	}

	public void updateEntity(JSONObject entity) {

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
		if (delete) {
			Panel.removeEntity(this);
		}
	}

	public int getId() {
		return id;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
