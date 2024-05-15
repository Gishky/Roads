package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.Particle;
import Window.Panel;

public class OvenEntity extends Entity {

	public OvenEntity(String id, String x, String y, String hppercent, String heldBlock) {
		super(id, x, y, hppercent, heldBlock);
	}

	public void draw(Graphics2D g, int cameraX, int cameraY) {
		double x = pos.getX() - cameraX + Panel.windowWidth / 2;
		double y = pos.getY() - cameraY + Panel.windowHeight / 2;

		g.setColor(Color.red);
		g.fillRect((int) x + Block.size / 4, (int) y + Block.size / 4, Block.size / 2, Block.size / 5);
		Random r = new Random();
		Panel.addParticle(new Particle(pos.getX() + Block.size / 4 + r.nextDouble() * Block.size / 2,
				pos.getY() + Block.size / 4 + r.nextDouble() * Block.size / 5, 0, 0,
				r.nextDouble() * 0.01 * Block.size - 0.0025 * Block.size, -r.nextDouble() * 0.025 * Block.size,
				Color.red));

		super.draw(g, cameraX, cameraY);
	}
}
