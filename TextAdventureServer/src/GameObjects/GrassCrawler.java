package GameObjects;

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

		super.action();

		double[] p;
		double[] target = new double[2];

		target[0] = pos.getX() + speed * (goLeft ? -1 : 1);
		p = World.getCastResult(pos.getX(), pos.getY(), target[0], pos.getY());
		if (p[0] == -1) {
			pos.setX(target[0]);
			return true;
		} else {
			World.setBlock((int) (pos.getX() + speed * (goLeft ? -1 : 1)) / Block.size, (int) pos.getY() / Block.size,
					new BlockAir());
			GameMaster.removeEntity(this);
			return false;
		}
	}
}
