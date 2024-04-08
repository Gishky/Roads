package GameObjects;

public class BlockDirt extends Block {
	public BlockDirt() {
		id = 1;
		friction = 2;
		
		breakable = true;
		breakThreshhold = 25;
	}
}
