package GameObjects;

public class BlockIron extends Block {
	public BlockIron() {
		id = 7;
		friction = 2;

		breakable = true;
		breakThreshhold = 100;
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
