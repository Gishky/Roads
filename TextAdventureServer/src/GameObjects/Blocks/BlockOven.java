package GameObjects.Blocks;

import GameObjects.Entities.OvenAbilityJet;
import GameObjects.Entities.OvenEntity;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import Server.GameMaster;

public class BlockOven extends Block {

	private int fuel;
	private int maxfuel = 2000;

	public BlockOven() {
		this(null);
	}

	public BlockOven(JSONObject block) {
		id = 4;
		friction = 3;

		breakable = true;
		breakThreshhold = 40;

		if (block == null)
			return;

		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
		if (block.get("fuel") != null)
			fuel = Integer.parseInt(block.get("fuel"));
	}

	public Block clone() {
		return new BlockOven(new JSONObject(toJSON()));
	}

	private int fuelCost = 40;
	private double boostAccelleration = 2;

	public void activateAbility(PlayerCharacter e) {
		if (!canAbilityActivate())
			return;

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

			new OvenAbilityJet(e.getId(), boostVelocity, boostPower);

			e.updateInventory();
		} else {
			setAbilityTime(System.currentTimeMillis());
		}
	}

	public int getAbilityCooldown() {
		return 2500;
	}

	private OvenEntity entity = null;

	public void update() {
		if (entity == null)
			entity = new OvenEntity(getX(), getY(), this);
		entity.action();
	}

	@Override
	public void breakBlock() {
		if (entity != null) {
			GameMaster.removeEntity(entity, false);
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
