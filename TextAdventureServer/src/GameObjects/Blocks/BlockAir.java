package GameObjects.Blocks;

import HelperObjects.JSONObject;

public class BlockAir extends Block {
	public BlockAir() {
		id = 0;
		setBlocksMovement(false);
		friction = 1;

		breakable = false;
	}

	public BlockAir(JSONObject block) {
		id = 0;
		setBlocksMovement(false);
		friction = 1;
		
		if(block==null)
			return;
		
		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
	}

	public Block clone() {
		return new BlockAir();
	}
}
