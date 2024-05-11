package GameObjects;

import HelperObjects.Position;

public class BlockOven extends Block {

	public BlockOven() {
		id = 4;
		friction = 3;

		breakable = true;
		breakThreshhold = 40;
	}

	public Block clone() {
		return new BlockOven();
	}

	public void activateAbility(Entity e) {

	}

	public int getAbilityCooldown() {
		return 30;
	}

	public void update() {
		System.out.println("update");
	}
}
