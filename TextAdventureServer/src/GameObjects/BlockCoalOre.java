package GameObjects;

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

	}

	public int getAbilityCooldown() {
		return 20;
	}
}
