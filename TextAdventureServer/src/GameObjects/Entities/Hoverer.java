package GameObjects.Entities;

import java.util.LinkedList;
import java.util.Random;

import GameObjects.World;
import GameObjects.Blocks.Block;
import HelperObjects.Hitbox;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

public class Hoverer extends Entity {

	private int damage = 10;
	private double speed = 0.01;
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
		Position pos = new Position(x, World.getCastResultFirst(x, 0, x, World.getWorld().length)[1] - 10);

		this.pos = pos;
		target = player;
		hitBox = new Hitbox(false, 0.13);
		maxHP = 2;
		HP = maxHP;
		createEntity();
	}

	private int actioncount = 0;

	@Override
	public boolean action() {
		if (pos.getX() < 0 || pos.getY() < 0 || pos.getX() > World.getWorld().length
				|| pos.getY() > World.getWorld()[0].length) {
			GameMaster.removeEntity(this, false);
			return false;
		}

		if (target == null || target.isDeleted()) {
			double dist = -1;
			target = null;
			for (Entity e : GameMaster.getEntities()) {
				if (e instanceof PlayerCharacter) {
					double entitydist = Math.sqrt(Math.pow(getX() - e.getX(), 2) + Math.pow(getY() - e.getY(), 2));
					if (target == null || entitydist < dist) {
						dist = entitydist;
						target = (PlayerCharacter) e;
					}
				}
			}
		}

		if (target != null) {
			if (!rush) {
				velocity[0] /= drag;
				velocity[1] /= drag;
			}
			if (World.getCastResultFirst(getX(), getY(), target.getX(), target.getY())[0] != -1) {
				actioncount = 0;
				rush = false;
				pathFind();
			} else {
				nonPathFind();
			}
		}
		return true;
	}

	private boolean rush = false;

	private void nonPathFind() {
		currentTarget = null;
		if (new Random().nextInt(1000) == 1) {
			velocity[0] = 0;
			velocity[1] = 0;
			rush = true;
		}
		if (rush) {
			rush();
			return;
		}
		if (actioncount == 0) {
			actioncount = new Random().nextInt(160);
		}
		double offsetx = Math.sin(actioncount++ / 20) * 5;
		double offsety = Math.cos(actioncount / 6) * 2;
		double distance = Math.sqrt(Math.pow(getX() - target.getX() + offsetx, 2)
				+ Math.pow(getY() - (target.getY() - 3 - offsety / 3), 2));
		velocity[0] += (getX() - target.getX() + offsetx) / distance * speed;
		velocity[1] += (getY() - (target.getY() - 3 - offsety / 3)) / distance * speed;
		double targetx = pos.getX() - velocity[0];
		double targety = pos.getY() - velocity[1];

		double[] castResult = World.getCastResultSlide(getX(), getY(), targetx, targety);
		if (castResult[0] == -1) {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), targetx, targety,
					e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				return;
			}
			pos.set(targetx, targety);
		} else {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), castResult[0], castResult[1],
					e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				return;
			}
			pos.set(castResult[0], castResult[1]);

			velocity[0] += targetx - castResult[0];
			velocity[1] += targety - castResult[1];
		}
	}

	private void rush() {
		actioncount = 0;
		double distance = Math.sqrt(Math.pow(getX() - target.getX(), 2) + Math.pow(getY() - target.getY(), 2));
		System.out.println(id+":"+target.id+" "+target.getX()+"/"+target.getY());
		velocity[0] += (getX() - target.getX()) / distance * speed / 6;
		velocity[1] += (getY() - target.getY()) / distance * speed / 6;
		double targetx = pos.getX() - velocity[0];
		double targety = pos.getY() - velocity[1];

		double[] castResult = World.getCastResultSlide(getX(), getY(), targetx, targety);
		if (castResult[0] == -1) {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), targetx, targety,
					e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				rush = false;
				return;
			}
			pos.set(targetx, targety);
		} else {
			double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), castResult[0], castResult[1],
					e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
			if (hit[0] != -1) {
				pos.set(hit[0], hit[1]);
				GameMaster.removeEntity(this, false);
				rush = false;
				return;
			}
			pos.set(castResult[0], castResult[1]);

			velocity[0] += targetx - castResult[0];
			velocity[1] += targety - castResult[1];

			rush = false;
		}
	}

	private Point currentTarget = null;
	private LinkedList<Point> currentPath = null;

	private void pathFind() {
		if (currentTarget != null && (int) getX() == currentTarget.x && (int) getY() == currentTarget.y) {
			currentTarget = null;
		}
		if (currentTarget == null) {
			if (currentPath == null) {
				double dist = Math.sqrt(Math.pow(getX() - target.getX(), 2) + Math.pow(getY() - target.getY(), 2));
				for (Entity e : GameMaster.getEntities()) {
					if (e instanceof PlayerCharacter) {
						double entitydist = Math.sqrt(Math.pow(getX() - e.getX(), 2) + Math.pow(getY() - e.getY(), 2));
						if (entitydist < dist) {
							dist = entitydist;
							target = (PlayerCharacter) e;
						}
					}
				}
				Point start = new Point((int) getX(), (int) getY(), null);
				int y = (int) (World.getCastResultFirst(target.getX(), target.getY(), target.getX(),
						target.getY() - 3)[1]);
				if (y == -1) {
					y = (int) (target.getY() - 3);
				}
				Point end = new Point((int) target.getX(), y, null);
				currentPath = FindPath(start, end);
				if (currentPath == null) {
					nonPathFind();
					return;
				}
			}
			if (currentPath != null) {
				while (currentPath.size() != 0) {
					Point point = currentPath.get(0);
					double targetx = point.x + 0.5;
					double targety = point.y + 0.5;
					if (World.getCastResultFirst(getX(), getY(), targetx, targety)[0] == -1) {
						currentTarget = point;
						currentPath.remove(0);
						break;
					}
					currentPath.remove(0);
				}
				if (currentPath.size() == 0)
					currentPath = null;
			}
		}
		if (currentTarget != null) {
			double targetx = currentTarget.x + 0.5;
			double targety = currentTarget.y + 0.5;
			double distance = Math.sqrt(Math.pow(getX() - targetx, 2) + Math.pow(getY() - targety, 2));
			velocity[0] += (getX() - targetx) / distance * speed;
			velocity[1] += (getY() - targety) / distance * speed;
			targetx = pos.getX() - velocity[0];
			targety = pos.getY() - velocity[1];
			double[] castResult = World.getCastResultSlide(getX(), getY(), targetx, targety);
			if (castResult[0] == -1) {
				double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), targetx, targety,
						e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
				if (hit[0] != -1) {
					pos.set(hit[0], hit[1]);
					GameMaster.removeEntity(this, false);
					return;
				}
				pos.set(targetx, targety);
			} else {
				double[] hit = hitBox.getEntityCollission(pos.getX(), pos.getY(), castResult[0], castResult[1],
						e -> (!(e instanceof Hoverer) && !e.maxHPisZero()), e -> e.receiveDamage(damage));
				if (hit[0] != -1) {
					pos.set(hit[0], hit[1]);
					GameMaster.removeEntity(this, false);
					return;
				}
				currentTarget = null;
				pos.set(castResult[0], castResult[1]);

				velocity[0] += targetx - castResult[0];
				velocity[1] += targety - castResult[1];
			}
		}
	}

	public static class Point {
		public int x;
		public int y;
		public Point previous;

		public Point(int x, int y, Point previous) {
			this.x = x;
			this.y = y;
			this.previous = previous;
		}

		@Override
		public String toString() {
			return x + "/" + y;
		}

		@Override
		public boolean equals(Object o) {
			Point point = (Point) o;
			return x == point.x && y == point.y;
		}

		public Point offset(int ox, int oy) {
			return new Point(x + ox, y + oy, this);
		}

		public void backTrace(Point p) {
			if (previous != null)
				previous.backTrace(this);
			previous = p;
		}
	}

	public static boolean IsWalkable(Block[][] map, Point point) {
		if (point.y < 0 || point.y > map[0].length - 1)
			return false;
		if (point.x < 0 || point.x > map.length - 1)
			return false;
		return !map[point.x][point.y].isBlocksMovement();
	}

	public static LinkedList<Point> FindNeighbors(Block[][] map, Point point) {
		LinkedList<Point> neighbors = new LinkedList<>();
		Point up = point.offset(0, -1);
		Point down = point.offset(0, 1);
		Point left = point.offset(-1, 0);
		Point right = point.offset(1, 0);
		if (IsWalkable(map, up))
			neighbors.add(0, up);
		if (IsWalkable(map, down))
			neighbors.add(0, down);
		if (IsWalkable(map, left))
			neighbors.add(0, left);
		if (IsWalkable(map, right))
			neighbors.add(0, right);
		return neighbors;
	}

	public static LinkedList<Point> FindPath(Point start, Point end) {
		if (start.equals(end))
			return null;
		boolean finished = false;
		Block[][] map = World.getWorld();
		LinkedList<Point> usedStart = new LinkedList<>();
		LinkedList<Point> usedEnd = new LinkedList<>();
		usedStart.add(start);
		usedEnd.add(end);
		int count = 0;
		while (!finished) {
			if (count++ > 5000) {
				return null;
			}
			LinkedList<Point> newOpenStart = new LinkedList<>();
			LinkedList<Point> newOpenEnd = new LinkedList<>();
			for (Point point : usedStart) {
				for (Point neighbor : FindNeighbors(map, point)) {
					if (!usedStart.contains(neighbor) && !newOpenStart.contains(neighbor)) {
						newOpenStart.add(0, neighbor);
					}
				}
			}
			for (Point point : usedEnd) {
				for (Point neighbor : FindNeighbors(map, point)) {
					if (!usedEnd.contains(neighbor) && !newOpenEnd.contains(neighbor)) {
						newOpenEnd.add(0, neighbor);
					}
				}
			}

			for (Point pointEnd : newOpenEnd) {
				for (Point pointStart : usedStart) {
					if (pointStart.equals(pointEnd)) {
						pointEnd.backTrace(pointStart.previous);
						finished = true;
						break;
					}
				}
				usedEnd.add(0, pointEnd);
				if (finished)
					break;
			}
			if (!finished)
				for (Point point : newOpenStart) {
					usedStart.add(0, point);
				}

			if (!finished && (newOpenEnd.isEmpty() || newOpenStart.isEmpty())) {
				return null;
			}
		}

		LinkedList<Point> path = new LinkedList<>();
		while (end.previous != null) {
			path.add(0, end);
			end = end.previous;
		}
		return path;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("type", "hoverer");
		json.put("id", "" + id);
		json.put("x", String.format("%.4f", getX()));
		json.put("y", String.format("%.4f", getY()));
		json.put("hp%", "" + getHPPercentile());
		json.put("size", "" + hitBox.getRadius());
		json.put("rush", "" + rush);
		return json.getJSON();
	}
}
