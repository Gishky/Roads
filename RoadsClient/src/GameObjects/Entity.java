package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.Position;
import Window.Panel;

public class Entity {

	protected int id;
	protected boolean removed = false;

	protected Position pos;

	protected int breakCount;
	protected int HPPercent = 100;

	public Entity(String id, String x, String y, String hppercent) {
		this.id = Integer.parseInt(id);
		pos = new Position(Integer.parseInt(x), Integer.parseInt(y));
		HPPercent = Integer.parseInt(hppercent);
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
		if (HPPercent != 100) {
			int HPBarLength = 20;
			int HPBarHeight = 5;
			int HPBarOffset = -10;

			g.setColor(Color.gray);
			g.fillRect(pos.getX() - cameraX - HPBarLength / 2 + Panel.windowWidth / 2,
					pos.getY() - cameraY - HPBarHeight / 2 + HPBarOffset + Panel.windowHeight / 2, HPBarLength,
					HPBarHeight);
			g.setColor(Color.green);
			g.fillRect(pos.getX() - cameraX - HPBarLength / 2 + Panel.windowWidth / 2,
					pos.getY() - cameraY - HPBarHeight / 2 + HPBarOffset + Panel.windowHeight / 2,
					HPBarLength * HPPercent / 100, HPBarHeight);
		}

		g.setColor(Color.pink);
		g.fillOval(pos.getX() - 2 - cameraX + Panel.windowWidth / 2, pos.getY() - 2 - cameraY + Panel.windowHeight / 2,
				4, 4);
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

	public void setHPPercent(int int1) {
		HPPercent = int1;
	}
}
