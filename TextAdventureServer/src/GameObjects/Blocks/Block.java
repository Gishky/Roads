package GameObjects.Blocks;

import java.util.ArrayList;
import java.util.LinkedList;

import GameObjects.World;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import Server.GameMaster;

public class Block {

	protected int id;

	private boolean blocksMovement = true;
	protected double friction = 1;
	protected boolean breakable = false;
	protected int breakThreshhold = 1;
	protected int x, y;

	protected LinkedList<Block> inventory = null;
	protected Block smeltedBlock;
	protected int requiredFuelForSmelting;
	protected int fuelValue = 0;

	private long abilityTime = 0;

	private int[] directionToPlayer = { 0, 0 };
	private long distanceToPlayer = Long.MAX_VALUE;
	private int pathFindID = 0;
	private PlayerCharacter pathToPlayer = null;

	public Block() {
		this(null);
	}

	public Block(JSONObject block) {
		id = -1;
	}

	public int getId() {
		return id;
	}

	public void scheduleUpdate() {
		GameMaster.updateBlock(this);
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

	public void activateAbility(PlayerCharacter e) {

	}

	protected int getAbilityCooldown() {
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

	public void update() {

	}

	public void inventoryUpdate(PlayerCharacter e) {

	}

	public void activate(ArrayList<Block> activationchain) {
		if (activationchain.contains(this))
			return;
		activationchain.add(this);
	}

	public boolean isBreakable() {
		return breakable;
	}

	public LinkedList<Block> getInventory() {
		return inventory;
	}

	public void setInventory(LinkedList<Block> inventory) {
		this.inventory = inventory;
	}

	protected boolean canAbilityActivate() {
		if (System.currentTimeMillis() >= abilityTime) {
			abilityTime = System.currentTimeMillis() + getAbilityCooldown();
			return true;
		}
		return false;
	}

	public long getAbilityTime() {
		return abilityTime;
	}

	public void setAbilityTime(long abilityTime) {
		this.abilityTime = abilityTime;
	}

	public int[] getDirectionToPlayer() {
		return directionToPlayer;
	}

	public void setDirectionToPlayer(int[] directionToPlayer) {
		this.directionToPlayer = directionToPlayer;
	}

	public Block getNextPathBlock() {
		return World.getBlock(x + directionToPlayer[0], y + directionToPlayer[0]);
	}

	public long getDistanceToPlayer() {
		return distanceToPlayer;
	}

	public void setDistanceToPlayer(long distanceToPlayer) {
		this.distanceToPlayer = distanceToPlayer;
	}

	public int getPathFindID() {
		return pathFindID;
	}

	public void setPathFindID(int pathFindID) {
		this.pathFindID = pathFindID;
	}

	public PlayerCharacter getPathToPlayer() {
		return pathToPlayer;
	}

	public void setPathToPlayer(PlayerCharacter pathToPlayer) {
		this.pathToPlayer = pathToPlayer;
	}

	public Block popInventory() {
		if (inventory.size() != 0) {
			Block b = inventory.pop();
			World.setBlock(x, y, this);
			return b;
		}
		return null;
	}

	public void setDirectionToPlayer(int x, int y) {
		directionToPlayer[0] = x;
		directionToPlayer[1] = y;
	}
}
