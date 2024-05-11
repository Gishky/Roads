package GameObjects;

import HelperObjects.Position;

public class BlockDirt extends Block {
	public BlockDirt() {
		id = 1;
		friction = 2;

		breakable = true;
		breakThreshhold = 25;
	}

	public Block clone() {
		return new BlockDirt();
	}

	public void activateAbility(Entity e) {
		double[] fireboltVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
		double velocityLength = Math.sqrt(Math.pow(fireboltVelocity[0], 2) + Math.pow(fireboltVelocity[1], 2));
		double[] unitVelocity = { fireboltVelocity[0] / velocityLength, fireboltVelocity[1] / velocityLength };
		fireboltVelocity[0] = unitVelocity[0] * 20;
		fireboltVelocity[1] = unitVelocity[1] * 20;

		Position fireboltpos = new Position();
		fireboltpos.set(e.pos.getX() + unitVelocity[0] * (e.hitBox.getRadius() + 3),
				e.pos.getY() + unitVelocity[1] * (e.hitBox.getRadius() + 3));
		Firebolt bolt = new Firebolt(fireboltpos, fireboltVelocity, e.heldBlock.clone());
		bolt.setFallingAccelleration(0);
	}

	public int getAbilityCooldown() {
		return 20;
	}
}
