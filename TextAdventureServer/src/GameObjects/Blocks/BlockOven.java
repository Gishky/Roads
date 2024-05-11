package GameObjects.Blocks;

import GameObjects.Entity;
import GameObjects.OvenEntity;
import GameObjects.World;
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

	private double boostAccelleration = 4;

	public void activateAbility(Entity e) {
		if (fuel > 0) {
			fuel-=3;
			double[] boostVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
			double velocityLength = Math.sqrt(Math.pow(boostVelocity[0], 2) + Math.pow(boostVelocity[1], 2));
			double[] unitVelocity = { boostVelocity[0] / velocityLength, boostVelocity[1] / velocityLength };
			double[] entityVelocity = e.getVelocity();
			entityVelocity[0] += unitVelocity[0] * boostAccelleration;
			entityVelocity[1] += unitVelocity[1] * boostAccelleration;
			e.setVelocity(entityVelocity);
		}
	}

	public int getAbilityCooldown() {
		return 0;
	}

	private int fuel;
	private int maxfuel = 2000;
	private OvenEntity entity = null;

	public void update() {
		Block fuel = null;
		if (World.getBlock(getX() - 1, getY()).getFuelValue() > 0) {
			fuel = World.getBlock(getX() - 1, getY());
		} else if (World.getBlock(getX() + 1, getY()).getFuelValue() > 0) {
			fuel = World.getBlock(getX() + 1, getY());
		}

		Block smelting = null;
		if (World.getBlock(getX(), getY() - 1).getSmeltedBlock() != null) {
			smelting = World.getBlock(getX(), getY() - 1);
		}

		if (entity == null && (fuel != null || smelting != null)) {
			entity = new OvenEntity(getX(), getY(), this, smelting, fuel);
		} else if (entity != null && (smelting == null || this.fuel <= 0) && fuel == null) {
			GameMaster.removeEntity(entity);
			entity = null;
		}
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

}
