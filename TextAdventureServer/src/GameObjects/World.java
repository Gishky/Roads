package GameObjects;

import java.util.Random;

import AdminConsole.AdminConsole;
import GameObjects.Blocks.*;
import HelperObjects.OpenSimplex2S;
import Server.GameMaster;
import UDPServer.UDPServer;

public class World {

	private static Block[][] world = new Block[1000][300];

	public static void generateWorld() {
		AdminConsole.log("Generating World with Width " + world.length + " and Height " + world[0].length + "...",
				false);

		int worldHeight = world[0].length;
		Random r = new Random();

		AdminConsole.log("Shaping terrain...", false);
		// funktionen bestimmmen
		int functions = r.nextInt(10) + 20;
		double[] frequencies = new double[functions];
		double[] altitudes = new double[functions];

		for (int i = 0; i < functions; i++) {
			frequencies[i] = r.nextDouble() * 10000 / 100;
			altitudes[i] = r.nextDouble() * frequencies[i] / (300 / 40);
		}

		// höhengeneration
		int[] terrainBegin = new int[world.length];
		int[] terrainHoehe = new int[world.length];
		for (int i = 0; i < world.length; i++) {
			int delta = 0;
			for (int m = 0; m < functions; m++) {
				delta += Math.sin((double) i / frequencies[m]) * altitudes[m];
			}
			terrainBegin[i] = worldHeight / 2 + delta;
			if (terrainBegin[i] >= worldHeight)
				terrainBegin[i] = worldHeight - 1;
			if (terrainBegin[i] <= 0) {
				terrainBegin[i] = 1;
			}
		}

		// glätten
		for (int i = 1; i < world.length - 1; i++) {
			if (terrainBegin[i - 1] - terrainBegin[i] == -(terrainBegin[i] - terrainBegin[i + 1])) {
				terrainBegin[i] = terrainBegin[i - 1];
			}
			terrainHoehe[i] = worldHeight - terrainBegin[i];
		}

		AdminConsole.log("Generating Blocks...", false);
		// fill in blocks
		for (int x = 0; x < world.length; x++) {
			for (int y = 0; y < worldHeight; y++) {
				if (y < terrainBegin[x])
					setBlock(x, y, new BlockAir());
				else {
					if (r.nextInt(2) >= Math.abs(terrainBegin[x] - y)) {
						setBlock(x, y, new BlockGrass());
					} else {
						setBlock(x, y, new BlockDirt());
					}
				}
			}
			if (x != 0 && terrainBegin[x - 1] - terrainBegin[x] >= 3) {
				for (int y = terrainBegin[x]; y <= terrainBegin[x - 1]; y++) {
					setBlock(x, y, new BlockStone());
				}
			} else if (x != 0 && terrainBegin[x] - terrainBegin[x - 1] >= 3) {
				for (int y = terrainBegin[x - 1]; y <= terrainBegin[x]; y++) {
					setBlock(x - 1, y, new BlockStone());
				}
			}
		}

		// generate stone
		generateNoiseBlocks(worldHeight, worldHeight * 3 / 5, 0.6, 1, new BlockStone());
		generateNoiseBlocks(worldHeight / 2 + worldHeight / 5, worldHeight / 4, 0.6, 1, new BlockStone());

		AdminConsole.log("Generating Ores...", false);
		// generate CoalOre
		generateNoiseBlocks(worldHeight * 5 / 8, worldHeight * 3 / 10, 0.25, 4, new BlockCoalOre());

		// generate IronOre
		generateNoiseBlocks(worldHeight * 3 / 4, worldHeight / 2, 0.2, 2.5, new BlockIronOre());

		// generate GoldOre
		generateNoiseBlocks(worldHeight * 6 / 8, worldHeight / 5, 0.1, 4, new BlockGoldOre());

		AdminConsole.log("Generating Caves...", false);
		generateCaves(worldHeight * 5 / 8, worldHeight / 2, 0.3, 20, new BlockAir());
		generateCaves(worldHeight * 6 / 8, worldHeight / 4, 0.1, 50, new BlockAir());

		AdminConsole.log("Generating Trees...", false);
		generateTrees(0.5, 20);

		AdminConsole.log("World successfully Generated", false);
		AdminConsole.log("", true);
	}

