package GameObjects;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import HelperObjects.Hitbox;
import HelperObjects.Position;
import Server.GameMaster;

public class Firebolt extends Entity {

	private int damage = 2;

	public Firebolt(Position initialPosition, double[] initialVelocity, Block heldBlock) {
		super("firebolt");
		this.velocity = initialVelocity;
		this.pos = initialPosition;
		this.heldBlock = heldBlock;
		fallingaccelleration = 0.5;
		isGrounded = false;
		breakCount = 0;
		drag = 1.0001;
		hitBox = new Hitbox(false, 5);
	}

	@Override
	public boolean action() {
		if (isGrounded || pos.getX() < 0 || pos.getY() < 0 || pos.getX() > World.getWorld().length * Block.size
				|| pos.getY() > World.getWorld()[0].length * Block.size) {
			GameMaster.removeEntity(this);
			return false;
		}

		velocity[0] /= drag;
		velocity[1] += fallingaccelleration;
		double targety = pos.getY() + velocity[1];
		double targetx = pos.getX() + velocity[0];
		double[] castResult = World.getCastResultFirst(pos.getX(), pos.getY(), targetx, targety);
		if (castResult[0] != -1) {
			pos.set(castResult[0], castResult[1]);
			World.setBlock((int) (castResult[2]), (int) (castResult[3]), new BlockAir());
			isGrounded = true;
		} else {
			pos.set(targetx, targety);
		}

		for (Entity e : GameMaster.getEntities()) {
			if (e.getHP() > 0
					&& hitBox.isHit(e.getHitBox(), e.getPos().getX() - pos.getX(), e.getPos().getY() - pos.getY())) {
				e.receiveDamage(damage);
				isGrounded = true;
				break;
			}
		}
		return true;
	}
}
