package GameObjects.Blocks;

import GameObjects.OvenAbilityJet;
import GameObjects.OvenEntity;
import GameObjects.PlayerCharacter;
import HelperObjects.JSONObject;
import Server.GameMaster;

public class BlockOven extends Block {

	private int fuel;
	private int maxfuel = 2000;

	public BlockOven() {
		id = 4;
		friction = 3;

		breakable = true;
		breakThreshhold = 40;
	}

	public BlockOven(JSONObject block) {
		setX(Integer.parseInt(block.get("x")));
		setY(Integer.parseInt(block.get("y")));
		id = 4;
		friction = 3;

		breakable = true;
		breakThreshhold = 40;

		fuel = Integer.parseInt(block.get("fuel"));
	}

	public Block clone() {
		return new BlockOven(new JSONObject(toJSON()));
	}

	private int fuelCost = 40;
	private double boostAccelleration = 2;

	public void activateAbility(PlayerCharacter e) {
		if (fuel != 0) {
			double boostPower = 1;
			if (fuel < fuelCost)
				boostPower = (double) fuel / fuelCost;
			fuel -= fuelCost * boostPower;
			double[] boostVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
			double velocityLength = Math.sqrt(Math.pow(boostVelocity[0], 2) + Math.pow(boostVelocity[1], 2));
			double[] unitVelocity = { boostVelocity[0] / velocityLength, boostVelocity[1] / velocityLength };
			boostVelocity[0] = unitVelocity[0] * boostAccelleration * boostPower;
			boostVelocity[1] = unitVelocity[1] * boostAccelleration * boostPower;
			e.setVelocity(boostVelocity);

			new OvenAbilityJet(e.getPos(), boostVelocity, boostPower);

			e.updateInventory();
		}
	}

	public int getAbilityCooldown() {
		return 50;
	}

	private OvenEntity entity = null;

	public void updateBlock() {
		if (entity == null)
			entity = new OvenEntity(getX(), getY(), this);
	}

	@Override
	public void breakBlock() {
		if (entity != null) {
			GameMaster.removeEntity(entity);
			entity = null;
		}
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", "" + id);
		json.put("x", "" + getX());
		json.put("y", "" + getY());
		json.put("fuel", "" + fuel * 100 / maxfuel);
		return json.getJSON();
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public int getMaxfuel() {
		return maxfuel;
	}

	public void setMaxfuel(int maxfuel) {
		this.maxfuel = maxfuel;
	}

	public void removeEntity() {
		entity = null;
	}
}
