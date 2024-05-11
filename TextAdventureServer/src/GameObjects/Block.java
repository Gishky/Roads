package GameObjects;

public class Block {
	public static int size = 20;

	protected int id;
	public String blockString = "";

	protected boolean blocksMovement = true;
	protected double friction = 1;
	protected boolean breakable = false;
	protected int breakThreshhold = 1;
	protected int x, y;

	protected Block smeltedBlock;
	protected int requiredFuelForSmelting;
	protected int fuelValue = 0;

	public int getId() {
		return id;
	}

	public void update() {

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

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Block getSmeltedBlock() {
		return smeltedBlock;
	}

	public int getFuelValue() {
		return fuelValue;
	}

	public void setFuelValue(int value) {
		this.fuelValue = value;
	}

	public int getRequiredFuelForSmelting() {
		return requiredFuelForSmelting;
	}

	public void setRequiredFuelForSmelting(int value) {
		requiredFuelForSmelting = value;
	}
}
