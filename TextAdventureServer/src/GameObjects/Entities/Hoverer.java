package GameObjects.Entities;

import java.util.Random;

import GameObjects.World;
import HelperObjects.Hitbox;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class Hoverer extends Entity {

	private int damage = 10;
	private double speed = 0.01;
	private PlayerCharacter target = null;

	public Hoverer(PlayerCharacter player) {
		Random r = new Random();
		double x = r.nextDouble() * 20;
		x += 10;
		if (r.nextBoolean()) {
			x *= -1;
		}
		x += player.getX();
		if (x < 0 || x > World.getWorld().length) {
			x = 0;
			deleteEntity();
		}
		Position pos = new Position(x, World.getCastResultFirst(x, 0, x, World.getWorld().length)[1] - 10);

		this.pos = pos;
		target = player;
		hitBox = new Hitbox(false, 0.13);
		maxHP = 2;
		HP = maxHP;
		createEntity();
	}

	private int actioncount = 0;

	@Override
	public boolean action() {
		if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() > World.getWorld().length
				|| pos.getY() > World.getWorld()[0].length) {
			GameMaster.removeEntity(this, false);
			return false;
		}

		if (!rush) {
			velocity[0] /= drag;
			velocity[1] /= drag;
		}
		if (World.getCastResultFirst(getX(), getY(), target.getX(), target.getY())[0] != -1) {
			if (World.getBlock((int) getX(), (int) getY()).getPathToPlayer() != target) {
				target = World.getBlock((int) getX(), (int) getY()).getPathToPlayer();
			}
			actioncount = 0;
			rush = false;
			pathFind();
		} else {
			nonPathFind();
		}
		return true;
	}

	private boolean rush = false;

	private void nonPathFind() {
		if (target == null || target.isDeleted()) {
			for (Entity e : GameMaster.getEntities()) {
				if (e instanceof PlayerCharacter) {
					if (World.getCastResultFirst(getX(), getY(), e.getX(), e.getY())[0] != -1) {
						target = (PlayerCharacter) e;
						break;
					}
				}
			}
		}

		if (new Random().nextInt(1000) == 1) {
			velocity[0] = 0;
			velocity[1] = 0;
			rush = true;
		}
		if (rush) {
			rush();
			return;
		}
		if (actioncount == 0) {
			actioncount = new Random().nextInt(160);
		}
		double offsetx = Math.sin(actioncount++ / 20) * 5;
		double offsety = Math.cos(actioncount / 6) * 2;
		double distance = Math.sqrt(Math.pow(getX() - target.getX() + offsetx, 2)
				+ Math.pow(getY() - (target.getY() - 3 - offsety / 3), 2));
		velocity[0] += (getX() - target.getX() + offsetx) / distance * speed;
		velocity[1] += (getY() - (target.getY() - 3 - offsety / 3)) / distance * speed;
		double targetx = pos.getX() - velocity[0];
		double targety = pos.getY() - velocity[1];

		double[] castResult = World.getCastResultSlide(getX(), getY(), targetx, targety);
		if (castResult[0] == -1) {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), targetx, targety,
					e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				return;
			}
			pos.set(targetx, targety);
		} else {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), castResult[0], castResult[1],
					e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				return;
			}
			pos.set(castResult[0], castResult[1]);

			velocity[0] += targetx - castResult[0];
			velocity[1] += targety - castResult[1];
		}
	}

	private void rush() {
		actioncount = 0;
		double distance = Math.sqrt(Math.pow(getX() - target.getX(), 2) + Math.pow(getY() - target.getY(), 2));
		velocity[0] += (getX() - target.getX()) / distance * speed / 6;
		velocity[1] += (getY() - target.getY()) / distance * speed / 6;
		double targetx = pos.getX() - velocity[0];
		double targety = pos.getY() - velocity[1];

		double[] castResult = World.getCastResultSlide(getX(), getY(), targetx, targety);
		if (castResult[0] == -1) {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), targetx, targety,
					e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				rush = false;
				return;
			}
			pos.set(targetx, targety);
		} else {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), castResult[0], castResult[1],
					e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				rush = false;
				return;
			}
			pos.set(castResult[0], castResult[1]);

			velocity[0] += targetx - castResult[0];
			velocity[1] += targety - castResult[1];

			rush = false;
		}
	}

	private void pathFind() {
		int[] direction = World.getBlock((int) getX(), (int) getY()).getDirectionToPlayer();
		if (direction[0] == 0 && direction[1] == 0) {
			nonPathFind();
			return;
		}
		velocity[0] += direction[0] * speed;
		velocity[1] += direction[1] * speed;
		double targetx = pos.getX() - velocity[0];
		double targety = pos.getY() - velocity[1];

		double[] castResult = World.getCastResultSlide(getX(), getY(), targetx, targety);
		if (castResult[0] == -1) {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), targetx, targety,
					e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				return;
			}
			pos.set(targetx, targety);
		} else {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), castResult[0], castResult[1],
					e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				return;
			}
			pos.set(castResult[0], castResult[1]);

			velocity[0] += targetx - castResult[0];
			velocity[1] += targety - castResult[1];
		}
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "hoverer");
		json.put("id", "" + id);
		json.put("x", String.format("%.4f", getX()));
		json.put("y", String.format("%.4f", getY()));
		json.put("hp%", "" + getHPPercentile());
		json.put("size", "" + hitBox.getRadius());
		json.put("rush", "" + rush);
		return json.getJSON();
	}
}
