package GameObjects.Blocks;

import java.util.ArrayList;

import GameObjects.*;
import HelperObjects.*;

public class BlockRelay extends Block {
	public BlockRelay() {
		id = 12;
		friction = 2;

		breakable = true;
		breakThreshhold = 1;
	}

	public BlockRelay(JSONObject block) {
		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
		id = 12;
		friction = 2;

		breakable = true;
		breakThreshhold = 1;
	}

	public Block clone() {
		return new BlockRelay();
	}

	public void activate(ArrayList<Block> activationchain) {
		if(activationchain.contains(this))
			return;
		activationchain.add(this);
		
		World.getBlock(x - 1, y).activate(activationchain);
		World.getBlock(x + 1, y).activate(activationchain);
		World.getBlock(x, y - 1).activate(activationchain);
		World.getBlock(x, y + 1).activate(activationchain);
	}
}
