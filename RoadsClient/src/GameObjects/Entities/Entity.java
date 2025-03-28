package GameObjects.Entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import GameObjects.Blocks.Block;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Window.Panel;

public class Entity {

	protected int id;

	protected Position pos;

	protected boolean delete = false;
	protected float HPPercent = 100;
	protected double size;

	public Entity(String id, String x, String y) {
		this.id = Integer.parseInt(id);
		pos = new Position(Double.parseDouble(x), Double.parseDouble(y));
	}

	public Entity() {

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

		int x = (int) (pos.getX() * Block.size);
		int y = (int) (pos.getY() * Block.size);
		int size = (int) (this.size * Block.size);
		if (HPPercent < 100) {
			int HPBarLength = 20;
			int HPBarHeight = 5;

			g.setColor(Color.gray);
			g.fillRect(x - cameraX - HPBarLength / 2 + Panel.windowWidth / 2,
					y - cameraY - HPBarHeight / 2 + Panel.windowHeight / 2 - size, HPBarLength, HPBarHeight);
			g.setColor(Color.green);
			g.fillRect(x - cameraX - HPBarLength / 2 + Panel.windowWidth / 2,
					y - cameraY - HPBarHeight / 2 + Panel.windowHeight / 2 - size,
					(int) (HPBarLength * HPPercent / 100), HPBarHeight);
		}
		if (Panel.isStatistics()) {
			g.setColor(Color.black);
			g.setFont(new Font("Monospaced", Font.PLAIN, 15));
			g.drawString("" + id, x - cameraX + Panel.windowWidth / 2 - g.getFontMetrics().stringWidth("" + id) / 2,
					y - cameraY + Panel.windowHeight / 2 - 15);

			g.drawOval((x - cameraX + Panel.windowWidth / 2 - size), (y - cameraY + Panel.windowHeight / 2 - size),
					(size * 2), (size * 2));
		}
	}

	public int getId() {
		return id;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
