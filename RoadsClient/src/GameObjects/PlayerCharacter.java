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
		if (id == World.playerid) {
			cameraX += World.cameraX*Block.size - cameraX;
			cameraY += World.cameraY*Block.size - cameraY;
		}

		g.setColor(Color.blue.brighter().brighter());
		if (heldBlock != null) {
			g.setColor(heldBlock.getColor().darker());
		}

		g.fillOval((int) pos.getX() - 5 - cameraX + Panel.windowWidth / 2,
				(int) pos.getY() - 5 - cameraY + Panel.windowHeight / 2, 10, 10);

		if (breakCount != 0) {
			int blockx = (int) pos.getX() / Block.size;
			int blocky = (int) pos.getY() / Block.size + 1;
			Color c = World.getWorld()[blockx][blocky].getC();

			Random r = new Random();
			for (int i = 0; i < 3; i++) {
				Panel.addParticle(new Particle(blockx + r.nextDouble(), blocky + r.nextDouble(), 0,
						-r.nextDouble() * 0.25 - 0.15, r.nextDouble() * 0.025 - 0.015, r.nextDouble()*0.15 + 0.025, c.brighter()));
			}
		}

		super.draw(g, cameraX, cameraY);
	}

}