	private static void generateTrees(double treeDensity, double forestSize) {
		long seed = new Random().nextLong();
		Random r = new Random();
		for (int x = 0; x < world.length; x++) {
			if (r.nextDouble() <= (OpenSimplex2S.noise2(seed, (double) x / forestSize, 1) + 1) / 2 * treeDensity) {
				int y = getHeight(x);
				if (getBlock(x, y) instanceof BlockGrass) {
					if (getBlock(x - 1, y - 1) instanceof BlockAir)
						setBlock(x - 1, y - 1, new BlockLeaf());
					if (getBlock(x + 1, y - 1) instanceof BlockAir)
						setBlock(x + 1, y - 1, new BlockLeaf());
					if (getBlock(x - 1, y - 2) instanceof BlockAir)
						setBlock(x - 1, y - 2, new BlockLeaf());
					if (getBlock(x + 1, y - 2) instanceof BlockAir)
						setBlock(x + 1, y - 2, new BlockLeaf());
					if (getBlock(x, y - 3) instanceof BlockAir)
						setBlock(x, y - 3, new BlockLeaf());
					setBlock(x, y - 1, new BlockWood());
					setBlock(x, y, new BlockDirt());
					setBlock(x, y - 2, new BlockWood());
				}
			}
		}
	}

	private static void generateNoiseBlocks(double medianHeight, double standardHeightDeviation, double veinDensity,
			double veinSize, Block generatedBlock) {
		long seed = new Random().nextLong();
		for (int x = 0; x < world.length; x++) {
			for (int y = 0; y < world[0].length; y++) {
				double normalDistribution = normalDistribution(y, medianHeight, standardHeightDeviation, veinDensity);
				if (world[x][y].getId() == 0 || world[x][y].getId() == 2)
					continue;
				if (normalDistribution >= (OpenSimplex2S.noise2(seed, (double) x / veinSize, (double) y / veinSize) + 1)
						/ 2) {
					setBlock(x, y, generatedBlock.clone());
				}
			}
		}
	}

	private static void generateCaves(double medianHeight, double standardHeightDeviation, double veinDensity,
			double veinSize, Block generatedBlock) {
		long seed = new Random().nextLong();
		for (int x = 0; x < world.length; x++) {
			for (int y = 0; y < world[0].length; y++) {
				double normalDistribution = normalDistribution(y, medianHeight, standardHeightDeviation, veinDensity);
				if (normalDistribution >= (OpenSimplex2S.noise2(seed, (double) x / veinSize, (double) y / veinSize) + 1)
						/ 2) {
					setBlock(x, y, generatedBlock.clone());
				}
			}
		}
	}

	private static double normalDistribution(int x, double mediam, double sd, double maximum) {
		double max = (Math.exp(-((mediam - mediam) / sd) * ((mediam - mediam) / sd) / 2) / Math.sqrt(2 * Math.PI)) / sd;
		return ((Math.exp(-((x - mediam) / sd) * ((x - mediam) / sd) / 2) / Math.sqrt(2 * Math.PI)) / sd) * maximum
				/ max;
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
		double[] castResult = { -1, -1, -1, -1 };
		double[] result;
		try {
			result = getCastResult(sourceX, sourceY, targetX, targetY, false);
			castResult[0] = result[0];
			castResult[1] = result[1];
			castResult[2] = result[4];
			castResult[3] = result[5];
		} catch (Exception e) {
			AdminConsole.log("Exception: " + e.getMessage(), false);
			for (int i = 0; i < e.getStackTrace().length; i++) {
				String s = "";
				if (i != e.getStackTrace().length - 1)
					s += "├─";
				else
					s += "└─";
				AdminConsole.log(s + e.getStackTrace()[i].toString(), true);
			}
		}

		return castResult;
	}

	public static double[] getCastResultSlide(double sourceX, double sourceY, double targetX, double targetY) {
		double[] castResult = { -1, -1, -1, -1 };
		double[] result;
		try {
			result = getCastResult(sourceX, sourceY, targetX, targetY, true);
			castResult[0] = result[2];
			castResult[1] = result[3];
			castResult[2] = result[4];
			castResult[3] = result[5];
		} catch (Exception e) {
			AdminConsole.log("Exception: " + e.getMessage(), false);
			for (int i = 0; i < e.getStackTrace().length; i++) {
				String s = "";
				if (i != e.getStackTrace().length - 1)
					s += "├─";
				else
					s += "└─";
				AdminConsole.log(s + e.getStackTrace()[i].toString(), true);
			}
		}

		return castResult;
	}

