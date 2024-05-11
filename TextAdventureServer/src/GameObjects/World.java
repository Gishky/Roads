package GameObjects;

import java.util.Random;

import Server.GameMaster;

public class World {

	private static Block[][] world = new Block[10000][300];

	public static void generateWorld() {
		int worldHeight = world[0].length;
		Random r = new Random();

		// funktionen bestimmmen
		int functions = r.nextInt(10) + 20;
		double[] frequencies = new double[functions];
		double[] altitudes = new double[functions];

		for (int i = 0; i < functions; i++) {
			frequencies[i] = r.nextDouble() * world.length / 100;
			altitudes[i] = r.nextDouble() * frequencies[i] / (world[0].length / 40);
		}

		// höhengeneration
		int[] höhe = new int[world.length];
		for (int i = 0; i < world.length; i++) {
			int delta = 0;
			for (int m = 0; m < functions; m++) {
				delta += Math.sin((double) i / frequencies[m]) * altitudes[m];
			}
			höhe[i] = worldHeight / 2 + delta;
			if (höhe[i] >= worldHeight)
				höhe[i] = worldHeight - 1;
			if (höhe[i] <= 0) {
				höhe[i] = 1;
			}
		}

		// glätten
		for (int i = 1; i < world.length - 1; i++) {
			if (höhe[i - 1] - höhe[i] == -(höhe[i] - höhe[i + 1])) {
				höhe[i] = höhe[i - 1];
			}
		}

		for (int x = 0; x < world.length; x++) {
			for (int y = 0; y < worldHeight; y++) {
				if (y < worldHeight - höhe[x])
					setBlock(x, y, new BlockAir());
				else {
					if (r.nextInt(worldHeight) < y / 2)
						setBlock(x, y, new BlockStone());
					else {
						if (r.nextInt(worldHeight) < y / 5)
							if (r.nextInt(worldHeight) < y / 2)
								setBlock(x, y, new BlockIronOre());
							else
								setBlock(x, y, new BlockCoalOre());
						else
							setBlock(x, y, new BlockDirt());
					}

					if (r.nextInt(2) >= Math.abs(worldHeight - höhe[x] - y)) {
						setBlock(x, y, new BlockGrass());
					}
				}
			}
			if (x != 0 && höhe[x] - höhe[x - 1] >= 3) {
				for (int y = worldHeight - höhe[x]; y <= worldHeight - höhe[x - 1]; y++) {
					setBlock(x, y, new BlockStone());
				}
			} else if (x != 0 && höhe[x - 1] - höhe[x] >= 3) {
				for (int y = worldHeight - höhe[x - 1]; y <= worldHeight - höhe[x]; y++) {
					setBlock(x - 1, y, new BlockStone());
				}
			}
		}
	}

	public static int getHeight(int x) {
		for (int i = 0; i < world[x].length; i++) {
			if (world[x][i].id != 0) {
				return i;
			}
		}
		return 0;
	}

	// casts a ray from source to target and returns hit coordinates if hit, returns
	// -1 if nothing hit
	public static double[] getCastResult(double xSource, double ySource, double xTarget, double yTarget) {
		double[] p = { -1, 0 };
		try {
			if (world[(int) (xTarget / Block.size)][(int) (yTarget / Block.size)].blocksMovement) {
				p[0] = xSource;
				p[1] = ySource;
				int counter = 100;
				while (counter-- > 0) {
					boolean stay = false;
					if (world[(int) (xSource / Block.size)][(int) (p[1] / Block.size)].blocksMovement == false) {
						p[0] = xSource;
						if (Math.abs(xTarget - xSource) > 1) {
							xSource += (xTarget > xSource ? 1 : -1);
							stay = true;
						} else {
							xSource = xTarget;
							stay = true;
						}
					}
					if (world[(int) (p[0] / Block.size)][(int) (ySource / Block.size)].blocksMovement == false) {
						p[1] = ySource;
						if (Math.abs(yTarget - ySource) > 1) {
							ySource += (yTarget > ySource ? 1 : -1);
							stay = true;
						} else {
							ySource = yTarget;
							stay = true;
						}
					}
					if (!stay)
						break;
				}
			}
		} catch (Exception e) {
		}

		return p;
	}

	public static void setBlock(int x, int y, Block block) {
		block.setPosition(x, y);
		block.blockString = "block;" + x + ";" + y + ";" + block.getId();
		GameMaster.sendToAll(block.blockString, true);
		world[x][y] = block;

		world[x][y].update();
		if (x != 0)
			world[x - 1][y].update();
		if (y != 0)
			world[x][y - 1].update();
		if (x != world.length - 1 && world[x + 1][y] != null)
			world[x + 1][y].update();
		if (y != world[0].length - 1 && world[x][y + 1] != null)
			world[x][y + 1].update();
	}

	public static Block getBlock(int x, int y) {
		try {
			return world[x][y];
		} catch (Exception e) {
		}
		return null;
	}

	public static Block[][] getWorld() {
		return world;
	}
}
