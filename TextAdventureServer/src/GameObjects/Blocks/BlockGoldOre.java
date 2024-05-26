package GameObjects.Blocks;

import GameObjects.Entities.Firebolt;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import HelperObjects.Position;

public class BlockGoldOre extends Block {
	public BlockGoldOre() {
		id = 8;
		friction = 1.5;

		breakable = true;
		breakThreshhold = 24;

		smeltedBlock = new BlockGold();
		requiredFuelForSmelting = 300;
	}

	public BlockGoldOre(JSONObject block) {
		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
		id = 8;
		friction = 1.5;

		breakable = true;
		breakThreshhold = 24;

		smeltedBlock = new BlockGold();
		requiredFuelForSmelting = Integer.parseInt(block.get("smelt"));
	}

	public void activateAbility(PlayerCharacter e) {
		for (int i = 0; i < 3; i++) {
			double[] fireboltVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
			double velocityLength = Math.sqrt(Math.pow(fireboltVelocity[0], 2) + Math.pow(fireboltVelocity[1], 2));
			double angle = Math.atan(fireboltVelocity[1] / fireboltVelocity[0]);
			if (fireboltVelocity[0] < 0) {
				angle += Math.PI;
			}
			angle = angle - (double) i / 4 + 0.25;
			fireboltVelocity[0] = Math.cos(angle) * velocityLength;
			fireboltVelocity[1] = Math.sin(angle) * velocityLength;
			double[] unitVelocity = { fireboltVelocity[0] / velocityLength, fireboltVelocity[1] / velocityLength };
			fireboltVelocity[0] = unitVelocity[0];
			fireboltVelocity[1] = unitVelocity[1];

			Position fireboltpos = new Position();
			fireboltpos.set(e.getPos().getX(), e.getPos().getY());
			new Firebolt(fireboltpos, fireboltVelocity, e.getHeldBlock().getId(), e);
		}
	}

	public int getAbilityCooldown() {
		return 20;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", "" + id);
		json.put("x", "" + getX());
		json.put("y", "" + getY());
		json.put("smelt", "" + requiredFuelForSmelting);
		return json.getJSON();
	}

	public Block clone() {
		return new BlockGoldOre(new JSONObject(toJSON()));
	}
}
