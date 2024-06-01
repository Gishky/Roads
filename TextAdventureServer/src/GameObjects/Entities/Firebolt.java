package GameObjects.Entities;

import GameObjects.World;
import GameObjects.Blocks.BlockAir;
import HelperObjects.Hitbox;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class Firebolt extends Entity {

	private int damage = 2;
	private Entity owner = null;
	private int colourBlockID;

	public Firebolt(Position initialPosition, double[] initialVelocity, int colourBlockID, Entity owner, int damage) {
		pos = initialPosition;
		this.velocity = initialVelocity;
		fallingaccelleration = 0.025;
		isGrounded = false;
		drag = 1.000005;
		hitBox = new Hitbox(false, 0.25);
		this.owner = owner;
		this.colourBlockID = colourBlockID;
		this.damage = damage;
		createEntity();
	}

	public Firebolt(Position initialPosition, double[] initialVelocity, int colourBlockID, Entity owner) {
		pos = initialPosition;
		this.velocity = initialVelocity;
		fallingaccelleration = 0.025;
		isGrounded = false;
		drag = 1.000005;
		hitBox = new Hitbox(false, 0.25);
		this.owner = owner;
		this.colourBlockID = colourBlockID;
		createEntity();
	}

	@Override
	public boolean action() {
		if (isGrounded || pos.getX() < 0 || pos.getY() < 0 || pos.getX() > World.getWorld().length
				|| pos.getY() > World.getWorld()[0].length) {
			GameMaster.removeEntity(this, false);
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
						isGrounded = true;
						break;
					}
				}
			}
		}
		return true;
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
