package GameObjects.Entities;

import java.util.LinkedList;
import java.util.Random;

import GameObjects.World;
import HelperObjects.Hitbox;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class Hoverer extends Entity {

	private int damage = 0;
	private double speed = 0.1;
	private PlayerCharacter target = null;

	public Hoverer(PlayerCharacter player) {
		Random r = new Random();
		double x = r.nextDouble() * 20;
		x += 10;
		if (r.nextBoolean()) {
			x *= -1;
		}
		x += player.getX();
		if (x < 0 || x > World.getWorld().length) {
			x = 0;
			deleteEntity();
		}
		Position pos = new Position(x, player.getY() - 10);

		this.pos = pos;
		target = player;
		hitBox = new Hitbox(false, 0.13);
		maxHP = 2;
		HP = maxHP;
		createEntity();
	}

	@Override
	public boolean action() {
		if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() > World.getWorld().length
				|| pos.getY() > World.getWorld()[0].length) {
			GameMaster.removeEntity(this, false);
			return false;
		}

		if (World.getCastResultFirst(getX(), getY(), target.getX(), target.getY())[0] != -1) {
			pathFind();
			return true;
		} else {
			double distance = Math.sqrt(Math.pow(getX() - target.getX(), 2) + Math.pow(getY() - target.getY(), 2));
			velocity[0] = (getX() - target.getX()) / distance * speed;
			velocity[1] = (getY() - target.getY()) / distance * speed;
			double targetx = pos.getX() - velocity[0];
			double targety = pos.getY() - velocity[1];

			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), targetx, targety, e -> (!e.maxHPisZero()),
					e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				return true;
			}
			pos.set(targetx, targety);

		}

		return true;
	}

	private void pathFind() {
		int targetx = (int) target.getX();
		int targety = (int) (World.getCastResultFirst(target.getX(), target.getY(), target.getX(),
				target.getY() - 3)[1]);
		if (targety == -1) {
			targety = (int) (target.getY() - 3);
		}

		int[][] map = new int[World.getWorld().length][World.getWorld()[0].length];
		LinkedList<int[]> queue = new LinkedList<>();
		int[] source = { (int) getX(), (int) getY(), 1 };
		queue.add(source);
		int count = 0;
		int[][] neighbours = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		while (queue.size() > 0 && count < 10000) {
			count++;
			int[] pos = queue.get(0);
			if (map[pos[0]][pos[1]] == 0 || map[pos[0]][pos[1]] > pos[2]) {
				map[pos[0]][pos[1]] = pos[2];

				if (pos[0] == targetx && pos[1] == targety) {
					while (pos[2] > 0) {
						for (int[] neighbour : neighbours) {
							int mapheight = map[pos[0] + neighbour[0]][pos[1] + neighbour[1]];
							if (mapheight == pos[2] - 1) {
								if (mapheight == 1) {
									System.out.println("moving");
									double x = pos[0] + 0.5;
									double y = pos[1] + 0.5;
									double distance = Math.sqrt(Math.pow(getX() - x, 2) + Math.pow(getY() - y, 2));
									velocity[0] = (getX() - x) / distance * speed;
									velocity[1] = (getY() - y) / distance * speed;
									double targetxb = getX() - velocity[0];
									double targetyb = getY() - velocity[1];

									double[] hit = hitBox.getEntityCollission(getX(), getY(), targetxb, targetyb,
											e -> (!e.maxHPisZero()), e -> e.receiveDamage(damage));
									if (hit[0] != -1) {
										this.pos.set(hit[0], hit[1]);
										GameMaster.removeEntity(this, false);
										return;
									}
									this.pos.set(targetxb, targetyb);
									return;
								}
								pos[0] += neighbour[0];
								pos[1] += neighbour[1];
								pos[2] -= 1;
							}
						}
					}
					System.out.println("didnt move");
				}

				for (int[] neighbour : neighbours) {
					if (pos[0] + neighbour[0] < 0 || pos[0] + neighbour[0] >= map.length || pos[1] + neighbour[1] < 0
							|| pos[1] + neighbour[1] >= map[0].length) {
					} else if (World.getBlock(pos[0] + neighbour[0], pos[1] + neighbour[1]).isBlocksMovement()) {
						map[pos[0] + neighbour[0]][pos[1] + neighbour[1]] = -1;
					} else {
						int[] newpos = { pos[0] + neighbour[0], pos[1] + neighbour[1], pos[2] + 1 };
						queue.add(newpos);
					}
				}
			}
			queue.remove(0);
		}
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "chomper");
		json.put("id", "" + id);
		json.put("x", String.format("%.4f", getX()));
		json.put("y", String.format("%.4f", getY()));
		json.put("hp%", "" + getHPPercentile());
		json.put("size", "" + hitBox.getRadius());
		return json.getJSON();
	}
}
