package GameObjects;

import HelperObjects.Hitbox;
import HelperObjects.Position;
import Server.GameMaster;

public class Firebolt extends Entity {

	private int damage = 10;

	public Firebolt(Position initialPosition, double[] initialVelocity) {
		super("firebolt");
		this.velocity = initialVelocity;
		this.pos = initialPosition;
		fallingaccelleration = 0.5;
		isGrounded = false;
		breakCount = 0;
		hitBox = new Hitbox(false, 3);
	}

	@Override
	public boolean action() {
		if (isGrounded) {
			GameMaster.removeEntity(this);
			return false;
		}

		velocity[0] /= drag;
		if (isGrounded) {
			velocity[0] /= World.getWorld()[(int) pos.getX() / Block.size][(int) pos.getY() / Block.size + 1]
					.getFriction();
		}
		velocity[1] += fallingaccelleration;
		double targety = pos.getY() + velocity[1];
		double targetx = pos.getX() + velocity[0];
		double[] p = World.getCastResult(pos.getX(), pos.getY(), targetx, targety);
		if (p[0] != -1) {
			pos.set(p[0], p[1]);
			isGrounded = true;
		} else {
			pos.set(targetx, targety);
		}
		
		for (Entity e : GameMaster.getEntities()) {
			if (e.getHP() > 0
					&& hitBox.isHit(e.getHitBox(), e.getPos().getX() - pos.getX(), e.getPos().getY() - pos.getY())) {
				e.receiveDamage(damage);
				GameMaster.removeEntity(this);
				return false;
			}
		}

		return true;
	}
}
