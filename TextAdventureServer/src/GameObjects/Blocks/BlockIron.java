package GameObjects.Blocks;

import GameObjects.Entities.Entity;
import GameObjects.Entities.Firebolt;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import HelperObjects.Position;

public class BlockIron extends Block {
	public BlockIron() {
		id = 7;
		friction = 2;

		breakable = true;
		breakThreshhold = 100;
	}
	
	public BlockIron(JSONObject block) {
		id = 7;
		friction = 2;

		breakable = true;
		breakThreshhold = 100;

		if (block == null)
			return;

		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
	}

	public void activateAbility(PlayerCharacter e) {
		if (!canAbilityActivate())
			return;
		
		for (int c = 0; c < 2; c++) {
			double[] fireboltVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
			double velocityLength = Math.sqrt(Math.pow(fireboltVelocity[0], 2) + Math.pow(fireboltVelocity[1], 2));
			double angle = Math.atan(fireboltVelocity[1] / fireboltVelocity[0]);
			if (fireboltVelocity[0] < 0) {
				angle += Math.PI;
			}
			fireboltVelocity[0] = Math.cos(angle) * velocityLength;
			fireboltVelocity[1] = Math.sin(angle) * velocityLength;
			double[] unitVelocity = { fireboltVelocity[0] / velocityLength, fireboltVelocity[1] / velocityLength };
			fireboltVelocity[0] = unitVelocity[0] * 4;
			fireboltVelocity[1] = unitVelocity[1] * 4;

			Position fireboltpos = new Position();
			fireboltpos.set(e.getPos().getX(), e.getPos().getY());
			Firebolt bolt = new Firebolt(fireboltpos, fireboltVelocity, e.getHeldBlock().getId(), e, 2, true);
		}
	}

	public int getAbilityCooldown() {
		return 1500;
	}

	public Block clone() {
		return new BlockIron();
	}

	public void activateAbility(Entity e) {

	}
}
