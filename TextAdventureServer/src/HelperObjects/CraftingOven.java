package HelperObjects;

import GameObjects.Block;
import GameObjects.BlockOven;
import GameObjects.World;

public class CraftingOven implements CraftingRecipe {

	@Override
	public Block checkCrafting(int x, int y) {
		if (World.getBlock(x, y).getId() == 3 && World.getBlock(x + 1, y).getId() == 3
				&& World.getBlock(x, y + 1).getId() == 3 && World.getBlock(x + 1, y + 1).getId() == 3) {
			return new BlockOven();
		}
		return null;
	}

}
