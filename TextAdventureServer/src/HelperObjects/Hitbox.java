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

		double xDiff = xTo - xFrom;
		double yDiff = yTo - yFrom;
		double angle = Math.atan2(yDiff,xDiff);
		double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

		for (Entity e : GameMaster.getEntities()) {
			if (e.getHitBox() == this)
				continue;

			if (!filter.test(e)) {
				continue;
			}

			xDiff = e.getPos().getX() - xFrom;
			yDiff = e.getPos().getY() - yFrom;
			double entityDistance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
			double radSum = radius + e.getHitBox().getRadius();
			if (entityDistance - radSum > distance || (currentDistance != -1 && entityDistance - radSum > currentDistance)) {
				continue;
			}

			double entityAngle = Math.atan2(yDiff, xDiff);
			double angleDiff = Math.abs(entityAngle - angle);
			double sinAngleDiff = Math.sin(angleDiff);

			double sinBeta = sinAngleDiff * entityDistance / radSum;

			double beta = Math.asin(sinBeta);
			double hitDistance = entityDistance * Math.sin(180 - angleDiff - beta) / Math.sin(beta);

			if (distance >= hitDistance && (currentDistance == -1 || hitDistance < currentDistance)) {
				currentDistance = hitDistance;
				hit = e;
			}
		}
		double[] result = { -1, -1 };
		if (currentDistance != -1) {
			consequence.accept(hit);
			result[0] = xFrom + Math.cos(angle) * currentDistance;
			result[1] = yFrom + Math.sin(angle) * currentDistance;
		}
		return result;
	}

	public double getRadius() {
		return radius;
	}

}
