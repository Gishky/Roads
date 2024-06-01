package GameObjects.Entities;

import GameObjects.World;
import GameObjects.Blocks.BlockAir;
import HelperObjects.Hitbox;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class GrassCrawler extends Entity {

	private int damage = 1;
	private double speed = 0.1;
	private boolean goLeft;
	private Entity owner = null;
	private int colourBlockID;

	public GrassCrawler(Position initialPosition, double[] initialVelocity, Entity owner, int colourBlockID) {
		pos = initialPosition;
		hitBox = new Hitbox(false, 0.15);
		velocity = initialVelocity;
		goLeft = initialVelocity[0] < 0;
		this.owner = owner;
		this.colourBlockID = colourBlockID;
		createEntity();
	}

	@Override
	public boolean action() {
		if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() > World.getWorld().length
				|| pos.getY() > World.getWorld()[0].length) {
			GameMaster.removeEntity(this, false);
			return false;
		}

		for (Entity e : GameMaster.getEntities()) {
			if (e.equals(owner)) {
				if (!hitBox.isHit(e.getHitBox(), e.getPos().getX() - pos.getX(), e.getPos().getY() - pos.getY())) {
					owner = null;
				}
			} else {
				if (e instanceof PlayerCharacter) {
					PlayerCharacter p = (PlayerCharacter) e;
					if (p.getHP() > 0 && hitBox.isHit(p.getHitBox(), p.getPos().getX() - pos.getX(),
							p.getPos().getY() - pos.getY())) {
						p.receiveDamage(damage);

						GameMaster.removeEntity(this, false);
						return false;
					}
				}
			}
		}

		velocity[0] /= drag;
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
				if ((int) pos.getX() != (int) castResult[0]) {
				}
				pos.set(castResult[0], castResult[1]);

				velocity[0] -= targetx - castResult[0];
				velocity[1] *= -1.06;

				if (Math.abs(velocity[1]) < 0.001)
					velocity[1] = 0;
			}
		} else {
			isGrounded = false;
			pos.set(targetx, targety);
		}

		double target = pos.getX() + speed * (goLeft ? -1 : 1);
		castResult = World.getCastResultFirst(pos.getX(), pos.getY(), target, pos.getY());
		if (castResult[0] == -1) {
			pos.setX(target);
			return true;
		} else {
			World.setBlock((int) (castResult[2]), (int) (castResult[3]), new BlockAir());
			GameMaster.removeEntity(this, false);
			return false;
		}
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "firebolt");
		json.put("id", "" + id);
		json.put("x", String.format("%.4f", pos.getX()));
		json.put("y", String.format("%.4f", pos.getY()));
		json.put("colourBlock", "" + colourBlockID);
		return json.getJSON();
	}
}
