package Crafting;

import GameObjects.World;
import GameObjects.Blocks.*;

public class CraftingIronChunk implements CraftingRecipe {

	@Override
	public boolean checkCrafting(int x, int y) {
		if ((World.getBlock(x, y) instanceof BlockIron && World.getBlock(x + 1, y) instanceof BlockAir
				&& World.getBlock(x, y - 1) instanceof BlockAir && World.getBlock(x + 1, y - 1) instanceof BlockAir)) {

			World.setBlock(x, y, new BlockIronChunk());
			World.setBlock(x + 1, y, new BlockIronChunk());
			World.setBlock(x, y - 1, new BlockIronChunk());
			World.setBlock(x + 1, y - 1, new BlockIronChunk());
			return true;
		}
		return false;
	}

}
