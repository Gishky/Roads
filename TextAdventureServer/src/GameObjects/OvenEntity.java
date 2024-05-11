package GameObjects;

import HelperObjects.Position;
import Server.GameMaster;

public class OvenEntity extends Entity {

	private Block fuel;
	private Block smelting;

	public OvenEntity(int x, int y, Block fuel, Block smelting) {
		entityIdentifier = "oven";
		pos = new Position();
		pos.set(x, y);
		id = count++;
		HP = maxHP;

		this.fuel = fuel;
		this.smelting = smelting;

		GameMaster.addEntity(this);
	}

	public boolean action() {
		fuel.setFuelValue(fuel.getFuelValue() - 1);
		smelting.setRequiredFuelForSmelting(smelting.getRequiredFuelForSmelting() - 1);

		if (smelting.getRequiredFuelForSmelting() <= 0) {
			World.setBlock(smelting.x, smelting.y, smelting.getSmeltedBlock());
		}

		if (fuel.getFuelValue() <= 0) {
			World.setBlock(fuel.x, fuel.y, new BlockAir());
		}

		return false;
	}
}
