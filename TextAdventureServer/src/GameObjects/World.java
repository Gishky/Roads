package GameObjects;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Random;

public class World {

	private static Block[][] world = new Block[1000][500];

	public static void generateWorld() {
		Random r = new Random();
		int[] höhe = new int[world.length];
		for (int i = 0; i < world.length; i++) {
			if (i == 0)
				höhe[i] = r.nextInt(41) + 100;
			else {
				höhe[i] = höhe[i - 1] + r.nextInt(5) - 2;
				if (höhe[i] >= 200)
					höhe[i] = 199;
			}
		}
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[0].length; j++) {
				if (j < 200 - höhe[i])
					world[i][j] = new BlockAir();
				else {
					int material = r.nextInt(100);
					if (material < j - 80)
						world[i][j] = new BlockStone();
					else
						world[i][j] = new BlockDirt();
				}
				if (200 - j == höhe[i] - 2) {
					world[i][j - 2] = new BlockGrass();
					world[i][j - 1] = new BlockGrass();
					if (i < 999 && höhe[i] - höhe[i + 1] == 2 || i > 0 && höhe[i] - höhe[i - 1] == 2) {
						world[i][j] = new BlockGrass();
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
		world[x][y] = block;
	}

	public static Block[][] getWorld() {
		return world;
	}
}
