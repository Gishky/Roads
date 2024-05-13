package GameObjects;

import GameObjects.Blocks.Block;
import HelperObjects.Hitbox;
import HelperObjects.Position;
import Server.GameMaster;

public class Entity {

	protected static int count = 0;
	protected int id;
	protected String entityIdentifier = "entity";

	protected Position pos;
	protected double[] velocity = { 0.0, 0.0 };

	protected double accelleration = 2;
	protected double fallingaccelleration = 2.5;
	protected double jumpforce = 25;
	protected double drag = 1.1;

	protected Block heldBlock = null;
	protected int breakCount = 0;
	protected int maxHP = 0;
	protected int HP;
	protected Hitbox hitBox = new Hitbox(false, 0);

	protected boolean isGrounded = false;
	protected Position mouse;

	protected String parameters = "";

	public Entity(String identifier) {
		entityIdentifier = identifier;
		pos = new Position();
		pos.setX(World.getWorld().length * Block.size / 2 + Block.size / 2);
		pos.setY(World.getHeight((int) (pos.getX() / Block.size)) * Block.size - Block.size / 2);
		id = count++;
		HP = maxHP;
		GameMaster.addEntity(this);
	}

	public Entity() {
	};

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	private boolean actionUpdateOverride = false;

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
		double[] castResult = World.getCastResultSlide(pos.getX(), pos.getY(), targetx, targety);
		if (castResult[0] != -1) {
			isGrounded = castResult[1] < targety;
			if (castResult[0] == pos.getX() && castResult[1] == pos.getY()) {
				velocity[0] = 0;
				velocity[1] = 0;
			} else {
				if ((int) (pos.getX() / Block.size) != (int) (castResult[0] / Block.size)) {
					breakCount = 0;
				}
				pos.set(castResult[0], castResult[1]);

				velocity[0] -= targetx - castResult[0];
				velocity[1] -= targety - castResult[1];

				if (x != (int) pos.getX() || y != (int) pos.getY())
					return true;
			}
		} else {
			isGrounded = false;
			breakCount = 0;
			pos.set(targetx, targety);
			return true;
		}

		if (actionUpdateOverride) {
			actionUpdateOverride = false;
			return true;
		}
		return false;
	}

	public void receiveDamage(int damage) {
		HP -= damage;
		if (HP <= 0) {
			GameMaster.removeEntity(this);
			return;
		}
		actionUpdateOverride = true;
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

	public Hitbox getHitBox() {
		return hitBox;
	}

	public int getHP() {
		return HP;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getHPPercentile() {
		if (maxHP == 0)
			return 100;
		return (int) (HP * 100 / maxHP);
	}

	public Block getHeldBlock() {
		return heldBlock;
	}

	public int getHeldBlockId() {
		if (heldBlock != null)
			return heldBlock.getId();
		return -1;
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

	public String getParameters() {
		if (parameters.equals("")) {
			return "empty";
		}
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}
