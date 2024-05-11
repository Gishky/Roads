package GameObjects;

import Server.GameMaster;

public class BlockOven extends Block {

	public BlockOven() {
		id = 4;
		friction = 3;

		breakable = true;
		breakThreshhold = 40;
	}

	public Block clone() {
		return new BlockOven();
	}

	public void activateAbility(Entity e) {

	}

	public int getAbilityCooldown() {
		return 30;
	}

	private OvenEntity entity = null;

	public void update() {
		Block fuel = null;
		if (World.getBlock(x - 1, y).getFuelValue() > 0) {
			fuel = World.getBlock(x - 1, y);
		} else if (World.getBlock(x + 1, y).getFuelValue() > 0) {
			fuel = World.getBlock(x + 1, y);
		}
		Block smelting = null;
		if (World.getBlock(x, y - 1).getSmeltedBlock() != null) {
			smelting = World.getBlock(x, y - 1);
		}

		if (entity == null && fuel != null && smelting != null) {
			entity = new OvenEntity(x, y, fuel, World.getBlock(x, y - 1));
		} else if (entity != null && (fuel == null || smelting == null)) {
			GameMaster.removeEntity(entity);
			entity = null;
		}
	}
}
