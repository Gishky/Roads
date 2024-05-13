package GameObjects;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockOven;
import HelperObjects.Position;
import Server.GameMaster;

public class OvenAbilityJet extends Entity {

	private BlockOven oven;

	public OvenAbilityJet(Position initialPosition, double initialVelocityx, double initialVelocityy, BlockOven oven) {
		super("ovenAbility");
		this.pos = initialPosition;
		this.heldBlock = new Block();
		parameters = -initialVelocityx + "/" + -initialVelocityy;
		breakCount = 0;

		this.oven = oven;
	}

	private int lifetime = 5;

	@Override
	public boolean action() {
		lifetime--;
		if (lifetime < 0) {
			GameMaster.removeEntity(this);
			oven.setJet(null);
		}
		return true;
	}

	public void setVelocity(double velocityx, double velocityy) {
		parameters = -velocityx + "/" + -velocityy;
	}

	public void reactivate() {
		lifetime = 5;
	}
}
