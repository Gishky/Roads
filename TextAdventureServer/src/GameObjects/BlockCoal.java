package GameObjects;

import HelperObjects.Position;

public class BlockCoal extends Block {
	public BlockCoal() {
		id = 5;
		friction = 2;

		breakable = true;
		breakThreshhold = 25;
	}

	public Block clone() {
		return new BlockCoal();
	}

	public void activateAbility(Entity e) {

	}

	public int getAbilityCooldown() {
		return 20;
	}
}
