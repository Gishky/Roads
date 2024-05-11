package HelperObjects;

import java.util.LinkedList;

import GameObjects.Block;
import GameObjects.BlockAir;
import GameObjects.World;

public class CraftingHandler {

	private static CraftingHandler instance = null;

	private LinkedList<CraftingRecipe> recipes;

	public CraftingHandler() {
		recipes = new LinkedList<CraftingRecipe>();
		recipes.add(new CraftingOven());
	}

	private Block getCraftedBlock(int x, int y) {
		for (CraftingRecipe r : recipes) {
			Block craftedBlock = r.checkCrafting(x, y);
			if (craftedBlock != null) {
				return craftedBlock;
			}
		}
		return null;
	}

	public static void tryCrafting(int x, int y) {
		if (instance == null)
			instance = new CraftingHandler();

		Block craftedBlock = instance.getCraftedBlock(x, y);
		if (craftedBlock != null) {
			World.setBlock(x, y, craftedBlock);
			World.setBlock(x + 1, y, new BlockAir());
			World.setBlock(x, y + 1, new BlockAir());
			World.setBlock(x + 1, y + 1, new BlockAir());
		}
	}
}
