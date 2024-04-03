package GameObjects;

import HelperObjects.Position;
import Server.GameMaster;

public class Entity {

	private static int count = 0;
	protected int id;

	protected Position pos;
	protected double[] velocity = { 0.0, 0.0 };

	protected double accelleration = 2;
	protected double fallingaccelleration = 2.5;
	protected double jumpforce = 25;
	protected double drag = 1.1;

	protected boolean isGrounded = false;

	public Entity() {
		pos = new Position();
		id = count++;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public void action() {
		velocity[0] /= drag;
		velocity[1] /= drag;
		velocity[1] += fallingaccelleration;
		double targety = pos.getY() + velocity[1];
		double targetx = pos.getX() + velocity[0];
		double[] p = World.getCastResult(pos.getX(), pos.getY(), targetx, targety);
		if (p[0] != -1) {
			if (p[0] == pos.getX() && p[1] == pos.getY()) {
				isGrounded = true;
			} else {
				pos.set(p[0], p[1]);
				GameMaster.entityPositionUpdate(this);
				if (p[0] != targetx) {
					velocity[0] = 0;
				}
				if (p[1] != targety) {
					velocity[1] = 0;
					isGrounded = true;
				} else {
					isGrounded = false;
				}
			}
		} else {
			isGrounded = false;
			pos.set(targetx, targety);
			GameMaster.entityPositionUpdate(this);
		}

	}

	public int getId() {
		return id;
	}

	public static int getNextID() {
		return count;
	}

}
