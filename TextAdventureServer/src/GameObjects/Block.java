package GameObjects;

public class Block {
	public static int size = 20;

	protected int id;
	public String blockString = "";

	protected boolean blocksMovement = true;
	protected double friction = 1;

	public int getId() {
		return id;
	}

	public double getFriction() {
		return friction;
	}
}
