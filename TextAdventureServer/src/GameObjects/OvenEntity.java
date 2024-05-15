package GameObjects;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import GameObjects.Blocks.BlockOven;
import HelperObjects.Position;

public class OvenEntity extends Entity {

	private BlockOven oven;
	private Block smelting;
	private Block fuel;

	public OvenEntity(int x, int y, BlockOven oven, Block smelting, Block fuel) {
		super("oven", new Position(x, y));

		this.oven = oven;
		this.smelting = smelting;
		this.fuel = fuel;
	}

	public boolean action() {
		for (int i = 0; i < 10; i++) {
			if (fuel != null) {
				if (fuel.getFuelValue() <= 0) {
					World.setBlock(fuel.getX(), fuel.getY(), new BlockAir());
				}
				fuel.setFuelValue(fuel.getFuelValue() - 1);
				oven.setFuel(oven.getFuel() + 1);
			}
		}

		if (oven.getFuel() > 0 && smelting != null) {
			oven.setFuel(oven.getFuel() - 1);

			smelting.setRequiredFuelForSmelting(smelting.getRequiredFuelForSmelting() - 1);

			if (smelting.getRequiredFuelForSmelting() <= 0) {
				World.setBlock(smelting.getX(), smelting.getY(), smelting.getSmeltedBlock());
			}
		}
		return false;
	}

	public Block getSmelting() {
		return smelting;
	}

	public void setSmelting(Block smelting) {
		this.smelting = smelting;
	}

	public Block getFuel() {
		return fuel;
	}

	public void setFuel(Block fuel) {
		this.fuel = fuel;
	}
}
