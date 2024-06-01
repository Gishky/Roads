package GameObjects.Blocks;

import GameObjects.Entities.Firebolt;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import HelperObjects.Position;

public class BlockWood extends Block {

	boolean coal = false;

	public BlockWood() {
		id = 10;
		friction = 2;

		breakable = true;
		breakThreshhold = 5;

		fuelValue = 50;

		smeltedBlock = new BlockWood(new JSONObject("{fuel:600}"));
		requiredFuelForSmelting = 400;
	}

	public BlockWood(JSONObject block) {
		id = 10;
		friction = 2;

		breakable = true;
		breakThreshhold = 5;

		if (block == null || block.get("fuel") == null) {
			smeltedBlock = new BlockWood(new JSONObject("{fuel:600}"));
			requiredFuelForSmelting = 400;
		} else {
			coal = true;
		}

		if (block == null)
			return;

		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
		if (block.get("fuel") != null) {
			setFuelValue(Integer.parseInt(block.get("fuel")));
			smeltedBlock = null;
			requiredFuelForSmelting = 0;
		}
	}

	public Block clone() {
		return new BlockWood();
	}

	public void activateAbility(PlayerCharacter e) {
		if (!canAbilityActivate())
			return;

		double[] fireboltVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
		double velocityLength = Math.sqrt(Math.pow(fireboltVelocity[0], 2) + Math.pow(fireboltVelocity[1], 2));
		double[] unitVelocity = { fireboltVelocity[0] / velocityLength, fireboltVelocity[1] / velocityLength };
		fireboltVelocity[0] = unitVelocity[0];
		fireboltVelocity[1] = unitVelocity[1];

		Position fireboltpos = new Position();
		fireboltpos.set(e.getPos().getX(), e.getPos().getY());
		Firebolt bolt = new Firebolt(fireboltpos, fireboltVelocity, e.getHeldBlock().getId(), e);
		bolt.setFallingAccelleration(0.13);
	}

	public int getAbilityCooldown() {
		return 750;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", "" + id);
		json.put("x", "" + getX());
		json.put("y", "" + getY());
		json.put("fuel", "" + fuelValue);
		json.put("coal", "" + coal);
		return json.getJSON();
	}

	public void setRequiredFuelForSmelting(int value) {
		int diff = requiredFuelForSmelting - value;
		if (diff > 0)
			fuelValue += diff;
		requiredFuelForSmelting = value;
	}
}
