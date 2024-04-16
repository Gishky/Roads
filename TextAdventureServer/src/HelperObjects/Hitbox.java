package HelperObjects;

public class Hitbox {
	private boolean isSquare;
	private int radius; // halbe seitenlänge bei square

	public Hitbox(boolean isSquare, int radius) {
		this.radius = radius;
		this.isSquare = isSquare;
	}

	public boolean isHit(Hitbox checkBox, double xOffset, double yOffset) {
		int x = (int) Math.abs(xOffset);
		int y = (int) Math.abs(yOffset);
		if (isSquare) {
			return squareHit(checkBox, x, y);
		} else {
			return circleHit(checkBox, x, y);
		}
	}

	private boolean squareHit(Hitbox checkBox, int xOffset, int yOffset) {
		int radiusSum = radius + checkBox.getRadius();
		if (checkBox.isSquare) {
			return xOffset <= radiusSum && yOffset <= radiusSum;
		} else {
			int cradius = checkBox.getRadius();
			return checkOverlap(cradius, 0, 0, xOffset - radius / 2, yOffset - radius / 2, xOffset + radius / 2,
					yOffset + radius / 2);
		}
	}

	private boolean circleHit(Hitbox checkBox, int xOffset, int yOffset) {
		if (checkBox.isSquare) {
			int sradius = checkBox.getRadius();
			return checkOverlap(radius, 0, 0, xOffset - sradius / 2, yOffset - sradius / 2, xOffset + sradius / 2,
					yOffset + sradius / 2);
		} else {
			return Math.sqrt(Math.pow(xOffset, 2) + Math.pow(yOffset, 2)) <= radius + checkBox.getRadius();
		}
	}

	private boolean checkOverlap(int R, int Xc, int Yc, int X1, int Y1, int X2, int Y2) {
		int Xn = Math.max(X1, Math.min(Xc, X2));
		int Yn = Math.max(Y1, Math.min(Yc, Y2));

		int Dx = Xn - Xc;
		int Dy = Yn - Yc;
		return (Dx * Dx + Dy * Dy) <= R * R;
	}

	public boolean isSquare() {
		return isSquare;
	}

	public int getRadius() {
		return radius;
	}

}
