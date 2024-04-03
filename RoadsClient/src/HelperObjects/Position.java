package HelperObjects;

public class Position {
	private int x, y;

	public Position() {
		x = 0;
		y = 0;
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Position(String x, String y) {
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
	}

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
