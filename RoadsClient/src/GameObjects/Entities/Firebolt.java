package GameObjects.Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.World;
import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockGrass;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class Firebolt extends Entity {

	private Block colourBlock;

	public Firebolt(JSONObject entity) {
		super(entity.get("id"), entity.get("x"), entity.get("y"));
		colourBlock = Block.getBlockFromID(entity.get("colourBlock"), null);

		lastx = pos.getX();
		lasty = pos.getY();
		size = (Double.parseDouble(entity.get("size")));
	}

	public void updateEntity(JSONObject entity) {
		pos.set(entity.get("x"), entity.get("y"));
		colourBlock = Block.getBlockFromID(entity.get("colourBlock"), null);
		size = Double.parseDouble(entity.get("size"));
	}

	private double lastx, lasty;

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		double stepcount = size * size * 300;
		double stepx = lastx - pos.getX();
		stepx /= stepcount;
		double stepy = lasty - pos.getY();
		stepy /= stepcount;

		if (colourBlock == null)
			return;

		Random r = new Random();
		for (int i = 0; i < stepcount; i++) {
			Panel.addParticle(new Particle(pos.getX() + stepx * i + r.nextDouble() * size * 2 - size,
					pos.getY() + stepy * i + r.nextDouble() * size * 2 - size, 0, 0, r.nextDouble() * 0.025 - 0.015,
					-r.nextDouble() * 0.025, colourBlock.getColor()));
		}

		lastx = pos.getX();
		lasty = pos.getY();

		super.draw(g, cameraX, cameraY);
	}
}