	// casts a ray from source to target and returns hit coordinates if hit, returns
	// -1 if nothing hit
	private static double[] getCastResult(double sourceX, double sourceY, double targetX, double targetY,
			boolean castSliding) throws Exception {

		double[] castResult = { -1, -1, -1, -1, -1, -1 };

		int sourceBlockX = (int) sourceX;
		int targetBlockX = (int) targetX;
		int sourceBlockY = (int) sourceY;
		int targetBlockY = (int) targetY;

		if (world[sourceBlockX][sourceBlockY].isBlocksMovement()) {
			// target started in block. collide immediately.
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

			int cornerModX = velX > 0 ? 1 : 0;
			int cornerModY = velY > 0 ? 1 : 0;

			while (true) {

				if (sourceBlockX + castModifierX == targetBlockX && sourceBlockY + castModifierY == targetBlockY) {
					// arrived at target without hitting a block
					return castResult;
				}

				int cornerX = sourceBlockX + castModifierX + cornerModX;
				int cornerY = sourceBlockY + castModifierY + cornerModY;

				double castCornerY = slope * cornerX + offset;
				boolean isHitFloor = velY > 0 == castCornerY >= cornerY;

				if (isHitFloor)
					castModifierY += unitVelY;
				else
					castModifierX += unitVelX;

				if (world[sourceBlockX + castModifierX][sourceBlockY + castModifierY].isBlocksMovement()) {
					// hit a block
					if (isHitFloor) {
						castResult[1] = sourceBlockY + castModifierY + (velY > 0 ? -0.0001 : 1);
						castResult[0] = (castResult[1] - offset) / slope;

						if (castSliding) {
							double[] straightCast = getStraightCastResult(castResult[0], castResult[1], targetX,
									castResult[1]);
							castResult[2] = straightCast[0];
							castResult[3] = straightCast[1];
						}
					} else {
						castResult[0] = sourceBlockX + castModifierX + (velX > 0 ? -0.0001 : 1);
						castResult[1] = slope * castResult[0] + offset;

						if (castSliding) {
							double[] straightCast = getStraightCastResult(castResult[0], castResult[1], castResult[0],
									targetY);
							castResult[2] = straightCast[0];
							castResult[3] = straightCast[1];
						}
					}
					castResult[4] = sourceBlockX + castModifierX;
					castResult[5] = sourceBlockY + castModifierY;
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

		int sourceBlockX = (int) sourceX;
		int targetBlockX = (int) targetX;
		int sourceBlockY = (int) sourceY;
		int targetBlockY = (int) targetY;

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
					castResult[1] = sourceBlockY + castModifierY + (velY > 0 ? -0.0001 : 1);
					castResult[0] = targetX;
				} else {
					castResult[0] = sourceBlockX + castModifierX + (velX > 0 ? -0.0001 : 1);
					castResult[1] = targetY;
				}
				castResult[2] = sourceBlockX + castModifierX;
				castResult[3] = sourceBlockY + castModifierY;

				return castResult;
			}

		}
	}

	public static void setBlock(int x, int y, Block block) {
		if (world[x][y] != null)
			world[x][y].breakBlock();

		block.setPosition(x, y);
		GameMaster.sendToAll("{action:setBlock,block:" + block.toJSON() + "}", true);
		world[x][y] = block;

		if (x != 0)
			world[x - 1][y].update();
		if (y != 0)
			world[x][y - 1].update();
		if (x != world.length - 1 && world[x + 1][y] != null)
			world[x + 1][y].update();
		if (y != world[0].length - 1 && world[x][y + 1] != null)
			world[x][y + 1].update();
		world[x][y].update();
	}

	public static Block getBlock(int x, int y) {
		if (x >= 0 && x < world.length && y >= 0 && y < world[0].length)
			return world[x][y];
		return new Block();
	}

	public static Block[][] getWorld() {
		return world;
	}
}
