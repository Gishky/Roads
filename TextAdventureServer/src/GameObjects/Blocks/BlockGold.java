package GameObjects.Blocks;

import GameObjects.Entity;
import GameObjects.Firebolt;
import GameObjects.PlayerCharacter;
import HelperObjects.JSONObject;
import HelperObjects.Position;

public class BlockGold extends Block {
	public BlockGold() {
		id = 9;
		friction = 2;

		breakable = true;
		breakThreshhold = 100;
	}
	
	public BlockGold(JSONObject block) {
		setX(Integer.parseInt(block.get("x")));
		setY(Integer.parseInt(block.get("y")));
		id = 9;
		friction = 2;

		breakable = true;
		breakThreshhold = 100;
	}

	public void activateAbility(PlayerCharacter e) {
		for (int c = 0; c < 2; c++)
			for (int i = 0; i < 3; i++) {
				double[] fireboltVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
				double velocityLength = Math.sqrt(Math.pow(fireboltVelocity[0], 2) + Math.pow(fireboltVelocity[1], 2));
				double angle = Math.atan(fireboltVelocity[1] / fireboltVelocity[0]);
				if (fireboltVelocity[0] < 0) {
					angle += Math.PI;
				}
				angle = angle - (double) i / 4 / 4 + 0.25 / 4;
				fireboltVelocity[0] = Math.cos(angle) * velocityLength;
				fireboltVelocity[1] = Math.sin(angle) * velocityLength;
				double[] unitVelocity = { fireboltVelocity[0] / velocityLength, fireboltVelocity[1] / velocityLength };
				fireboltVelocity[0] = unitVelocity[0] * 4;
				fireboltVelocity[1] = unitVelocity[1] * 4;

				Position fireboltpos = new Position();
				fireboltpos.set(e.getPos().getX(), e.getPos().getY());
				new Firebolt(fireboltpos, fireboltVelocity, e.getHeldBlock().getId(), e);
			}
	}

	public int getAbilityCooldown() {
		return 30;
	}

	public Block clone() {
		return new BlockGold();
	}

	public void activateAbility(Entity e) {

	}
}
