package Crafting;

import GameObjects.World;
import GameObjects.Blocks.*;

public class CraftingGoldChunk implements CraftingRecipe {

	@Override
	public boolean checkCrafting(int x, int y) {
		if ((World.getBlock(x, y) instanceof BlockGold && World.getBlock(x + 1, y) instanceof BlockAir
				&& World.getBlock(x, y - 1) instanceof BlockAir && World.getBlock(x + 1, y - 1) instanceof BlockAir)) {

			World.setBlock(x, y, new BlockGoldChunk());
			World.setBlock(x + 1, y, new BlockGoldChunk());
			World.setBlock(x, y - 1, new BlockGoldChunk());
			World.setBlock(x + 1, y - 1, new BlockGoldChunk());
			return true;
		}
		return false;
	}

}
