package GameObjects;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import HelperObjects.Hitbox;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class Firebolt extends Entity {

	private int damage = 2;
	private Entity owner = null;

	public Firebolt(Position initialPosition, double[] initialVelocity, Block heldBlock, Entity owner) {
		pos = initialPosition;
		this.velocity = initialVelocity;
		this.heldBlock = heldBlock;
		fallingaccelleration = 0.025;
		isGrounded = false;
		breakCount = 0;
		drag = 1.000005;
		hitBox = new Hitbox(false, 0.25);
		this.owner = owner;
		createEntity();
	}

	@Override
	public boolean action() {
		if (isGrounded || pos.getX() < 0 || pos.getY() < 0 || pos.getX() > World.getWorld().length
				|| pos.getY() > World.getWorld()[0].length) {
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
			if (e.equals(owner)) {
				if (!hitBox.isHit(e.getHitBox(), e.getPos().getX() - pos.getX(), e.getPos().getY() - pos.getY())) {
					owner = null;
				}
			} else {
				if (e.getHP() > 0 && hitBox.isHit(e.getHitBox(), e.getPos().getX() - pos.getX(),
						e.getPos().getY() - pos.getY())) {
					e.receiveDamage(damage);
					isGrounded = true;
					break;
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
		json.put("heldBlock", getHeldBlock().toJSON());
		return json.getJSON();
	}
}
