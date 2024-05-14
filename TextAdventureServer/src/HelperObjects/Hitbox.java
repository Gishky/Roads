package HelperObjects;

public class Hitbox {
	private boolean isSquare;
	private double radius; // halbe seitenlänge bei square

	public Hitbox(boolean isSquare, double radius) {
		this.radius = radius;
		this.isSquare = isSquare;
	}

	public boolean isHit(Hitbox checkBox, double xOffset, double yOffset) {
		double x = Math.abs(xOffset);
		double y = Math.abs(yOffset);
		if (isSquare) {
			return squareHit(checkBox, x, y);
		} else {
			return circleHit(checkBox, x, y);
		}
	}

	private boolean squareHit(Hitbox checkBox, double xOffset, double yOffset) {
		double radiusSum = radius + checkBox.getRadius();
		if (checkBox.isSquare) {
			return xOffset <= radiusSum && yOffset <= radiusSum;
		} else {
			double cradius = checkBox.getRadius();
			return checkOverlap(cradius, 0, 0, xOffset - radius / 2, yOffset - radius / 2, xOffset + radius / 2,
					yOffset + radius / 2);
		}
	}

	private boolean circleHit(Hitbox checkBox, double xOffset, double yOffset) {
		if (checkBox.isSquare) {
			double sradius = checkBox.getRadius();
			return checkOverlap(radius, 0, 0, xOffset - sradius / 2, yOffset - sradius / 2, xOffset + sradius / 2,
					yOffset + sradius / 2);
		} else {
			return Math.sqrt(Math.pow(xOffset, 2) + Math.pow(yOffset, 2)) <= radius + checkBox.getRadius();
		}
	}

	private boolean checkOverlap(double R, double Xc, double Yc, double X1, double Y1, double X2, double Y2) {
		double Xn = Math.max(X1, Math.min(Xc, X2));
		double Yn = Math.max(Y1, Math.min(Yc, Y2));

		double Dx = Xn - Xc;
		double Dy = Yn - Yc;
		return (Dx * Dx + Dy * Dy) <= R * R;
	}

	public boolean isSquare() {
		return isSquare;
	}

	public double getRadius() {
		return radius;
	}

}
