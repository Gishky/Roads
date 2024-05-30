package Crafting;

import java.util.LinkedList;

public class CraftingHandler {

	private static CraftingHandler instance = null;

	private LinkedList<CraftingRecipe> recipes;

	public CraftingHandler() {
		recipes = new LinkedList<CraftingRecipe>();
		recipes.add(new CraftingOven());
		recipes.add(new CraftingRelay());
		recipes.add(new CraftingActivator());
		recipes.add(new CraftingMachine());
		recipes.add(new CraftingGoldChunk());
		recipes.add(new CraftingIronChunk());
	}

	private void getCraftedBlock(int x, int y) {
		for (CraftingRecipe r : recipes) {
			if (r.checkCrafting(x, y))
				return;
		}
	}

	public static void tryCrafting(int x, int y) {
		if (instance == null)
			instance = new CraftingHandler();

		instance.getCraftedBlock(x, y);
	}
}
