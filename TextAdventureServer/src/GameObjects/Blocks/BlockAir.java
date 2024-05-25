package GameObjects.Blocks;

import HelperObjects.JSONObject;

public class BlockAir extends Block {
	public BlockAir() {
		id = 0;
		setBlocksMovement(false);
		friction = 1;
	}
	
	public BlockAir(JSONObject block) {
		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
		id = 0;
		setBlocksMovement(false);
		friction = 1;
	}

	public Block clone() {
		return new BlockAir();
	}
}
