package GameObjects;

public class BlockAir extends Block{
	public BlockAir() {
		id = 0;
		blocksMovement = false;
		friction = 1;
	}
	

	public Block clone() {
		return new BlockAir();
	}
}
