package GameObjects;

public class Block {
	public static int size = 20;

	protected int id;
	public String blockString = "";

	protected boolean blocksMovement = true;
	protected double friction = 1;
	protected boolean breakable = false;
	protected int breakThreshhold = 1;

	public int getId() {
		return id;
	}

	public double getFriction() {
		return friction;
	}

	public int getBreakThreshhold() {
		return breakThreshhold;
	}

	public Block clone() {
		return null;
	}

	public void activateAbility(Entity e) {

	}

	public int getAbilityCooldown() {
		return 0;
	}
}
