package GameObjects.Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class Chomper extends Entity {

	public Chomper(JSONObject json) {
		super(json.get("id"), json.get("x"), json.get("y"));

		size = (int) (Double.parseDouble(json.get("size")) * Block.size) * 2;
		HPPercent = Integer.parseInt(json.get("hp%"));
	}

	public void updateEntity(JSONObject json) {
		pos.set(json.get("x"), json.get("y"));
		if (Integer.parseInt(json.get("hp%")) != HPPercent) {
			Random r = new Random();
			Color c = Color.red;
			for (int i = 0; i < 2 * size; i++) {
				Particle p = new Particle(pos.getX() + r.nextDouble() * size - size / 2,
						pos.getY() + r.nextDouble() * size - size / 2,
						r.nextDouble() * 0.1 * Block.size - 0.05 * Block.size, -(r.nextDouble() * 0.1 * Block.size), 0,
						0, c);
				p.setLifetime(r.nextInt(10) + 3);
				Panel.addParticle(p);
			}
		}
		HPPercent = Integer.parseInt(json.get("hp%"));
		size = (int) (Double.parseDouble(json.get("size")) * Block.size) * 2;
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		g.setColor(new Color(255, 0, 0, 100));
		int rad = (int) Math.round(Block.size * 0.06);
		if(rad ==0)
			rad = 1;
		for (double x = -size / 2; x < size / 2; x += rad) {
			double diff = Math.sin(Math.acos(x / (size / 2))) * (size / 2);
			for (double y = (int) (-diff); y < diff; y += rad) {
				g.fillRect((int) (pos.getX() - x - rad/2) - cameraX + Panel.windowWidth / 2,
						(int) (pos.getY() - y - rad/2) - cameraY + Panel.windowHeight / 2,
						rad, rad);
			}
		}
		if (delete) {
			Random r = new Random();
			Color c = Color.red;
			for (int i = 0; i < 4 * size; i++) {
				Particle p = new Particle(pos.getX() + r.nextDouble() * size - size / 2,
						pos.getY() + r.nextDouble() * size - size / 2,
						r.nextDouble() * 0.1 * Block.size - 0.05 * Block.size, -(r.nextDouble() * 0.1 * Block.size), 0,
						0, c);
				p.setLifetime(r.nextInt(10) + 3);
				Panel.addParticle(p);
			}
		}
		super.draw(g, cameraX, cameraY);
	}
}
