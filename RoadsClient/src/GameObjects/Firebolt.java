package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import HelperObjects.Particle;
import Window.Panel;

public class Firebolt extends Entity {

	public Firebolt(String id, String x, String y) {
		super(id, x, y);
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (removed)
			return;

		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			Panel.addParticle(new Particle(pos.getX() + r.nextDouble() * 5 - 3, pos.getY() + r.nextDouble() * 5 - 3, 0,
					0, r.nextDouble() * 0.5 - 0.3, -r.nextDouble() * 0.5, Color.red));
		}
	}
}