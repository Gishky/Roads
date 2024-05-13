package GameObjects;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import GameObjects.Blocks.BlockGrass;
import HelperObjects.Hitbox;
import HelperObjects.Position;
import Server.GameMaster;

public class GrassCrawler extends Entity {

	private int damage = 1;
	private double speed = 2;
	private boolean goLeft;

	public GrassCrawler(Position initialPosition, double[] initialVelocity) {
		super("firebolt");
		this.pos = initialPosition;
		this.heldBlock = new BlockGrass();
		breakCount = 0;
		hitBox = new Hitbox(false, 3);
		velocity = initialVelocity;
		goLeft = initialVelocity[0] < 0;
	}

	@Override
	public boolean action() {
		for (Entity e : GameMaster.getEntities()) {
			if (e.getHP() > 0
					&& hitBox.isHit(e.getHitBox(), e.getPos().getX() - pos.getX(), e.getPos().getY() - pos.getY())) {
				e.receiveDamage(damage);

				GameMaster.removeEntity(this);
				return false;
			}
		}

		int x = (int) pos.getX();
		int y = (int) pos.getY();

		velocity[0] /= drag;
		if (isGrounded) {
			velocity[0] /= World.getWorld()[(int) pos.getX() / Block.size][(int) pos.getY() / Block.size + 1]
					.getFriction();
		}
		velocity[1] /= drag;
		velocity[1] += fallingaccelleration;
		double targety = pos.getY() + velocity[1];
		double targetx = pos.getX() + velocity[0];
		double[] castResult = World.getCastResultSlide(pos.getX(), pos.getY(), targetx, targety);
		if (castResult[0] != -1) {
			isGrounded = castResult[1] < targety;
			if (castResult[0] == pos.getX() && castResult[1] == pos.getY()) {
				velocity[0] = 0;
				velocity[1] = 0;
			} else {
				if ((int) (pos.getX() / Block.size) != (int) (castResult[0] / Block.size)) {
					breakCount = 0;
				}
				pos.set(castResult[0], castResult[1]);

				velocity[0] -= targetx - castResult[0];
				velocity[1] *= -1.1;

				if (Math.abs(velocity[1]) < 0.1)
					velocity[1] = 0;
			}
		} else {
			isGrounded = false;
			breakCount = 0;
			pos.set(targetx, targety);
		}

		if (isGrounded) {
			double target = pos.getX() + speed * (goLeft ? -1 : 1);
			castResult = World.getCastResultFirst(pos.getX(), pos.getY(), target, pos.getY());
			if (castResult[0] == -1) {
				pos.setX(target);
				return true;
			} else {
				World.setBlock((int) (castResult[2]), (int) (castResult[3]), new BlockAir());
				GameMaster.removeEntity(this);
				return false;
			}
		}
		return true;
	}
}
