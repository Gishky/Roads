package GameObjects.Entities;

import GameObjects.World;
import HelperObjects.Hitbox;
import HelperObjects.Position;
import Server.GameMaster;

public class Entity {

	protected static int count = 0;
	protected int id;

	protected Position pos;
	protected double[] velocity = { 0.0, 0.0 };

	protected double accelleration = 0.1;
	protected double fallingaccelleration = 0.125;
	protected double jumpforce = 1.15;
	protected double drag = 1.1;

	protected Hitbox hitBox = new Hitbox(false, 0);

	protected boolean isGrounded = false;
	protected Position mouse;

	protected int maxHP = 0;
	protected double HP;

	private boolean deleted = false;

	public Entity(Position pos) {
		this.pos = pos;
		createEntity();
	}

	public Entity() {
	}

	public void createEntity() {
		id = count++;
		GameMaster.addEntity(this);
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	protected boolean actionUpdateOverride = false;

	public boolean action() {
		velocity[0] /= drag;
		if (isGrounded) {
			velocity[0] /= World.getWorld()[(int) pos.getX()][(int) pos.getY() + 1].getFriction();
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
				if ((int) (pos.getX()) != (int) (castResult[0])) {
				}
				pos.set(castResult[0], castResult[1]);

				velocity[0] -= targetx - castResult[0];
				velocity[1] -= targety - castResult[1];

				return true;
			}
		} else {
			isGrounded = false;
			pos.set(targetx, targety);
			return true;
		}

		if (actionUpdateOverride) {
			actionUpdateOverride = false;
			return true;
		}
		return false;
	}

	public int getId() {
		return id;
	}

	public static int getNextID() {
		return count;
	}

	public Hitbox getHitBox() {
		return hitBox;
	}

	public Position getMousePosition() {
		return mouse;
	}

	public void setFallingAccelleration(double accelleration) {
		fallingaccelleration = accelleration;
	}

	public double[] getVelocity() {
		return velocity;
	}

	public void setVelocity(double[] velocity) {
		this.velocity = velocity;
	}

	public float getHPPercentile() {
		if (maxHP == 0)
			return 100;
		return (float) (HP * 100 / maxHP);
	}

	public double getHP() {
		return HP;
	}

	public boolean maxHPisZero() {
		return maxHP == 0;
	}

	public void receiveDamage(double d) {
		HP -= d;
		if (HP <= 0) {
			GameMaster.removeEntity(this, false);
			return;
		}
		actionUpdateOverride = true;
	}

	public String toJSON() {
		return "";
	}

	public double getX() {
		return pos.getX();
	}

	public double getY() {
		return pos.getY();
	}

	public void deleteEntity() {
		maxHP = 0;
		deleted = true;
	}

	public boolean isDeleted() {
		return deleted;
	}
}
