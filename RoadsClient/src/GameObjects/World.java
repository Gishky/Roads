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

	public static void draw(Graphics2D g, int cameraX, int cameraY) {
		if (world == null)
			return;

		for (int x = 0; x < world.length; x++) {
			for (int y = 0; y < world[0].length; y++) {
				if (world[x][y] != null) {
					world[x][y].draw(x, y, g, cameraX, cameraY);
				}
			}
		}
	}

	public static void createWorld(String widthS, String heightS) {
		int width = Integer.parseInt(widthS);
		int height = Integer.parseInt(heightS);
		world = new Block[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				world[x][y] = new Block();
			}
		}
	}

	public static void setBlock(int x, int y, Block block) {
		if (world != null)
			world[x][y] = block;
	}

}
