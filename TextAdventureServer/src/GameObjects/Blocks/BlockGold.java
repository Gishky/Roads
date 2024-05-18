package GameObjects.Blocks;

import GameObjects.Entity;

public class BlockGold extends Block {
	public BlockGold() {
		id = 9;
		friction = 2;

		breakable = true;
		breakThreshhold = 100;
	}

	public Block clone() {
		return new BlockGold();
	}

	public void activateAbility(Entity e) {

	}

	public int getAbilityCooldown() {
		return 20;
	}
}
