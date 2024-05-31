package GameObjects.Blocks;

import GameObjects.World;
import HelperObjects.JSONObject;

public class BlockLeaf extends Block {
	public BlockLeaf() {
		id = 11;
		setBlocksMovement(false);
		friction = 1;

		breakable = true;
		breakThreshhold = 1;
		fuelValue = 10;
	}

	public BlockLeaf(JSONObject block) {
		id = 11;
		setBlocksMovement(false);
		friction = 1;

		if (block == null)
			return;

		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
	}

	public void update() {
		if (!(World.getBlock(x - 1, y) instanceof BlockWood)) {
			if (!(World.getBlock(x + 1, y) instanceof BlockWood)) {
				if (!(World.getBlock(x, y - 1) instanceof BlockWood)) {
					if (!(World.getBlock(x, y + 1) instanceof BlockWood)) {
						World.setBlock(x, y, new BlockAir());
					}
				}
			}
		}
	}

	public Block clone() {
		return new BlockLeaf();
	}
}
