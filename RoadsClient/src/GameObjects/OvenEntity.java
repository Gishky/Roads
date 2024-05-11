package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import HelperObjects.Particle;
import Window.Panel;

public class OvenEntity extends Entity {

	public OvenEntity(String id, String x, String y, String hppercent, String heldBlock) {
		super(id, x, y, hppercent, heldBlock);
	}

	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int x = pos.getX() * Block.size - cameraX + Panel.windowWidth / 2;
		int y = pos.getY() * Block.size - cameraY + Panel.windowHeight / 2;

		g.setColor(Color.red);
		g.fillRect(x + Block.size / 4, y + Block.size / 4, Block.size / 2, Block.size / 5);
		Random r = new Random();
		Panel.addParticle(new Particle(pos.getX() * Block.size + Block.size / 4 + r.nextDouble() * Block.size / 2,
				pos.getY() * Block.size + Block.size / 4 + r.nextDouble() * Block.size / 5, 0, 0,
				r.nextDouble() * 0.2 - 0.05, -r.nextDouble() * 0.5, Color.red));

		super.draw(g, cameraX, cameraY);
	}
}