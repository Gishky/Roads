package GameObjects;

import GameObjects.Blocks.Block;
import HelperObjects.Position;
import Server.GameMaster;

public class OvenAbilityJet extends Entity {

	public OvenAbilityJet(Position initialPosition, double[] initialVelocity) {
		super("ovenAbility");
		this.pos = initialPosition;
		this.heldBlock = new Block();
		velocity = initialVelocity;
		breakCount = 0;
		parameters = velocity[0] + "/" + velocity[1];
	}


	@Override
	public boolean action() {
		pos.setX(pos.getX() + velocity[0]);
		pos.setY(pos.getY() + velocity[1]);
		GameMaster.removeEntity(this);
		return true;
	}
}
