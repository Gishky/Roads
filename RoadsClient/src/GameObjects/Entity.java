package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.Position;
import Window.Panel;

public class Entity {

	protected int id;

	protected Position pos;

	protected Block heldBlock;
	protected int breakCount;
	protected int HPPercent = 100;

	protected boolean delete = false;

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

		if (delete) {
			Panel.removeEntity(this);
		}
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

	public void setHPPercent(int int1) {
		HPPercent = int1;
	}

	public void setHeldBlock(Block b) {
		heldBlock = b;
	}

	public Block getHeldBlock() {
		return heldBlock;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}
