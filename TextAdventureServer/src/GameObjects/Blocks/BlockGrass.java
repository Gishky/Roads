package GameObjects.Blocks;

import GameObjects.GrassCrawler;
import GameObjects.PlayerCharacter;
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
		setX(Integer.parseInt(block.get("x")));
		setY(Integer.parseInt(block.get("y")));
		id = 2;
		friction = 1.5;

		breakable = true;
		breakThreshhold = 5;
	}

	public Block clone() {
		return new BlockGrass();
	}

	public void activateAbility(PlayerCharacter e) {
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
		return 30;
	}

}
