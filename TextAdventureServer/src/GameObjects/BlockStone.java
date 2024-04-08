package GameObjects;

public class BlockStone extends Block {
	public BlockStone() {
		id = 3;
		friction = 2;

		breakable = true;
		breakThreshhold = 100;
	}
}
