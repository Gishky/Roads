package Crafting;

import GameObjects.World;
import GameObjects.Blocks.*;

public class CraftingRelay implements CraftingRecipe {

	@Override
	public boolean checkCrafting(int x, int y) {
		if ((World.getBlock(x, y) instanceof BlockGoldChunk && World.getBlock(x + 1, y) instanceof BlockWood
				&& World.getBlock(x, y - 1) instanceof BlockWood
				&& World.getBlock(x + 1, y - 1) instanceof BlockGoldChunk)
				|| (World.getBlock(x, y) instanceof BlockWood && World.getBlock(x + 1, y) instanceof BlockGoldChunk
						&& World.getBlock(x, y - 1) instanceof BlockGoldChunk
						&& World.getBlock(x + 1, y - 1) instanceof BlockWood)) {

			World.setBlock(x, y, new BlockRelay());
			World.setBlock(x + 1, y, new BlockRelay());
			World.setBlock(x, y - 1, new BlockRelay());
			World.setBlock(x + 1, y - 1, new BlockRelay());
			return true;
		}
		return false;
	}

}
