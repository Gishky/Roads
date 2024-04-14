package GameObjects;

import java.util.Random;

import Server.GameMaster;

public class World {

	private static Block[][] world = new Block[10000][300];

	public static void generateWorld() {
		int worldHeight = world[0].length;
		Random r = new Random();
		int[] höhe = new int[world.length];
		for (int i = 0; i < world.length; i++) {
			if (i == 0)
				höhe[i] = worldHeight / 2;
			else {
				höhe[i] = höhe[i - 1] + r.nextInt(5) - 2;
				if (höhe[i] >= worldHeight)
					höhe[i] = 99;
				else if (höhe[i] <= 0)
					höhe[i] = 1;
			}
		}
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < worldHeight; j++) {
				if (j < worldHeight - höhe[i])
					setBlock(i, j, new BlockAir());
				else {
					int material = r.nextInt(worldHeight);
					if (material < j / 1.5)
						setBlock(i, j, new BlockStone());
					else
						setBlock(i, j, new BlockDirt());

					if (r.nextInt(2) >= Math.abs(worldHeight - höhe[i] - j)) {
						setBlock(i, j, new BlockGrass());
					}
				}
			}
		}
	}

	public static int getHeight(int x) {
		for (int y = 0; y < world[x].length; y++) {
			if (world[x][y].blocksMovement == true) {
				return y;
			}
		}
		return -1;
	}

	// casts a ray from source to target and returns hit coordinates if hit, returns
	// -1 if nothing hit
	public static double[] getCastResult(double xSource, double ySource, double xTarget, double yTarget) {
		double[] p = { -1, 0 };
		try {
			if (world[(int) (xTarget / Block.size)][(int) (yTarget / Block.size)].blocksMovement) {
				p[0] = xSource;
				p[1] = ySource;
				int counter = 50;
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
		block.blockString = "block;" + x + ";" + y + ";" + block.getId();
		GameMaster.sendToAll(block.blockString, true);
		world[x][y] = block;
	}

	public static Block getBlock(int x, int y) {
		return world[x][y];
	}

	public static Block[][] getWorld() {
		return world;
	}
}
