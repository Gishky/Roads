package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class PlayerCharacter extends Entity {

	public PlayerCharacter(JSONObject entity) {
		super(entity.get("id"), entity.get("x"), entity.get("y"), entity.get("hp%"),
				new JSONObject(entity.get("heldBlock")).get("id"));
		breakCount = Integer.parseInt(entity.get("breakCount"));
	}

	public void updateEntity(JSONObject json) {
		pos.setX(json.get("x"));
		pos.setY(json.get("y"));
		HPPercent = Integer.parseInt(json.get("hp%"));
		heldBlock = Block.getBlockFromID(new JSONObject(json.get("heldBlock")).get("id"));
		breakCount = Integer.parseInt(json.get("breakCount"));

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

		g.setColor(Color.blue.brighter().brighter());
		if (heldBlock != null) {
			g.setColor(heldBlock.getColor().darker());
		}

		g.fillOval((int) pos.getX() - 5 - cameraX + Panel.windowWidth / 2,
				(int) pos.getY() - 5 - cameraY + Panel.windowHeight / 2, 10, 10);

		if (breakCount != 0) {
			int blockx = (int) pos.getX() / Block.size;
			int blocky = (int) pos.getY() / Block.size + 1;
			Color c = World.getWorld()[blockx][blocky].getC();

			Random r = new Random();
			for (int i = 0; i < 3; i++) {
				Panel.addParticle(new Particle(blockx * Block.size + r.nextDouble() * Block.size,
						blocky * Block.size + r.nextDouble() * Block.size, 0,
						-r.nextDouble() * 0.25 * Block.size - 0.15 * Block.size,
						r.nextDouble() * 0.025 * Block.size - 0.015 * Block.size,
						r.nextDouble() * 0.15 * Block.size + 0.025 * Block.size, c.brighter()));
			}
		}

		super.draw(g, cameraX, cameraY);
	}

}
