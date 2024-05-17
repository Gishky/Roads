package GameObjects.Blocks;

import GameObjects.Entity;
import HelperObjects.JSONObject;

public class Block {

	protected int id;

	private boolean blocksMovement = true;
	protected double friction = 1;
	protected boolean breakable = false;
	protected int breakThreshhold = 1;
	private int x;

	private int y;

	protected Block smeltedBlock;
	protected int requiredFuelForSmelting;
	protected int fuelValue = 0;

	public Block() {
		id = -1;
	}

	public int getId() {
		return id;
	}

	public void update() {

	}

	public void breakBlock() {

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
		this.setX(x);
		this.setY(y);
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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isBlocksMovement() {
		return blocksMovement;
	}

	public void setBlocksMovement(boolean blocksMovement) {
		this.blocksMovement = blocksMovement;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", "" + id);
		json.put("x", "" + getX());
		json.put("y", "" + getY());
		return json.getJSON();
	}
}
