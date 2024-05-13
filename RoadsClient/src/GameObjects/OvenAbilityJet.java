package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import HelperObjects.Particle;
import Window.Panel;

public class OvenAbilityJet extends Entity {

	public OvenAbilityJet(String id, String x, String y, String hppercent, String heldBlock) {
		super(id, x, y, hppercent, heldBlock);
	}

	private int lastx, lasty;

	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (this.parameters == null)
			return;
		int stepx = lastx - pos.getX();
		stepx /= 5;
		int stepy = lasty - pos.getY();
		stepy /= 5;
		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			Panel.addParticle(new Particle(pos.getX() + stepx * i + r.nextDouble(),
					pos.getY() + stepy * i + r.nextDouble(),
					Double.parseDouble(parameters.split("/")[0]) / 5 + r.nextDouble() * 2,
					Double.parseDouble(parameters.split("/")[1]) / 5 + r.nextDouble() * 2, 0, 0, Color.red, 10));
		}
		lastx = pos.getX();
		lasty = pos.getY();

		super.draw(g, cameraX, cameraY);
	}
}