package GameObjects;

import HelperObjects.Position;

public class BlockGrass extends Block {
	public BlockGrass() {
		id = 2;
		friction = 1.5;

		breakable = true;
		breakThreshhold = 25;
	}

	public Block clone() {
		return new BlockGrass();
	}

	public void activateAbility(Entity e) {
		boolean goLeft = e.velocity[0] < 0;

		new GrassCrawler(new Position(e.getPos().getX() + (e.getHitBox().getRadius() + 10) * (goLeft ? -1 : 1),
				e.getPos().getY()), goLeft);
	}

	public int getAbilityCooldown() {
		return 30;
	}

}
