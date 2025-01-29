package GameObjects;

import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockGrass;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class Firebolt extends Entity {

	private Block colourBlock;
	private int size;

	public Firebolt(JSONObject entity) {
		super(entity.get("id"), entity.get("x"), entity.get("y"));
		colourBlock = Block.getBlockFromID(entity.get("colourBlock"), null);

		lastx = pos.getX();
		lasty = pos.getY();
		size = (int) (Double.parseDouble(entity.get("size")) * Block.size) * 2;
	}

	public void updateEntity(JSONObject entity) {
		pos.set(entity.get("x"),entity.get("y"));
		colourBlock = Block.getBlockFromID(entity.get("colourBlock"), null);
		size = (int) (Double.parseDouble(entity.get("size")) * Block.size) * 2;
	}

	private double lastx, lasty;

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		// System.out.println(id+":
		// "+((pos.getX()-World.getWorld().length*Block.size/2)/Block.size)+"/"+(pos.getY()/Block.size));

		// System.out.println(id+ ": "+pos.getX()+"/"+pos.getY()+"
		// ("+heldBlock.getColor()+")");
		double stepx = lastx - pos.getX();
		stepx /= 5;
		double stepy = lasty - pos.getY();
		stepy /= 5;

		if (colourBlock == null)
			return;

		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			if (!(colourBlock instanceof BlockGrass))
				Panel.addParticle(
						new Particle(pos.getX() + stepx * i + r.nextDouble() * 0.2 * Block.size - 0.1 * Block.size,
								pos.getY() + stepy * i + r.nextDouble() * 0.2 * Block.size - 0.1 * Block.size, 0, 0,
								r.nextDouble() * 0.025 * Block.size - 0.015 * Block.size,
								-r.nextDouble() * 0.025 * Block.size, colourBlock.getColor()));
			else
				Panel.addParticle(new Particle(pos.getX() + stepx * i, pos.getY() + stepy * i,
						r.nextDouble() * 0.1 * Block.size - 0.05 * Block.size, -0.05 * Block.size * r.nextDouble(), 0,
						r.nextDouble() * 0.005 * Block.size, colourBlock.getColor()));
		}

		lastx = pos.getX();
		lasty = pos.getY();

		g.setColor(colourBlock.getColor());
		g.fillOval((int) pos.getX() - size / 2 - cameraX + Panel.windowWidth / 2,
				(int) pos.getY() - size / 2 - cameraY + Panel.windowHeight / 2, size, size);

		super.draw(g, cameraX, cameraY);
	}
}
