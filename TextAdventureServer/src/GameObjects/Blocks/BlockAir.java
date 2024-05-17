package GameObjects.Blocks;

import HelperObjects.JSONObject;

public class BlockAir extends Block {
	public BlockAir() {
		id = 0;
		setBlocksMovement(false);
		friction = 1;
	}

	public Block clone() {
		return new BlockAir();
	}
}
