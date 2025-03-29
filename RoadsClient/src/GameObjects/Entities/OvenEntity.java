package GameObjects.Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import Window.Panel;

public class OvenEntity extends Entity {

	private boolean burningL = false;
	private boolean burningR = false;
	private boolean smelting = false;

	private double progressL = 0;
	private double progressR = 0;

	public OvenEntity(JSONObject entity) {
		super(entity.get("id"), entity.get("x"), entity.get("y"));
		burningL = entity.get("burningL").equals("true");
		burningR = entity.get("burningR").equals("true");
		smelting = entity.get("smelting").equals("true");

	}

	@Override
	public void updateEntity(JSONObject entity) {
		burningL = entity.get("burningL").equals("true");
		burningR = entity.get("burningR").equals("true");
		smelting = entity.get("smelting").equals("true");
	}

	public void draw(Graphics2D g, int cameraX, int cameraY) {
		if (smelting) {
			double x = pos.getX() * Block.size - cameraX + Panel.windowWidth / 2;
			double y = pos.getY() * Block.size - cameraY + Panel.windowHeight / 2;

			g.setColor(Color.red);
			g.fillRect((int) x + Block.size / 4, (int) y + Block.size / 4, Block.size / 2, Block.size / 5);
			Random r = new Random();
			Panel.addParticle(
					new Particle(pos.getX() + 0.25 + r.nextDouble() / 2, pos.getY() + 0.25 + r.nextDouble() / 5, 0, 0,
							r.nextDouble() * 0.01 - 0.0025, -r.nextDouble() * 0.025, Color.red));
		}

		if (burningL) {
			progressL += 0.1;
			if (progressL >= 1)
				progressL = 0;
			Panel.addParticle(new Particle(pos.getX() - 1 + progressL, pos.getY(), 0, 0, 0, 0, Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX() - progressL, pos.getY() + 1, 0, 0, 0, 0, Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX(), pos.getY() + progressL, 0, 0, 0, 0, Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX() - 1, pos.getY() + 1 - progressL, 0, 0, 0, 0, Color.red, 10, 0.8));
		}

		if (burningR) {
			progressR += 0.1;
			if (progressR >= 1)
				progressR = 0;
			Panel.addParticle(new Particle(pos.getX() + 1 + progressR, pos.getY(), 0, 0, 0, 0, Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX() + 2 - progressR, pos.getY() + 1, 0, 0, 0, 0, Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX() + 2, pos.getY() + progressR, 0, 0, 0, 0, Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX() + 1, pos.getY() + 1 - progressR, 0, 0, 0, 0, Color.red, 10, 0.8));
		}

		super.draw(g, cameraX, cameraY);
	}
}
