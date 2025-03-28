package GameObjects.Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class OvenAbilityJet extends Entity {

	private double velx;
	private double vely;
	private int lifetime;

	public OvenAbilityJet(JSONObject entity) {
		super(entity.get("id"), entity.get("x"), entity.get("y"));
		for (Entity e : Panel.getEntities()) {
			if (e.getId() == (int) pos.getX()) {
				pos = e.getPos();
				break;
			}
		}
		velx = Double.parseDouble(entity.get("velx"));
		vely = Double.parseDouble(entity.get("vely"));
		lifetime = Integer.parseInt(entity.get("lifetime"));
		lastx = pos.getX();
		lasty = pos.getY();
	}

	public void setDelete(boolean delete) {

	}

	private double lastx, lasty;

	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (lifetime < 0)
			delete = true;
		lifetime--;

		int steps = (int) (2 * Math.pow(lifetime, 3));
		double stepx = lastx - pos.getX();
		stepx /= steps;
		double stepy = lasty - pos.getY();
		stepy /= steps;
		Random r = new Random();
		for (int i = 0; i <= steps + 1; i++) {
			Panel.addParticle(
					new Particle(pos.getX() + stepx * i + r.nextDouble() * 0.2 * Block.size - 0.1 * Block.size,
							pos.getY() + stepy * i + r.nextDouble() * 0.2 * Block.size - 0.1 * Block.size,
							-velx * 0.05 * Block.size + (r.nextDouble() * 0.03 - 0.015) * Block.size * lifetime,
							-vely * 0.05 * Block.size + (r.nextDouble() * 0.03 - 0.015) * Block.size * lifetime,
							velx * 0.00015 * Block.size, 0.00015 * Block.size, Color.red, 10));
		}
		lastx = pos.getX();
		lasty = pos.getY();
		super.draw(g, cameraX, cameraY);
	}
}