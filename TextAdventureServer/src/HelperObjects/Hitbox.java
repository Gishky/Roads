package HelperObjects;

import java.util.ArrayList;

import GameObjects.Entities.Entity;
import Server.GameMaster;

public class Hitbox {

	private double radius;

	public Hitbox(boolean isSquare, double radius) {
		this.radius = radius;
	}

	public ArrayList<Entity> getEntityCollissions(double xFrom, double yFrom, double xTo, double yTo) {
		ArrayList<Entity> hitEntities = new ArrayList<Entity>();
		ArrayList<Double> hitEntitiesDistances = new ArrayList<Double>();
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
			xDiff = e.getPos().getY() - xFrom;
			yDiff = e.getPos().getX() - yFrom;
			double entityDistance = Math
					.sqrt(Math.pow(e.getPos().getX() - xFrom, 2) + Math.pow(e.getPos().getY() - yFrom, 2));
			if (entityDistance >= distance + radius + e.getHitBox().getRadius())
				continue;

			double maxAngleDiff = Math.atan((radius + e.getHitBox().getRadius()) / entityDistance);

			xDiff = e.getPos().getY() - xFrom;
			yDiff = e.getPos().getX() - yFrom;
			double entityAngle = Math.atan(yDiff / xDiff);
			if (xDiff < 0) {
				entityAngle += Math.PI;
			}
			double angleDiff = (angle - entityAngle + Math.PI * 3) % (Math.PI * 2) - Math.PI;

			if (angleDiff < maxAngleDiff) {
				double gamma = Math.asin(distance * Math.sin(angleDiff) / (radius + e.getHitBox().getRadius()))
						+ Math.PI / 2;
				double alpha = 180 - gamma - angleDiff;
				double hitDistance = Math.sin(alpha) * distance / Math.sin(gamma);
				if (distance >= hitDistance) {
					for (int i = 0; i <= hitEntitiesDistances.size(); i++) {
						if (i == hitEntitiesDistances.size()) {
							hitEntitiesDistances.add(hitDistance);
							hitEntities.add(e);
							break;
						} else if (hitEntitiesDistances.get(i) > hitDistance) {
							hitEntitiesDistances.add(i, hitDistance);
							hitEntities.add(i, e);
							break;
						}
					}
				}
			}
		}
		return hitEntities;
	}

	public double getRadius() {
		return radius;
	}

}
