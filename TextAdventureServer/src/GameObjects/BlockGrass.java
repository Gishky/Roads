package GameObjects;

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
}
