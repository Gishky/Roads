package HelperObjects;

import GameObjects.Blocks.Block;
import Window.Panel;

public class Position {
	private double x, y;

	public Position() {
		x = 0;
		y = 0;
	}

	public Position(double x, double y) {
		this.x = x * Block.size;
		this.y = y * Block.size;
	}

	public Position(String x, String y) {
		this.x = Double.parseDouble(x) * Block.size;
		this.y = Double.parseDouble(y) * Block.size;
	}
	
	public void set(String x, String y) {
		this.x = Double.parseDouble(x) * Block.size;
		this.y = Double.parseDouble(y) * Block.size;
	}

	public void set(double x, double y) {
		this.x = x * Block.size;
		this.y = y * Block.size;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x * Block.size;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y * Block.size;
	}
	
	public void setX(String x) {
		this.x = Double.parseDouble(x) * Block.size;
	}

	public void setY(String y) {
		this.y = Double.parseDouble(y) * Block.size;
	}

	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}

}
