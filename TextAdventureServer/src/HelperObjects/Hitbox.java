package HelperObjects;

import java.util.function.Consumer;
import java.util.function.Predicate;

import GameObjects.Entities.Entity;
import Server.GameMaster;

public class Hitbox {

	private double radius;

	public Hitbox(boolean isSquare, double radius) {
		this.radius = radius;
	}

	public double[] getEntityCollission(double xFrom, double yFrom, double xTo, double yTo, Predicate<Entity> filter,
			Consumer<Entity> consequence) {
		Entity hit = null;
		double currentDistance = -1;
		double hitangle = 0;

		double xDiff = xTo - xFrom;
		double yDiff = yTo - yFrom;
		double angle = Math.atan(yDiff / xDiff);
		double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
		if (xDiff < 0) {
			angle += Math.PI;
		}

		for (Entity e : GameMaster.getEntities()) {
			if (e.getHitBox() == this)
				continue;

			if (!filter.test(e)) {
				continue;
			}

			xDiff = e.getPos().getY() - xFrom;
			yDiff = e.getPos().getX() - yFrom;
			double entityDistance = Math
					.sqrt(Math.pow(e.getPos().getX() - xFrom, 2) + Math.pow(e.getPos().getY() - yFrom, 2));
			double radSum = radius + e.getHitBox().getRadius();
			if (currentDistance != -1 && entityDistance - radSum > currentDistance) {
				continue;
			}
			System.out.println("got here");

			double entityAngle = Math.atan(yDiff / xDiff);
			if (xDiff < 0) {
				entityAngle += Math.PI;
			}
			double angleDiff = (angle - entityAngle + Math.PI * 3) % (Math.PI * 2) - Math.PI;

			double sinangle = Math.sin(angleDiff);

			double c = sinangle * entityDistance / radSum;
			if (c < -1 || 1 < c) {
				continue;
			}

			double gamma = Math.asin(c) - angleDiff;
			double hitDistance = Math.sin(gamma) * radSum / sinangle;

			if (distance >= hitDistance && (currentDistance == -1 || hitDistance < currentDistance)) {
				System.out.println("hit");
				currentDistance = hitDistance;
				hitangle = entityAngle;
				hit = e;
			}
		}
		double[] result = { -1, -1 };
		if (currentDistance != -1) {
			System.out.println("consuming");
			consequence.accept(hit);
			result[0] = xFrom + Math.cos(hitangle) * currentDistance;
			result[1] = yFrom + Math.sin(hitangle) * currentDistance;
		}
		return result;
	}

	public double getRadius() {
		return radius;
	}

}
