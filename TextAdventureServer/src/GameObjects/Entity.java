package GameObjects;

import HelperObjects.Position;
import Server.GameMaster;

public class Entity {

	private static int count = 0;
	protected int id;
	protected String entityIdentifier = "entity";

	protected Position pos;
	protected double[] velocity = { 0.0, 0.0 };

	protected double accelleration = 2;
	protected double fallingaccelleration = 2.5;
	protected double jumpforce = 25;
	protected double drag = 1.1;

	protected boolean isGrounded = false;

	public Entity() {
		pos = new Position(World.getWorld().length * Block.size / 2, 0);
		id = count++;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public String action() {
		velocity[0] /= drag;
		if (isGrounded) {
			velocity[0] /= World.getWorld()[(int) pos.getX() / Block.size][(int) pos.getY() / Block.size + 1]
					.getFriction();
		}
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

				velocity[0] -= targetx - p[0];
				velocity[1] -= targety - p[1];

				if (p[1] != targety) {
					isGrounded = true;
				} else {
					isGrounded = false;
				}

				return "entity;" + getId() + ";" + (int) p[0] + ";" + (int) p[1];
			}
		} else {
			isGrounded = false;
			pos.set(targetx, targety);
			return "entity;" + getId() + ";" + (int) targetx + ";" + (int) targety;
		}
		return "";

	}

	public int getId() {
		return id;
	}

	public static int getNextID() {
		return count;
	}

	public String getEntityIdentifier() {
		return entityIdentifier;
	}
}
