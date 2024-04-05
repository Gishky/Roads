package GameObjects;

import java.util.Random;

public class World {

	private static Block[][] world = new Block[1000][100];

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
					if (material < j - 30)
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

	// casts a ray from source to target and returns hit coordinates if hit, returns
	// target if nothing hit
	public static double[] getCastResult(double xSource, double ySource, double xTarget, double yTarget) {
		double[] p = { -1, 0 };
		try {
			if (world[(int) xTarget / Block.size][(int) yTarget / Block.size].blocksMovement) {
				p[0] = xSource;
				p[1] = ySource;
				int counter = 10;
				while (counter-- > 0) {
					boolean stay = false;
					if (world[(int) xSource / Block.size][(int) p[1] / Block.size].blocksMovement == false) {
						if (Math.abs(xTarget - xSource) > 1) {
							p[0] = (int) xSource;
							xSource += (xTarget > xSource ? 1 : -1);
							stay = true;
						} else {
							p[0] = xSource;
							xSource = xTarget;
							stay = true;
						}
					}
					if (world[(int) p[0] / Block.size][(int) ySource / Block.size].blocksMovement == false) {
						if (Math.abs(yTarget - ySource) > 1) {
							p[1] = (int) ySource;
							ySource += (yTarget > ySource ? 1 : -1);
							stay = true;
						} else {
							p[1] = ySource;
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
		world[x][y] = block;
	}

	public static Block[][] getWorld() {
		return world;
	}
}
