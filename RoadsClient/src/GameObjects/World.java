package GameObjects;

import java.awt.Graphics2D;

import Window.Panel;

public class World {

	private static Block[][] world;

	public static int cameraX, cameraY;
	public static int playerid = -1;

	public static Block[][] getWorld() {
		return world;
	}

	public static void setWorld(Block[][] world) {
		World.world = world;
	}

	public static void draw(Graphics2D g, int cameraX, int cameraY) {
		if (world == null)
			return;

		for (int x = (cameraX - Panel.windowWidth / 2) / Block.size - 2; x <= (cameraX + Panel.windowWidth / 2)
				/ Block.size + 2; x++) {
			if (x >= world.length)
				break;
			else if (x < 0)
				continue;
			for (int y = (cameraY - Panel.windowHeight / 2) / Block.size - 2; y <= (cameraY + Panel.windowHeight / 2)
					/ Block.size + 2; y++) {
				if (y >= world[0].length)
					break;
				else if (y < 0)
					continue;
				if (world[x][y] != null) {
					world[x][y].draw(x, y, g, cameraX, cameraY);
				} else {
					Panel.getServerConnection().sendMessage("block;" + x + ";" + y, true);
				}
			}
		}
	}

	public static void createWorld(int width, int height) {
		world = new Block[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				world[x][y] = null;
			}
		}
	}

	public static void setBlock(int x, int y, Block block) {
		if (world != null)
			world[x][y] = block;
	}

	public static double getBlockBreakCount(int x, int y) {
		int biggestCount = 0;
		for (int i = 0; i < Panel.getEntities().size(); i++) {
			Entity e = Panel.getEntities().get(i);
			if (e.getBreakCount() > biggestCount && (int) (e.getPos().getX() / Block.size) == x
					&& (int) (e.getPos().getY() / Block.size) == y - 1) {
				biggestCount = e.getBreakCount();
			}
		}
		return biggestCount;
	}

}
