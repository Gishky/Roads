package GameObjects.Blocks;

import GameObjects.Entities.Firebolt;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import HelperObjects.Position;

public class BlockWood extends Block {
	public BlockWood() {
		id = 10;
		friction = 2;

		breakable = true;
		breakThreshhold = 5;
		
		fuelValue = 50;
	}

	public BlockWood(JSONObject block) {
		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
		id = 10;
		friction = 2;

		breakable = true;
		breakThreshhold = 5;
	}

	public Block clone() {
		return new BlockWood();
	}

	public void activateAbility(PlayerCharacter e) {
		double[] fireboltVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
		double velocityLength = Math.sqrt(Math.pow(fireboltVelocity[0], 2) + Math.pow(fireboltVelocity[1], 2));
		double[] unitVelocity = { fireboltVelocity[0] / velocityLength, fireboltVelocity[1] / velocityLength };
		fireboltVelocity[0] = unitVelocity[0];
		fireboltVelocity[1] = unitVelocity[1];

		Position fireboltpos = new Position();
		fireboltpos.set(e.getPos().getX(), e.getPos().getY());
		new Firebolt(fireboltpos, fireboltVelocity, e.getHeldBlock().getId(), e);
	}

	public int getAbilityCooldown() {
		return 15;
	}
}
