package GameObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class PlayerCharacter extends Entity {

	private String name = "";
	protected Block heldBlock;
	protected int breakCount;
	protected int HPPercent = 100;
	private int size;

	public PlayerCharacter(JSONObject entity) {
		super(entity.get("id"), entity.get("x"), entity.get("y"));
		breakCount = Integer.parseInt(entity.get("breakCount"));
		HPPercent = Integer.parseInt(entity.get("hp%"));
		heldBlock = Block.getBlockFromID(new JSONObject(entity.get("heldBlock")).get("id"), null);
		name = entity.get("name");
		size = (int) (Double.parseDouble(entity.get("size")) * Block.size) * 2;
	}

	public void updateEntity(JSONObject json) {
		pos.setX(json.get("x"));
		pos.setY(json.get("y"));
		HPPercent = Integer.parseInt(json.get("hp%"));
		heldBlock = Block.getBlockFromID(new JSONObject(json.get("heldBlock")).get("id"), null);
		breakCount = Integer.parseInt(json.get("breakCount"));
		name = json.get("name");
		size = (int) (Double.parseDouble(json.get("size")) * Block.size) * 2;

		if (id == World.playerid) {
			World.cameraX = Double.parseDouble(json.get("x"));
			World.cameraY = Double.parseDouble(json.get("y"));
		}
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (id == World.playerid) {
			cameraX += World.cameraX * Block.size - cameraX;
			cameraY += World.cameraY * Block.size - cameraY;
		}

		if (HPPercent != 100) {
			int HPBarLength = 20;
			int HPBarHeight = 5;
			int HPBarOffset = -10;

			g.setColor(Color.gray);
			g.fillRect((int) pos.getX() - cameraX - HPBarLength / 2 + Panel.windowWidth / 2,
					(int) pos.getY() - cameraY - HPBarHeight / 2 + HPBarOffset + Panel.windowHeight / 2, HPBarLength,
					HPBarHeight);
			g.setColor(Color.green);
			g.fillRect((int) pos.getX() - cameraX - HPBarLength / 2 + Panel.windowWidth / 2,
					(int) pos.getY() - cameraY - HPBarHeight / 2 + HPBarOffset + Panel.windowHeight / 2,
					HPBarLength * HPPercent / 100, HPBarHeight);
		}

		g.setColor(Color.blue.brighter().brighter());
		if (heldBlock != null) {
			g.setColor(heldBlock.getColor().darker());
		}

		g.fillOval((int) pos.getX() - size / 2 - cameraX + Panel.windowWidth / 2,
				(int) pos.getY() - size / 2 - cameraY + Panel.windowHeight / 2, size, size);

		if (breakCount != 0) {
			int blockx = (int) pos.getX() / Block.size;
			int blocky = (int) pos.getY() / Block.size + 1;
			Color c = World.getWorld()[blockx][blocky].getColor();

			Random r = new Random();
			for (int i = 0; i < 3; i++) {
				Panel.addParticle(new Particle(blockx * Block.size + r.nextDouble() * Block.size,
						blocky * Block.size + r.nextDouble() * Block.size, 0,
						-r.nextDouble() * 0.25 * Block.size - 0.15 * Block.size,
						r.nextDouble() * 0.025 * Block.size - 0.015 * Block.size,
						r.nextDouble() * 0.15 * Block.size + 0.025 * Block.size, c.brighter()));
			}
		}

		if (id != World.playerid) {
			g.setColor(Color.black);
			g.setFont(new Font("Monospaced", Font.PLAIN, 15));
			g.drawString(name,
					(int) pos.getX() - cameraX + Panel.windowWidth / 2 - g.getFontMetrics().stringWidth(name) / 2,
					(int) pos.getY() - cameraY + Panel.windowHeight / 2 - 15);
		}

		super.draw(g, cameraX, cameraY);
	}

}
