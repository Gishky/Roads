package GameObjects;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import GameObjects.Blocks.BlockOven;
import HelperObjects.Position;
import Server.GameMaster;

public class OvenEntity extends Entity {

	private BlockOven oven;
	private Block smelting;
	private Block fuel;

	public OvenEntity(int x, int y, BlockOven oven, Block smelting, Block fuel) {
		entityIdentifier = "oven";
		pos = new Position();
		pos.set(x, y);
		id = count++;
		HP = maxHP;

		this.oven = oven;
		this.smelting = smelting;
		this.fuel = fuel;

		GameMaster.addEntity(this);
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
}
