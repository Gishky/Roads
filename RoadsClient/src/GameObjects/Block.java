package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

public class Block {
	protected static int size = 20;

	protected int breakThreshhold = 0;

	protected Color c;

	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {

	}

	public static Block getBlockFromID(String id) {
		switch (id) {
		case "0":
			return new BlockAir();
		case "1":
			return new BlockDirt();
		case "2":
			return new BlockGrass();
		case "3":
			return new BlockStone();
		default:
			return null;
		}
	}

	public Color getColor() {
		return c;
	}
}
