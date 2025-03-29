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
	private boolean destructive = false;

	public Firebolt(Position initialPosition, double[] initialVelocity, int colourBlockID, Entity owner, int damage) {
		pos = initialPosition;
		this.velocity = initialVelocity;
		fallingaccelleration = 0.025;
		isGrounded = false;
		drag = 1.000005;
		hitBox = new Hitbox(false, 0.1);
		this.owner = owner;
		this.colourBlockID = colourBlockID;
		this.damage = damage;
		createEntity();
	}
	
	public Firebolt(Position initialPosition, double[] initialVelocity, int colourBlockID, Entity owner, int damage, boolean destructive) {
		pos = initialPosition;
		this.velocity = initialVelocity;
		fallingaccelleration = 0.025;
		isGrounded = false;
		drag = 1.000005;
		hitBox = new Hitbox(false, 0.1);
		this.owner = owner;
		this.colourBlockID = colourBlockID;
		this.damage = damage;
		this.destructive = destructive;
		createEntity();
	}

	public Firebolt(Position initialPosition, double[] initialVelocity, int colourBlockID, Entity owner) {
		pos = initialPosition;
		this.velocity = initialVelocity;
		fallingaccelleration = 0.025;
		isGrounded = false;
		drag = 1.000005;
		hitBox = new Hitbox(false, 0.1);
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
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), castResult[0], castResult[1],
					e -> (!e.equals(owner) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				isGrounded = true;
				return true;
			}
			pos.set(castResult[0], castResult[1]);
			if(destructive)
				World.setBlock((int) (castResult[2]), (int) (castResult[3]), new BlockAir());
			isGrounded = true;
		} else {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), targetx, targety,
					e -> (!e.equals(owner) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				isGrounded = true;
				return true;
			}
			pos.set(targetx, targety);
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
		json.put("size", "" + hitBox.getRadius());
		return json.getJSON();
	}
}
