package GameObjects.Entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.World;
import GameObjects.Blocks.Block;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class PlayerCharacter extends Entity {

	private String name = "";
	protected Block heldBlock;
	protected int breakCount;

	public PlayerCharacter(JSONObject entity) {
		super(entity.get("id"), entity.get("x"), entity.get("y"));
		breakCount = Integer.parseInt(entity.get("breakCount"));
		HPPercent = Float.parseFloat(entity.get("hp%"));
		heldBlock = Block.getBlockFromID(new JSONObject(entity.get("heldBlock")).get("id"), null);
		name = entity.get("name");
		size = Double.parseDouble(entity.get("size"));
	}

	public void updateEntity(JSONObject json) {
		pos.set(json.get("x"), json.get("y"));
		HPPercent = Float.parseFloat(json.get("hp%"));
		heldBlock = Block.getBlockFromID(new JSONObject(json.get("heldBlock")).get("id"), null);
		breakCount = Integer.parseInt(json.get("breakCount"));
		name = json.get("name");
		size = Double.parseDouble(json.get("size"));

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

		Color c = Color.blue.brighter().brighter();
		if (heldBlock != null) {
			c = heldBlock.getColor().darker();
		}

		double posx = (pos.getX() * Block.size);
		double posy = (pos.getY() * Block.size);
		int size = (int) (this.size * Block.size);
		int rad = (int) (0.08 * Block.size);
		if (rad == 0)
			rad = 1;
		double offset = posx % rad;
		for (double x = -size + offset; x < size + offset; x += rad) {
			double diff = Math.sin(Math.acos(x / (size))) * (size);
			for (double y = (int) (-diff) + offset; y < diff + offset; y += rad) {
				int drawx = (int) (posx + x) / rad * rad;
				int drawy = (int) (posy + y) / rad * rad;
				g.setColor(new Color(Math.max(0, Math.min((int) (c.getRed() + (x * y % rad * 5)), 255)),
						Math.max(0, Math.min((int) (c.getGreen() + (x * y % rad * 5)), 255)),
						Math.max(0, Math.min((int) (c.getBlue() + (x * y % rad * 5)), 255)), c.getAlpha()));
				g.fillRect((int) ((drawx) - cameraX + Panel.windowWidth / 2),
						(int) ((drawy - rad / 2) - cameraY + Panel.windowHeight / 2), rad, rad);
			}
		}

		if (breakCount != 0) {
			int blockx = (int) pos.getX();
			int blocky = (int) pos.getY() + 1;
			c = World.getWorld()[blockx][blocky].getColor();

			Random r = new Random();
			for (int i = 0; i < 3; i++) {
				Panel.addParticle(
						new Particle(blockx + r.nextDouble(), blocky + r.nextDouble(), 0, -r.nextDouble() * 0.25 - 0.15,
								r.nextDouble() * 0.025 - 0.015, r.nextDouble() * 0.15 + 0.025, c.brighter()));
			}
		}

		if (id != World.playerid) {
			g.setColor(Color.black);
			g.setFont(new Font("Monospaced", Font.PLAIN, 15));
			g.drawString(name, (int) posx - cameraX + Panel.windowWidth / 2 - g.getFontMetrics().stringWidth(name) / 2,
					(int) posy - cameraY + Panel.windowHeight / 2 - 15);
		}

		super.draw(g, cameraX, cameraY);
	}

}
