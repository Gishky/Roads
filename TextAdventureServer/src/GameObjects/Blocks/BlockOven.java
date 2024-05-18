package GameObjects.Blocks;

import GameObjects.OvenAbilityJet;
import GameObjects.OvenEntity;
import GameObjects.PlayerCharacter;
import HelperObjects.JSONObject;
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

	private double boostAccelleration = 0.2;
	private OvenAbilityJet jet = null;

	public void activateAbility(PlayerCharacter e) {
		if (fuel > 0) {
			fuel -= 3;
			double[] boostVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
			double velocityLength = Math.sqrt(Math.pow(boostVelocity[0], 2) + Math.pow(boostVelocity[1], 2));
			double[] unitVelocity = { boostVelocity[0] / velocityLength, boostVelocity[1] / velocityLength };
			double[] entityVelocity = e.getVelocity();
			entityVelocity[0] += unitVelocity[0] * boostAccelleration;
			entityVelocity[1] += unitVelocity[1] * boostAccelleration;
			e.setVelocity(entityVelocity);

			if (jet == null) {
				jet = new OvenAbilityJet(e.getPos(), unitVelocity[0] * boostAccelleration,
						unitVelocity[1] * boostAccelleration, this);
			} else {
				jet.setVelocity(unitVelocity[0] * boostAccelleration, unitVelocity[1] * boostAccelleration);
				jet.reactivate();
			}
			e.updateInventory();
		}
	}

	public int getAbilityCooldown() {
		return 0;
	}

	private int fuel;
	private int maxfuel = 2000;
	private OvenEntity entity = null;

	public void update() {
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

	public void setJet(OvenAbilityJet object) {
		jet = object;
	}

	public void removeEntity() {
		entity = null;
	}
}
