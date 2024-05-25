package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import HelperObjects.Particle;
import Window.Panel;

public class World {

	private static Block[][] world;

	public static double cameraX, cameraY;
	public static int playerid = -1;
	public static Block[] playerInventory = new Block[5];
	public static int selectedInventory = 0;

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
		if (world == null)
			return;

		if (World.getWorld()[x][y] != null && !block.getClass().equals(world[x][y].getClass())) {
			Random r = new Random();
			Color c = World.getWorld()[x][y].getColor().brighter();
			for (int i = 0; i < 50; i++) {
				Particle p = new Particle(x * Block.size + r.nextDouble() * Block.size,
						y * Block.size + r.nextDouble() * Block.size, 0, 0,
						r.nextDouble() * 0.025 * Block.size - 0.015 * Block.size, r.nextDouble() * 0.025 * Block.size,
						c);
				p.setLifetime(r.nextInt(10) + 3);
				Panel.addParticle(p);
			}
		}

		world[x][y] = block;
	}

}
