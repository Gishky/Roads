package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.Particle;
import Window.Panel;

public class PlayerCharacter extends Entity {

	public PlayerCharacter(String id, String x, String y, String hppercent, String heldblock) {
		super(id, x, y, hppercent, heldblock);
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		g.setColor(Color.black);
		if (heldBlock != null) {
			g.setColor(heldBlock.getColor().darker());
		}
		g.fillOval(pos.getX() - 5 - cameraX + Panel.windowWidth / 2, pos.getY() - 5 - cameraY + Panel.windowHeight / 2,
				10, 10);

		if (breakCount != 0) {
			int blockx = (int) pos.getX() / Block.size;
			int blocky = (int) pos.getY() / Block.size + 1;
			Color c = World.getWorld()[blockx][blocky].getC();

			Random r = new Random();
			for (int i = 0; i < 3; i++) {
				Panel.addParticle(new Particle(blockx * Block.size + r.nextDouble() * Block.size,
						blocky * Block.size + r.nextDouble() * Block.size, 0, -r.nextDouble() * 5 - 3,
						r.nextDouble() * 0.5 - 0.3, r.nextDouble() * 1 + 0.5, c.brighter()));
			}
		}

		super.draw(g, cameraX, cameraY);
	}

}
