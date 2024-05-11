package GameObjects;

import java.awt.Graphics2D;
import java.util.Random;

import HelperObjects.Particle;
import Window.Panel;

public class Firebolt extends Entity {

	public Firebolt(String id, String x, String y, String hppercent, String heldBlock) {
		super(id, x, y, hppercent, heldBlock);
	}

	private int lastx, lasty;

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		// System.out.println(id+":
		// "+((pos.getX()-World.getWorld().length*Block.size/2)/Block.size)+"/"+(pos.getY()/Block.size));

		int stepx = lastx - pos.getX();
		stepx /= 5;
		int stepy = lasty - pos.getY();
		stepy /= 5;

		if (heldBlock == null)
			return;

		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			if (heldBlock instanceof BlockDirt || heldBlock instanceof BlockStone)
				Panel.addParticle(new Particle(pos.getX() + r.nextDouble() * 4 - 2 + stepx * i,
						pos.getY() + r.nextDouble() * 4 - 2 + stepy * i, 0, 0, r.nextDouble() * 0.5 - 0.3,
						-r.nextDouble() * 0.5, heldBlock.getColor()));
			else if (heldBlock instanceof BlockGrass)
				Panel.addParticle(new Particle(pos.getX() + stepx * i, pos.getY() + stepy * i, r.nextDouble() * 2 - 1,
						-1 * r.nextDouble(), 0, r.nextDouble() * 0.1, heldBlock.getColor()));
		}

		lastx = pos.getX();
		lasty = pos.getY();

		super.draw(g, cameraX, cameraY);
	}
}
