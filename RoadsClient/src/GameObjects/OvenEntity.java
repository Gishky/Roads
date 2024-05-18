package GameObjects;

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

	private int stateL = 0;
	private int stateR = 0;
	private double leftX = 0;
	private double rightX = 0;
	private double leftY = 0;
	private double rightY = 0;

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
			double x = pos.getX() - cameraX + Panel.windowWidth / 2;
			double y = pos.getY() - cameraY + Panel.windowHeight / 2;

			g.setColor(Color.red);
			g.fillRect((int) x + Block.size / 4, (int) y + Block.size / 4, Block.size / 2, Block.size / 5);
			Random r = new Random();
			Panel.addParticle(new Particle(pos.getX() + Block.size / 4 + r.nextDouble() * Block.size / 2,
					pos.getY() + Block.size / 4 + r.nextDouble() * Block.size / 5, 0, 0,
					r.nextDouble() * 0.01 * Block.size - 0.0025 * Block.size, -r.nextDouble() * 0.025 * Block.size,
					Color.red));
		}

		if (burningL) {
			switch (stateL) {
			case 0:
				leftX += (double) Block.size / 10;
				if (leftX >= Block.size)
					stateL = 1;
				break;
			case 1:
				leftY += (double) Block.size / 10;
				if (leftY >= Block.size)
					stateL = 2;
				break;
			case 2:
				leftX -= (double) Block.size / 10;
				if (leftX <= 0)
					stateL = 3;
				break;
			case 3:
				leftY -= (double) Block.size / 10;
				if (leftY <= 0)
					stateL = 0;
				break;
			}
			Panel.addParticle(
					new Particle(pos.getX() - Block.size + leftX, pos.getY() + leftY, 0, 0, 0, 0, Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX() - Block.size - leftX + Block.size,
					pos.getY() - leftY + Block.size, 0, 0, 0, 0, Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX() - Block.size - leftY + Block.size, pos.getY() + leftX, 0, 0, 0, 0,
					Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX() - Block.size + leftY, pos.getY() - leftX + Block.size, 0, 0, 0, 0,
					Color.red, 10, 0.8));
		}

		if (burningR) {
			switch (stateR) {
			case 0:
				rightX += (double) Block.size / 10;
				if (rightX >= Block.size)
					stateR = 1;
				break;
			case 1:
				rightY += (double) Block.size / 10;
				if (rightY >= Block.size)
					stateR = 2;
				break;
			case 2:
				rightX -= (double) Block.size / 10;
				if (rightX <= 0)
					stateR = 3;
				break;
			case 3:
				rightY -= (double) Block.size / 10;
				if (rightY <= 0)
					stateR = 0;
				break;
			}
			Panel.addParticle(new Particle(pos.getX() + Block.size + rightX, pos.getY() + rightY, 0, 0, 0, 0, Color.red,
					10, 0.8));
			Panel.addParticle(new Particle(pos.getX() + Block.size - rightX + Block.size,
					pos.getY() - rightY + Block.size, 0, 0, 0, 0, Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX() + Block.size - rightY + Block.size, pos.getY() + rightX, 0, 0, 0,
					0, Color.red, 10, 0.8));
			Panel.addParticle(new Particle(pos.getX() + Block.size + rightY, pos.getY() - rightX + Block.size, 0, 0, 0,
					0, Color.red, 10, 0.8));
		}

		super.draw(g, cameraX, cameraY);
	}
}
