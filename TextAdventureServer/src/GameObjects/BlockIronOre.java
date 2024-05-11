package GameObjects;

public class BlockIronOre extends Block {
	public BlockIronOre() {
		id = 6;
		friction = 2;

		breakable = true;
		breakThreshhold = 20;

		smeltedBlock = new BlockIron();
		requiredFuelForSmelting = 400;
	}

	public Block clone() {
		return new BlockIronOre();
	}

	public void activateAbility(Entity e) {

	}

	public int getAbilityCooldown() {
		return 20;
	}
}
