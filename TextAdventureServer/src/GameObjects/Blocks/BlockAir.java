package GameObjects.Blocks;

import HelperObjects.JSONObject;

public class BlockAir extends Block {
	public BlockAir() {
		id = 0;
		setBlocksMovement(false);
		friction = 1;
	}
	
	public BlockAir(JSONObject block) {
		setX(Integer.parseInt(block.get("x")));
		setY(Integer.parseInt(block.get("y")));
		id = 0;
		setBlocksMovement(false);
		friction = 1;
	}

	public Block clone() {
		return new BlockAir();
	}
}
