package GameObjects.Blocks;

import GameObjects.Entity;
import GameObjects.GrassCrawler;
import HelperObjects.Position;

public class BlockGrass extends Block {
	public BlockGrass() {
		id = 2;
		friction = 1.5;

		breakable = true;
		breakThreshhold = 5;
	}

	public Block clone() {
		return new BlockGrass();
	}

	public void activateAbility(Entity e) {
		double[] initialVelocity = { e.getMousePosition().getX() ,
				e.getMousePosition().getY()  };
		double velocityLength = Math.sqrt(Math.pow(initialVelocity[0], 2) + Math.pow(initialVelocity[1], 2));
		double[] unitVelocity = { initialVelocity[0] / velocityLength, initialVelocity[1] / velocityLength };
		initialVelocity[0] = unitVelocity[0] * 20;
		initialVelocity[1] = unitVelocity[1] * 20;

		Position initialPos = new Position();
		initialPos.set(e.getPos().getX() + unitVelocity[0] * (e.getHitBox().getRadius() + 10),
				e.getPos().getY() + unitVelocity[1] * (e.getHitBox().getRadius() + 10));
		new GrassCrawler(initialPos, initialVelocity);
	}

	public int getAbilityCooldown() {
		return 30;
	}

}
