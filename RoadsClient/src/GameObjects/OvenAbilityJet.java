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
	}

	private double lastx, lasty;

	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (this.parameters == null)
			return;
		double stepx = lastx - pos.getX();
		stepx /= 5;
		double stepy = lasty - pos.getY();
		stepy /= 5;
		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			Panel.addParticle(
					new Particle(pos.getX() + stepx * i + r.nextDouble() * 0.2 * Block.size - 0.1 * Block.size,
							pos.getY() + stepy * i + r.nextDouble() * 0.2 * Block.size - 0.1 * Block.size,
							Double.parseDouble(parameters.split("/")[0]) * 0.15 * Block.size
									+ r.nextDouble() * 0.01 * Block.size - 0.005 * Block.size,
							Double.parseDouble(parameters.split("/")[1]) * 0.15 * Block.size
									+ r.nextDouble() * 0.01 * Block.size - 0.005 * Block.size,
							0, 0, Color.red, 10));
		}
		lastx = pos.getX();
		lasty = pos.getY();

		super.draw(g, cameraX, cameraY);
	}
}