package Crafting;

import GameObjects.World;
import GameObjects.Blocks.*;

public class CraftingMachine implements CraftingRecipe {

	@Override
	public boolean checkCrafting(int x, int y) {
		if ((World.getBlock(x, y - 1) instanceof BlockRelay && World.getBlock(x + 1, y - 1) instanceof BlockIron
				&& World.getBlock(x, y) instanceof BlockWood && World.getBlock(x + 1, y) instanceof BlockWood)
				|| (World.getBlock(x, y - 1) instanceof BlockIron && World.getBlock(x + 1, y - 1) instanceof BlockRelay
						&& World.getBlock(x, y) instanceof BlockWood
						&& World.getBlock(x + 1, y) instanceof BlockWood)) {

			World.setBlock(x, y, new BlockMachine());
			World.setBlock(x + 1, y, new BlockAir());
			World.setBlock(x, y - 1, new BlockAir());
			World.setBlock(x + 1, y - 1, new BlockAir());
			return true;
		}
		return false;
	}

}
