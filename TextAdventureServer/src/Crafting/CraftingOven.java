package Crafting;

import GameObjects.World;
import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import GameObjects.Blocks.BlockOven;

public class CraftingOven implements CraftingRecipe {

	@Override
	public boolean checkCrafting(int x, int y) {
		if (World.getBlock(x, y).getId() == 3 && World.getBlock(x + 1, y).getId() == 3
				&& World.getBlock(x, y - 1).getId() == 3 && World.getBlock(x + 1, y - 1).getId() == 3) {

			World.setBlock(x, y, new BlockOven());
			World.setBlock(x + 1, y, new BlockAir());
			World.setBlock(x, y - 1, new BlockAir());
			World.setBlock(x + 1, y - 1, new BlockAir());
			return true;
		}
		return false;
	}

}
