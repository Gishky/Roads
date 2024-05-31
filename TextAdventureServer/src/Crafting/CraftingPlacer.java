package Crafting;

import GameObjects.World;
import GameObjects.Blocks.BlockAir;
import GameObjects.Blocks.BlockMachine;
import GameObjects.Blocks.BlockPlacer;
import GameObjects.Blocks.BlockStone;

public class CraftingPlacer implements CraftingRecipe {

	@Override
	public boolean checkCrafting(int x, int y) {
		if ((World.getBlock(x, y - 1) instanceof BlockStone && World.getBlock(x + 1, y - 1) instanceof BlockStone
				&& World.getBlock(x, y) instanceof BlockMachine && World.getBlock(x + 1, y) instanceof BlockStone)) {

			World.setBlock(x, y, new BlockPlacer());
			World.setBlock(x + 1, y, new BlockAir());
			World.setBlock(x, y - 1, new BlockAir());
			World.setBlock(x + 1, y - 1, new BlockAir());
			return true;
		}
		return false;
	}
}
