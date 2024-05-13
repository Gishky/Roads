package GameObjects;

import java.util.Random;

import GameObjects.Blocks.Block;
import GameObjects.Blocks.BlockAir;
import GameObjects.Blocks.BlockCoalOre;
import GameObjects.Blocks.BlockDirt;
import GameObjects.Blocks.BlockGrass;
import GameObjects.Blocks.BlockIronOre;
import GameObjects.Blocks.BlockStone;
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
			if (world[x][i].getId() != 0) {
				return i;
			}
		}
		return 0;
	}

	public static double[] getCastResultFirst(double sourceX, double sourceY, double targetX, double targetY) {
		double[] castResult = new double[4];
		double[] result = getCastResult(sourceX, sourceY, targetX, targetY, false);

		castResult[0] = result[0];
		castResult[1] = result[1];
		castResult[2] = result[4];
		castResult[3] = result[5];

		return castResult;
	}

	public static double[] getCastResultSlide(double sourceX, double sourceY, double targetX, double targetY) {
		double[] castResult = new double[4];
		double[] result = getCastResult(sourceX, sourceY, targetX, targetY, true);

		castResult[0] = result[2];
		castResult[1] = result[3];
		castResult[2] = result[4];
		castResult[3] = result[5];

		return castResult;
	}

	// casts a ray from source to target and returns hit coordinates if hit, returns
	// -1 if nothing hit
	private static double[] getCastResult(double sourceX, double sourceY, double targetX, double targetY,
			boolean castSliding) {
		double[] castResult = { -1, -1, -1, -1, -1, -1 };

		int sourceBlockX = (int) sourceX / Block.size;
		int targetBlockX = (int) targetX / Block.size;
		int sourceBlockY = (int) sourceY / Block.size;
		int targetBlockY = (int) targetY / Block.size;

		if (world[sourceBlockX][sourceBlockY].isBlocksMovement()) {
			castResult[0] = sourceX;
			castResult[1] = sourceY;
			castResult[2] = sourceX;
			castResult[3] = sourceY;
			castResult[4] = sourceBlockX;
			castResult[5] = sourceBlockY;
		}

		double velX = targetX - sourceX;
		double velY = targetY - sourceY;
		int unitVelX = velX > 0 ? 1 : -1;
		int unitVelY = velY > 0 ? 1 : -1;

		if (velX == 0 || velY == 0) {
			double[] straightCast = getStraightCastResult(sourceX, sourceY, targetX, targetY);
			if (straightCast[0] != targetX || straightCast[1] != targetY) {
				castResult[0] = straightCast[0];
				castResult[1] = straightCast[1];
				castResult[2] = straightCast[0];
				castResult[3] = straightCast[1];
				castResult[4] = straightCast[2];
				castResult[5] = straightCast[3];
			}
			return castResult;
		} else {
			double slope = velY / velX;
			double offset = sourceY - slope * sourceX;

			int castModifierX = 0;
			int castModifierY = 0;

			int cornerModX = velX > 0 ? Block.size : 0;
			int cornerModY = velY > 0 ? Block.size : 0;

			while (true) {
				if (sourceBlockX + castModifierX == targetBlockX && sourceBlockY + castModifierY == targetBlockY) {
					// arrived at target without hitting a block
					return castResult;
				}

				int cornerX = (sourceBlockX + castModifierX) * Block.size + cornerModX;
				int cornerY = (sourceBlockY + castModifierY) * Block.size + cornerModY;
				double castCornerY = slope * cornerX + offset;

				boolean isLastHitFloor = false;
				if (velY > 0) {
					if (castCornerY >= cornerY) {
						isLastHitFloor = true;
						castModifierY += unitVelY;
					} else {
						castModifierX += unitVelX;
					}
				} else {
					if (castCornerY >= cornerY) {
						castModifierX += unitVelX;
					} else {
						isLastHitFloor = true;
						castModifierY += unitVelY;
					}
				}

				if (world[sourceBlockX + castModifierX][sourceBlockY + castModifierY].isBlocksMovement()) {
					// hit a block
					if (isLastHitFloor) {
						castResult[1] = (sourceBlockY + castModifierY) * Block.size + (velY > 0 ? -0.0001 : Block.size);
						castResult[0] = (castResult[1] - offset) / slope;

						double[] straightCast = getStraightCastResult(castResult[0], castResult[1], targetX,
								castResult[1]);

						if (castSliding) {
							castResult[2] = straightCast[0];
							castResult[3] = straightCast[1];
						}
						castResult[4] = sourceBlockX + castModifierX;
						castResult[5] = sourceBlockY + castModifierY;
					} else {
						castResult[0] = (sourceBlockX + castModifierX) * Block.size + (velX > 0 ? -0.0001 : Block.size);
						castResult[1] = slope * castResult[0] + offset;

						if (castSliding) {
							double[] straightCast = getStraightCastResult(castResult[0], castResult[1], castResult[0],
									targetY);
							castResult[2] = straightCast[0];
							castResult[3] = straightCast[1];
						}
						castResult[4] = sourceBlockX + castModifierX;
						castResult[5] = sourceBlockY + castModifierY;
					}

					return castResult;
				}
			}
		}
	}

	private static double[] getStraightCastResult(double sourceX, double sourceY, double targetX, double targetY) {

		double velX = targetX - sourceX;
		double velY = targetY - sourceY;
		int unitVelX = velX == 0 ? 0 : velX < 0 ? -1 : 1;
		int unitVelY = velY == 0 ? 0 : velY < 0 ? -1 : 1;

		int sourceBlockX = (int) sourceX / Block.size;
		int targetBlockX = (int) targetX / Block.size;
		int sourceBlockY = (int) sourceY / Block.size;
		int targetBlockY = (int) targetY / Block.size;

		double[] castResult = { targetX, targetY, targetBlockX, targetBlockY };

		int castModifierX = 0;
		int castModifierY = 0;

		while (true) {
			if (sourceBlockX + castModifierX == targetBlockX && sourceBlockY + castModifierY == targetBlockY) {
				// arrived at target without hitting a block
				return castResult;
			}

			castModifierX += unitVelX;
			castModifierY += unitVelY;

			if (world[sourceBlockX + castModifierX][sourceBlockY + castModifierY].isBlocksMovement()) {
				// hit a block
				if (unitVelX == 0) {
					castResult[1] = (sourceBlockY + castModifierY) * Block.size + (velY > 0 ? -0.0001 : Block.size);
					castResult[0] = targetX;
					castResult[2] = sourceBlockX + castModifierX;
					castResult[3] = sourceBlockY + castModifierY;
				} else {
					castResult[0] = (sourceBlockX + castModifierX) * Block.size + (velX > 0 ? -0.0001 : Block.size);
					castResult[1] = targetY;
					castResult[2] = sourceBlockX + castModifierX;
					castResult[3] = sourceBlockY + castModifierY;
				}

				return castResult;
			}

		}
	}

	public static void setBlock(int x, int y, Block block) {
		world[x][y].breakBlock();
		
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
