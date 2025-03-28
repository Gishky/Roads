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
		double[] result = { -1, -1 };

		double xDiff = xTo - xFrom;
		double yDiff = yTo - yFrom;
		double angle = Math.atan2(yDiff, xDiff);
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
			if (entityDistance <= radSum) {
				consequence.accept(e);
				result[0] = xFrom;
				result[1] = yFrom;
				DebugCreator.createDebugLine(result[0], result[1], e.getX(),
						e.getY(), 50, 255, 0, 0);
				return result;
			}
			if ( (currentDistance != -1 && entityDistance - radSum > currentDistance)) {
				continue;
			}
			double entityAngle = angle - Math.atan2(yDiff, xDiff);
			double rootIn = radSum - Math.pow(entityDistance * Math.sin(entityAngle),2);
			if (rootIn < 0) {
				continue;
			}
			double hitDistance = entityDistance * Math.cos(entityAngle) - Math.sqrt(rootIn);

			if (hitDistance > 0 && distance >= hitDistance
					&& (currentDistance == -1 || hitDistance < currentDistance)) {
				hit = e;
				currentDistance = hitDistance;
			}
		}
		if (currentDistance != -1) {
			consequence.accept(hit);
			result[0] = xFrom + Math.cos(angle) * currentDistance;
			result[1] = yFrom + Math.sin(angle) * currentDistance;
			DebugCreator.createDebugLine(result[0], result[1], hit.getX(),
					hit.getY(), 50, 255, 0, 0);
		}
		return result;
	}

	public double getRadius() {
		return radius;
	}

}
