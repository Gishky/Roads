package GameObjects;

import HelperObjects.Position;

public class BlockStone extends Block {
	public BlockStone() {
		id = 3;
		friction = 2;

		breakable = true;
		breakThreshhold = 100;
	}

	public Block clone() {
		return new BlockStone();
	}

	public void activateAbility(Entity e) {
		for (int i = 0; i < 5; i++) {
			double[] fireboltVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
			double velocityLength = Math.sqrt(Math.pow(fireboltVelocity[0], 2) + Math.pow(fireboltVelocity[1], 2));
			double angle = Math.atan(fireboltVelocity[1] / fireboltVelocity[0]);
			if (fireboltVelocity[0] < 0) {
				angle += Math.PI;
			}
			angle = angle - (double) i / 6 + 0.5;
			fireboltVelocity[0] = Math.cos(angle) * velocityLength;
			fireboltVelocity[1] = Math.sin(angle) * velocityLength;
			double[] unitVelocity = { fireboltVelocity[0] / velocityLength, fireboltVelocity[1] / velocityLength };
			fireboltVelocity[0] = unitVelocity[0] * 20;
			fireboltVelocity[1] = unitVelocity[1] * 20;

			Position fireboltpos = new Position();
			fireboltpos.set(e.pos.getX() + unitVelocity[0] * (e.hitBox.getRadius() + 3),
					e.pos.getY() + unitVelocity[1] * (e.hitBox.getRadius() + 3));
			new Firebolt(fireboltpos, fireboltVelocity, e.heldBlock.clone());
		}
	}

	public int getAbilityCooldown() {
		return 25;
	}
}
