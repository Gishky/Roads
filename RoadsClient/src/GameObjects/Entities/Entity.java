package GameObjects.Entities;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import HelperObjects.Position;
import Window.Panel;

public class Entity {

	protected int id;

	protected Position pos;

	protected boolean delete = false;
	protected int HPPercent = 100;
	protected int size;

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
		if (HPPercent != 100) {
			int HPBarLength = 20;
			int HPBarHeight = 5;

			g.setColor(Color.gray);
			g.fillRect((int) pos.getX() - cameraX - HPBarLength / 2 + Panel.windowWidth / 2,
					(int) pos.getY() - cameraY - HPBarHeight / 2 + Panel.windowHeight / 2 - size, HPBarLength,
					HPBarHeight);
			g.setColor(Color.green);
			g.fillRect((int) pos.getX() - cameraX - HPBarLength / 2 + Panel.windowWidth / 2,
					(int) pos.getY() - cameraY - HPBarHeight / 2 + Panel.windowHeight / 2 - size,
					HPBarLength * HPPercent / 100, HPBarHeight);
		}
	}

	public int getId() {
		return id;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
