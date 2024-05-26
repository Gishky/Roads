package Crafting;

import GameObjects.World;
import GameObjects.Blocks.*;

public class CraftingActivator implements CraftingRecipe {

	@Override
	public boolean checkCrafting(int x, int y) {
		if ((World.getBlock(x, y - 1) instanceof BlockGold && World.getBlock(x + 1, y - 1) instanceof BlockWood
				&& World.getBlock(x, y) instanceof BlockIron && World.getBlock(x + 1, y) instanceof BlockGold)
				|| (World.getBlock(x, y - 1) instanceof BlockWood && World.getBlock(x + 1, y - 1) instanceof BlockGold
						&& World.getBlock(x, y) instanceof BlockGold
						&& World.getBlock(x + 1, y) instanceof BlockIron)) {

			World.setBlock(x, y, new BlockActivator());
			World.setBlock(x + 1, y, new BlockAir());
			World.setBlock(x, y - 1, new BlockAir());
			World.setBlock(x + 1, y - 1, new BlockAir());
			return true;
		}
		return false;
	}

}
