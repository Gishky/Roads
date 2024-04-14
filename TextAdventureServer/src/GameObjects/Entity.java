package GameObjects;

import HelperObjects.Position;

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

	protected int breakCount = 0;

	protected boolean isGrounded = false;

	public Entity() {
		pos = new Position();
		pos.setX(World.getWorld().length * Block.size / 2 + Block.size / 2);
		pos.setY(World.getHeight((int) (pos.getX() / Block.size)) * Block.size - Block.size / 2);
		id = count++;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	public boolean action() {
		int x = (int) pos.getX();
		int y = (int) pos.getY();
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
				velocity[0] = 0;
				velocity[1] = 0;
			} else {
				if ((int) (pos.getX() / Block.size) != (int) (p[0] / Block.size)) {
					breakCount = 0;
				}
				pos.set(p[0], p[1]);

				velocity[0] -= targetx - p[0];
				velocity[1] -= targety - p[1];

				if (p[1] < targety) {
					isGrounded = true;
				} else {
					isGrounded = false;
				}

				if (x != (int) pos.getX() || y != (int) pos.getY())
					return true;
			}
		} else {
			isGrounded = false;
			breakCount = 0;
			pos.set(targetx, targety);
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

	public String getEntityIdentifier() {
		return entityIdentifier;
	}

	public int getBreakCount() {
		return breakCount;
	}
}
