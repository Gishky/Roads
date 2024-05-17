package GameObjects;

import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockCoalOre;
import GameObjects.Blocks.BlockDirt;
import GameObjects.Blocks.BlockGrass;
import GameObjects.Blocks.BlockIronOre;
import GameObjects.Blocks.BlockStone;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class Firebolt extends Entity {

	public Firebolt(String id, String x, String y, String hppercent, String heldBlock) {
		super(id, x, y, hppercent, heldBlock);
	}

	public Firebolt(JSONObject entity) {
		super(entity.get("id"), entity.get("x"), entity.get("y"), "" + 100,
				new JSONObject(entity.get("heldBlock")).get("id"));
	}

	public void updateEntity(JSONObject json) {
		pos.setX(json.get("x"));
		pos.setY(json.get("y"));
		heldBlock=Block.getBlockFromID(new JSONObject(json.get("heldBlock")).get("id"));
	}

	private double lastx, lasty;

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		// System.out.println(id+":
		// "+((pos.getX()-World.getWorld().length*Block.size/2)/Block.size)+"/"+(pos.getY()/Block.size));

		//System.out.println(id+ ": "+pos.getX()+"/"+pos.getY()+" ("+heldBlock.getColor()+")");
		double stepx = lastx - pos.getX();
		stepx /= 5;
		double stepy = lasty - pos.getY();
		stepy /= 5;

		if (heldBlock == null)
			return;

		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			if (heldBlock instanceof BlockDirt || heldBlock instanceof BlockStone || heldBlock instanceof BlockIronOre
					|| heldBlock instanceof BlockCoalOre)
				Panel.addParticle(
						new Particle(pos.getX() + stepx * i + r.nextDouble() * 0.2 * Block.size - 0.1 * Block.size,
								pos.getY() + stepy * i + r.nextDouble() * 0.2 * Block.size - 0.1 * Block.size, 0, 0,
								r.nextDouble() * 0.025 * Block.size - 0.015 * Block.size,
								-r.nextDouble() * 0.025 * Block.size, heldBlock.getColor()));
			else if (heldBlock instanceof BlockGrass)
				Panel.addParticle(new Particle(pos.getX() + stepx * i, pos.getY() + stepy * i,
						r.nextDouble() * 0.1 * Block.size - 0.05 * Block.size, -0.05 * Block.size * r.nextDouble(), 0,
						r.nextDouble() * 0.005 * Block.size, heldBlock.getColor()));
		}

		lastx = pos.getX();
		lasty = pos.getY();

		super.draw(g, cameraX, cameraY);
	}
}
