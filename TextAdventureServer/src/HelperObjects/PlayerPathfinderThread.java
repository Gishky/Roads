package HelperObjects;

import java.util.LinkedList;

import GameObjects.World;
import GameObjects.Blocks.Block;
import GameObjects.Entities.PlayerCharacter;

public class PlayerPathfinderThread extends Thread {

	private static int idcount = 0;
	private int startx, starty;
	private int id;
	private PlayerCharacter player;

	private boolean running = true;

	public PlayerPathfinderThread(int x, int y, PlayerCharacter player) {
		startx = x;
		starty = y;
		id = idcount++;
		this.player = player;
		this.start();
	}

	@Override
	public void run() {
		LinkedList<int[]> used = new LinkedList<>();
		Block[][] map = World.getWorld();
		int[] start = { startx, starty };
		used.add(start);

		map[start[0]][start[1]].setDirectionToPlayer(0, 0);
		map[start[0]][start[1]].setPathFindID(id);
		map[start[0]][start[1]].setPathToPlayer(player);
		map[start[0]][start[1]].setDistanceToPlayer(0);
		while (running) {
			LinkedList<int[]> foundNeighbours = new LinkedList<>();
			while (used.size() != 0) {
				int[] current = used.get(0);
				for (int[] neighbour : FindNeighbors(map, current)) {
					Block neighbourBlock = map[neighbour[0]][neighbour[1]];
					Block block = map[current[0]][current[1]];
					if (neighbourBlock.getNextPathBlock() == block) {
						if (neighbourBlock.getDistanceToPlayer() < block.getDistanceToPlayer())
							neighbourBlock.setDistanceToPlayer(block.getDistanceToPlayer());
					} else {
						if (!foundNeighbours.contains(neighbour)) {
							foundNeighbours.add(neighbour);
							neighbourBlock.setDirectionToPlayer(neighbour[0] - current[0], neighbour[1] - current[1]);
							neighbourBlock.setDistanceToPlayer(block.getDistanceToPlayer() + 1);
							neighbourBlock.setPathFindID(id);
							neighbourBlock.setPathToPlayer(player);
						}
					}
				}
				used.remove(0);
			}

			if (foundNeighbours.size() == 0) {
				running = false;
			}

			for (int[] block : foundNeighbours) {
				Block b = map[block[0]][block[1]];
				used.add(block);
			}
		}
	}

	public boolean IsWalkable(Block[][] map, int[] block) {
		if (block[1] < 0 || block[1] > map[0].length - 1)
			return false;
		if (block[0] < 0 || block[0] > map.length - 1)
			return false;
		return !map[block[0]][block[1]].isBlocksMovement();
	}

	public LinkedList<int[]> FindNeighbors(Block[][] map, int[] block) {
		LinkedList<int[]> neighbors = new LinkedList<>();
		int[] up = { block[0], block[1] - 1 };
		int[] down = { block[0], block[1] + 1 };
		int[] left = { block[0] - 1, block[1] };
		int[] right = { block[0] + 1, block[1] };
		if (IsWalkable(map, left)
				&& (map[left[0]][left[1]].getDistanceToPlayer() - 1 > map[block[0]][block[1]].getDistanceToPlayer()
						|| map[left[0]][left[1]].getPathToPlayer() == map[block[0]][block[1]].getPathToPlayer()
						|| map[left[0]][left[1]].getPathToPlayer().isDeleted())
				&& map[left[0]][left[1]].getPathFindID() != map[block[0]][block[1]].getPathFindID())
			neighbors.add(0, left);
		if (IsWalkable(map, right)
				&& (map[right[0]][right[1]].getDistanceToPlayer() - 1 > map[block[0]][block[1]].getDistanceToPlayer()
						|| map[right[0]][right[1]].getPathToPlayer() == map[block[0]][block[1]].getPathToPlayer()
						|| map[right[0]][right[1]].getPathToPlayer().isDeleted())
				&& map[right[0]][right[1]].getPathFindID() != map[block[0]][block[1]].getPathFindID())
			neighbors.add(0, right);
		if (IsWalkable(map, up)
				&& (map[up[0]][up[1]].getDistanceToPlayer() - 1 > map[block[0]][block[1]].getDistanceToPlayer()
						|| map[up[0]][up[1]].getPathToPlayer() == map[block[0]][block[1]].getPathToPlayer()
						|| map[up[0]][up[1]].getPathToPlayer().isDeleted())
				&& map[up[0]][up[1]].getPathFindID() != map[block[0]][block[1]].getPathFindID())
			neighbors.add(0, up);
		if (IsWalkable(map, down)
				&& (map[down[0]][down[1]].getDistanceToPlayer() - 1 > map[block[0]][block[1]].getDistanceToPlayer()
						|| map[down[0]][down[1]].getPathToPlayer() == map[block[0]][block[1]].getPathToPlayer()
						|| map[down[0]][down[1]].getPathToPlayer().isDeleted())
				&& map[down[0]][down[1]].getPathFindID() != map[block[0]][block[1]].getPathFindID())
			neighbors.add(0, down);
		return neighbors;
	}

	public boolean isDone() {
		return !running;
	}
}
