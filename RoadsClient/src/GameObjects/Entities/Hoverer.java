package GameObjects.Entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class Hoverer extends Entity {

	public Hoverer(JSONObject json) {
		super(json.get("id"), json.get("x"), json.get("y"));

		size = Double.parseDouble(json.get("size")) * 2;
		HPPercent = Float.parseFloat(json.get("hp%"));
	}

	public void updateEntity(JSONObject json) {
		pos.set(json.get("x"), json.get("y"));
		float newhp = Float.parseFloat(json.get("hp%"));
		if (newhp < HPPercent) {
			Random r = new Random();
			Color c = Color.red;
			for (int i = 0; i < 2 * size * Block.size; i++) {
				Particle p = new Particle(pos.getX() + r.nextDouble() * size - size / 2,
						pos.getY() + r.nextDouble() * size - size / 2,
						r.nextDouble() * 0.1 * Block.size - 0.05 * Block.size, -(r.nextDouble() * 0.1 * Block.size), 0,
						0, c);
				p.setLifetime(r.nextInt(10) + 3);
				Panel.addParticle(p);
			}
		}
		HPPercent = newhp;
		size = Double.parseDouble(json.get("size")) * 2;
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		g.setColor(new Color(14, 0, 186, 160));

		int size = (int) (this.size * Block.size);
		int rad = (int) Math.round(0.08 * Block.size);
		if (rad == 0)
			rad = 1;
		double offset = pos.getX() % rad;
		for (double x = -size / 2 + offset; x < size / 2 + offset; x += rad) {
			double diff = Math.sin(Math.acos(x / (size / 2))) * (size / 2);
			for (double y = (int) (-diff); y < diff; y += rad) {
				int drawx = (int) (pos.getX() + x) / rad * rad;
				int drawy = (int) (pos.getY() + y) / rad * rad;
				g.fillRect((int) ((drawx - rad / 2) - cameraX + Panel.windowWidth / 2),
						(int) ((drawy - rad / 2) - cameraY + Panel.windowHeight / 2), rad, rad);
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
