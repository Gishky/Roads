package GameObjects;

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

		super.action();

		if (isGrounded) {
			double target = pos.getX() + speed * (goLeft ? -1 : 1);
			double[] castResult;
			System.out.println(pos.getX()+"/"+pos.getY()+">"+target+"/"+pos.getY());
			castResult = World.getCastResultFirst(pos.getX(), pos.getY(), target, pos.getY());
			System.out.println(castResult[0]+"/"+castResult[1]+" "+castResult[2]+"/"+castResult[3]);
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
