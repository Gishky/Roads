package GameObjects.Entities;

import java.util.Random;

import GameObjects.World;
import HelperObjects.Hitbox;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class Chomper extends Entity{

	private int damage = 20;
	private double speed = 0.01;
	private boolean moveLeft;
	
	public Chomper(PlayerCharacter player) {
		Random r = new Random();
		double x = r.nextDouble()*60;
		x+=30;
		if(r.nextBoolean()) {
			x*=-1;
		}
		x+=player.getX();
		if(x < 0 || x > World.getWorld().length) {
			x = 0;
			deleteEntity();
		}
		Position pos = new Position(x, 0);
		
		this.pos = pos;
		moveLeft = pos.getX()>player.getX();
		hitBox = new Hitbox(false, 0.2);
		maxHP = 5;
		HP = maxHP;
		createEntity();
	}
	
	@Override
	public boolean action() {
		if(maxHPisZero())
		System.out.println("error:"+id);
		if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() > World.getWorld().length
				|| pos.getY() > World.getWorld()[0].length) {
			GameMaster.removeEntity(this, false);
			return false;
		}
		
		if (HP < maxHP) {
			HP += 0.01;
			if(HP > maxHP)
				HP = maxHP;
		}

		velocity[0] /= drag;
		velocity[1] /= drag;
		velocity[1] += fallingaccelleration/2;
		double targety = pos.getY() + velocity[1];
		double targetx = pos.getX() + velocity[0];
		double[] castResult = World.getCastResultSlide(pos.getX(), pos.getY(), targetx, targety);
		if (castResult[0] != -1) {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), castResult[0], castResult[1],
					e -> (!e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				return false;
			}
			isGrounded = castResult[1] < targety;
			if (castResult[0] != targetx) {
				moveLeft = !moveLeft;
				velocity[0] = 8*speed * (moveLeft ? -1 : 1);
				velocity[1] = -5*fallingaccelleration;
				receiveDamage(0.4);
			} else {
				if ((int) pos.getX() != (int) castResult[0]) {
				}
				pos.set(castResult[0], castResult[1]);

				velocity[0] -= targetx - castResult[0];
				velocity[1] *= -1.06;

				if (Math.abs(velocity[1]) < 0.001)
					velocity[1] = 0;
			}
			pos.set(castResult[0], castResult[1]);
		} else {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), targetx, targety,
					e -> (!e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				return false;
			}
			isGrounded = false;
			pos.set(targetx, targety);
		}
		velocity[0] += speed * (moveLeft ? -1 : 1);
		return true;
	}
	
	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "chomper");
		json.put("id", "" + id);
		json.put("x", String.format("%.4f", pos.getX()));
		json.put("y", String.format("%.4f", pos.getY()));
		json.put("hp%", "" + getHPPercentile());
		json.put("size", "" + hitBox.getRadius());
		return json.getJSON();
	}
}
