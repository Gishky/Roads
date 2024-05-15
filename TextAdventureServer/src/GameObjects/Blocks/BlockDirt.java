package GameObjects.Blocks;

import GameObjects.Entity;
import GameObjects.Firebolt;
import HelperObjects.Position;

public class BlockDirt extends Block {
	public BlockDirt() {
		id = 1;
		friction = 2;

		breakable = true;
		breakThreshhold = 5;
	}

	public Block clone() {
		return new BlockDirt();
	}

	public void activateAbility(Entity e) {
		double[] fireboltVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
		double velocityLength = Math.sqrt(Math.pow(fireboltVelocity[0], 2) + Math.pow(fireboltVelocity[1], 2));
		double[] unitVelocity = { fireboltVelocity[0] / velocityLength, fireboltVelocity[1] / velocityLength };
		fireboltVelocity[0] = unitVelocity[0];
		fireboltVelocity[1] = unitVelocity[1];

		Position fireboltpos = new Position();
		fireboltpos.set(e.getPos().getX(), e.getPos().getY());
		Firebolt bolt = new Firebolt(fireboltpos, fireboltVelocity, e.getHeldBlock().clone(),e);
		bolt.setFallingAccelleration(0);
	}

	public int getAbilityCooldown() {
		return 15;
	}
}
