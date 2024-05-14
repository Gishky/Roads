package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.Particle;
import Window.Panel;

public class OvenAbilityJet extends Entity {

	public OvenAbilityJet(String id, String x, String y, String hppercent, String heldBlock) {
		super(id, x, y, hppercent, heldBlock);
		lastx = this.pos.getX() / Block.size;
		lasty = this.pos.getY() / Block.size;
	}

	private double lastx, lasty;

	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (this.parameters == null)
			return;
		double stepx = lastx - pos.getX() / Block.size;
		stepx /= 5;
		double stepy = lasty - pos.getY() / Block.size;
		stepy /= 5;
		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			Panel.addParticle(
					new Particle(pos.getX() / Block.size + stepx  * i + r.nextDouble() * 0.2 - 0.1,
							pos.getY() / Block.size + stepy * i + r.nextDouble() * 0.2 - 0.1,
							Double.parseDouble(parameters.split("/")[0]) * 0.15 + r.nextDouble() * 0.01 - 0.005,
							Double.parseDouble(parameters.split("/")[1]) * 0.15 + r.nextDouble() * 0.01 - 0.005, 0, 0,
							Color.red, 10));
		}
		lastx = pos.getX() / Block.size;
		lasty = pos.getY() / Block.size;

		super.draw(g, cameraX, cameraY);
	}
}