package GameObjects.Blocks;

import GameObjects.Entity;
import GameObjects.Firebolt;
import HelperObjects.Position;

public class BlockCoalOre extends Block {
	public BlockCoalOre() {
		id = 5;
		friction = 2;

		breakable = true;
		breakThreshhold = 20;

		fuelValue = 200;
	}

	public Block clone() {
		return new BlockCoalOre();
	}

	public void activateAbility(Entity e) {
		for (int i = 0; i < 3; i++) {
			double[] fireboltVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
			double velocityLength = Math.sqrt(Math.pow(fireboltVelocity[0], 2) + Math.pow(fireboltVelocity[1], 2));
			double angle = Math.atan(fireboltVelocity[1] / fireboltVelocity[0]);
			if (fireboltVelocity[0] < 0) {
				angle += Math.PI;
			}
			angle = angle - (double) i / 4 + 0.25;
			fireboltVelocity[0] = Math.cos(angle) * velocityLength;
			fireboltVelocity[1] = Math.sin(angle) * velocityLength;
			double[] unitVelocity = { fireboltVelocity[0] / velocityLength, fireboltVelocity[1] / velocityLength };
			fireboltVelocity[0] = unitVelocity[0];
			fireboltVelocity[1] = unitVelocity[1];

			Position fireboltpos = new Position();
			fireboltpos.set(e.getPos().getX() + unitVelocity[0] * (e.getHitBox().getRadius() + 0.15),
					e.getPos().getY() + unitVelocity[1] * (e.getHitBox().getRadius() + 0.15));
			new Firebolt(fireboltpos, fireboltVelocity, e.getHeldBlock().clone());
		}
	}

	public int getAbilityCooldown() {
		return 20;
	}
}
