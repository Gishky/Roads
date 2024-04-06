package GameObjects;

import java.awt.Graphics2D;

public class World {

	private static Block[][] world;

	public static int cameraX, cameraY;
	public static int playerid = 0;

	public static Block[][] getWorld() {
		return world;
	}

	public static void setWorld(Block[][] world) {
		World.world = world;
	}

	public static void draw(Graphics2D g) {
		if (world == null)
			return;

		int cameraX = World.cameraX;
		int cameraY = World.cameraY;
		for (int x = 0; x < world.length; x++) {
			for (int y = 0; y < world[0].length; y++) {
				if (world[x][y] != null) {
					world[x][y].draw(x, y, g, cameraX, cameraY);
				}
			}
		}
	}

	public static void createWorld(String width, String height) {
		world = new Block[Integer.parseInt(width)][Integer.parseInt(height)];
		for (Block[] c : world) {
			for (Block b : c) {
				b = new BlockAir();
			}
		}
	}

	public static void setBlock(int x, int y, Block block) {
		world[x][y] = block;
	}

}
