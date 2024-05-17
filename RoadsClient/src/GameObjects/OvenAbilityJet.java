package GameObjects;

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

	public OvenAbilityJet(String id, String x, String y, String hppercent, String heldBlock) {
		super(id, x, y, hppercent, heldBlock);
	}

	public OvenAbilityJet(JSONObject entity) {
		super(entity.get("id"), entity.get("x"), entity.get("y"), "100", "0");
		velx = Double.parseDouble(entity.get("velx"));
		vely = Double.parseDouble(entity.get("vely"));
	}
	
	public void updateEntity(JSONObject json) {
		pos.setX(json.get("x"));
		pos.setY(json.get("y"));
		velx = Double.parseDouble(json.get("velx"));
		vely = Double.parseDouble(json.get("vely"));
	}

	private double lastx, lasty;

	public void draw(Graphics2D g, int cameraX, int cameraY) {
		double stepx = lastx - pos.getX();
		stepx /= 5;
		double stepy = lasty - pos.getY();
		stepy /= 5;
		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			Panel.addParticle(
					new Particle(pos.getX() + stepx * i + r.nextDouble() * 0.2 * Block.size - 0.1 * Block.size,
							pos.getY() + stepy * i + r.nextDouble() * 0.2 * Block.size - 0.1 * Block.size,
							velx * 0.15 * Block.size + r.nextDouble() * 0.01 * Block.size - 0.005 * Block.size,
							vely * 0.15 * Block.size + r.nextDouble() * 0.01 * Block.size - 0.005 * Block.size, 0, 0,
							Color.red, 10));
		}
		lastx = pos.getX();
		lasty = pos.getY();

		super.draw(g, cameraX, cameraY);
	}
}