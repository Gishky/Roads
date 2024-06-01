package GameObjects.Blocks;

import GameObjects.Entities.GrassCrawler;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import HelperObjects.Position;

public class BlockGrass extends Block {
	public BlockGrass() {
		id = 2;
		friction = 1.5;

		breakable = true;
		breakThreshhold = 5;
	}
	
	public BlockGrass(JSONObject block) {
		id = 2;
		friction = 1.5;

		breakable = true;
		breakThreshhold = 5;

		if (block == null)
			return;

		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
	}

	public Block clone() {
		return new BlockGrass();
	}

	public void activateAbility(PlayerCharacter e) {
		if (!canAbilityActivate())
			return;
		
		double[] initialVelocity = { e.getMousePosition().getX(), e.getMousePosition().getY() };
		double velocityLength = Math.sqrt(Math.pow(initialVelocity[0], 2) + Math.pow(initialVelocity[1], 2));
		double[] unitVelocity = { initialVelocity[0] / velocityLength, initialVelocity[1] / velocityLength };
		initialVelocity[0] = unitVelocity[0];
		initialVelocity[1] = unitVelocity[1];

		Position initialPos = new Position();
		initialPos.set(e.getPos().getX(), e.getPos().getY());
		new GrassCrawler(initialPos, initialVelocity, e, e.getHeldBlock().getId());
	}

	public int getAbilityCooldown() {
		return 1500;
	}

}
