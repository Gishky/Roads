package GameObjects;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import GameObjects.Blocks.BlockOven;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class OvenEntity extends Entity {

	private BlockOven oven;
	private boolean burningLeft = false;
	private boolean burningRight = false;
	private boolean smelting = false;

	public OvenEntity(int x, int y, BlockOven oven) {
		int ovenX = oven.getX();
		int ovenY = oven.getY();

		Block left = World.getBlock(ovenX - 1, ovenY);
		Block right = World.getBlock(ovenX + 1, ovenY);
		Block up = World.getBlock(ovenX, ovenY - 1);

		if (left.getFuelValue() > 0) {
			burningLeft = true;
		} else {
			burningLeft = false;
		}

		if (right.getFuelValue() > 0) {
			burningRight = true;
		} else {
			burningRight = false;
		}

		if (oven.getFuel() > 0 && up.getRequiredFuelForSmelting() > 0) {
			smelting = true;
		} else {
			smelting = false;
		}

		this.pos = new Position(x, y);
		this.oven = oven;
		createEntity();
	}

	public boolean action() {
		int ovenX = oven.getX();
		int ovenY = oven.getY();

		Block left = World.getBlock(ovenX - 1, ovenY);
		Block right = World.getBlock(ovenX + 1, ovenY);
		Block up = World.getBlock(ovenX, ovenY - 1);

		boolean pastSmelting = smelting;
		boolean pastBurningLeft = burningLeft;
		boolean pastBurningRight = burningRight;

		if (left.getFuelValue() > 0) {
			for (int i = 0; i < 5; i++) {
				left.setFuelValue(left.getFuelValue() - 1);
				oven.setFuel(oven.getFuel() + 1);
				if (left.getFuelValue() <= 0) {
					World.setBlock(ovenX - 1, ovenY, new BlockAir());
					break;
				}
			}
			burningLeft = true;
		} else {
			burningLeft = false;
		}

		if (right.getFuelValue() > 0) {
			for (int i = 0; i < 5; i++) {
				right.setFuelValue(right.getFuelValue() - 1);
				oven.setFuel(oven.getFuel() + 1);
				if (right.getFuelValue() <= 0) {
					World.setBlock(ovenX + 1, ovenY, new BlockAir());
					break;
				}
			}
			burningRight = true;
		} else {
			burningRight = false;
		}

		if (oven.getFuel() > 0 && up.getRequiredFuelForSmelting() > 0) {
			oven.setFuel(oven.getFuel() - 1);
			up.setRequiredFuelForSmelting(up.getRequiredFuelForSmelting() - 1);
			if (up.getRequiredFuelForSmelting() <= 0) {
				World.setBlock(ovenX, ovenY - 1, up.getSmeltedBlock());
			}
			smelting = true;
		} else {
			smelting = false;
		}

		if (!(burningLeft || burningRight || smelting)) {
			oven.removeEntity();
			GameMaster.removeEntity(this);
		} else if (!(burningLeft == pastBurningLeft && burningRight == pastBurningRight && smelting == pastSmelting)) {
			JSONObject json = new JSONObject();
			json.put("id", "" + id);
			json.put("smelting", "" + smelting);
			json.put("burningL", "" + burningLeft);
			json.put("burningR", "" + burningRight);
			GameMaster.sendToAll("{action:updateEntity,entity:" + json.getJSON() + "}", true);
		}

		return false;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "oven");
		json.put("id", "" + id);
		json.put("x", "" + pos.getX());
		json.put("y", "" + pos.getY());
		json.put("smelting", "" + smelting);
		json.put("burningL", "" + burningLeft);
		json.put("burningR", "" + burningRight);
		return json.getJSON();
	}
}
