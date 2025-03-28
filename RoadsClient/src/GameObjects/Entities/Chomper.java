package GameObjects.Entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class Chomper extends Entity {

	public Chomper(JSONObject json) {
		super(json.get("id"), json.get("x"), json.get("y"));

		size = Double.parseDouble(json.get("size"));
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
						r.nextDouble() * 0.1  - 0.05 , -(r.nextDouble() * 0.1 ), 0,
						0, c);
				p.setLifetime(r.nextInt(10) + 3);
				Panel.addParticle(p);
			}
		}
		HPPercent = newhp;
		size = Double.parseDouble(json.get("size"));
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		g.setColor(new Color(255, 0, 0, 100));

		double posx = (pos.getX() * Block.size);
		double posy = (pos.getY() * Block.size);
		int size = (int) (this.size * Block.size);
		int rad = (int) Math.round(0.08 * Block.size);
		if (rad == 0)
			rad = 1;
		double offset = posx % rad;
		for (double x = -size / 2 + offset; x < size + offset; x += rad) {
			double diff = Math.sin(Math.acos(x / (size))) * (size);
			for (double y = (int) (-diff); y < diff; y += rad) {
				int drawx = (int) (posx + x) / rad * rad;
				int drawy = (int) (posy + y) / rad * rad;
				g.fillRect((int) ((drawx - rad / 2) - cameraX + Panel.windowWidth / 2),
						(int) ((drawy - rad / 2) - cameraY + Panel.windowHeight / 2), rad, rad);
			}
		}
		if (delete) {
			Random r = new Random();
			Color c = Color.red;
			for (int i = 0; i < 4 * size; i++) {
				Particle p = new Particle(posx + r.nextDouble() * size - size / 2,
						posy + r.nextDouble() * size - size / 2,
						r.nextDouble() * 0.1 * Block.size - 0.05 , -(r.nextDouble() * 0.1 ), 0,
						0, c);
				p.setLifetime(r.nextInt(10) + 3);
				Panel.addParticle(p);
			}
		}
		super.draw(g, cameraX, cameraY);
	}
}
