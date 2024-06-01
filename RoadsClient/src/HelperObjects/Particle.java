package HelperObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import Window.Panel;

public class Particle {
	private double x;
	private double y;
	private double size;

	private double velocityx;
	private double velocityy;

	private double accellerationx;
	private double accellerationy;

	private int lifetime;

	private Color c;

	public Particle(double x, double y, double velocityx, double velocityy, double accellerationx,
			double accellerationy, Color c) {
		this.x = x;
		this.y = y;
		this.velocityx = velocityx;
		this.velocityy = velocityy;
		this.accellerationx = accellerationx;
		this.accellerationy = accellerationy;
		this.c = c;
		size = 0.1 * Block.size;

		lifetime = new Random().nextInt(15) + 5;
	}

	public Particle(double x, double y, double velocityx, double velocityy, double accellerationx,
			double accellerationy, Color c, int lifetime) {
		this.x = x;
		this.y = y;
		this.velocityx = velocityx;
		this.velocityy = velocityy;
		this.accellerationx = accellerationx;
		this.accellerationy = accellerationy;
		this.c = c;
		size = 0.1 * Block.size;

		this.lifetime = lifetime;
	}

	public Particle(double x, double y, double velocityx, double velocityy, double accellerationx,
			double accellerationy, Color c, int lifetime, double size) {
		this.x = x;
		this.y = y;
		this.velocityx = velocityx;
		this.velocityy = velocityy;
		this.accellerationx = accellerationx;
		this.accellerationy = accellerationy;
		this.c = c;
		this.size = 0.1 * Block.size * size;
		if (size < 1)
			size = 1;

		this.lifetime = lifetime;
	}

	public boolean draw(Graphics2D g, int cameraX, int cameraY) {
		lifetime--;
		if (lifetime <= 0) {
			return true;
		}

		velocityx += accellerationx;
		velocityy += accellerationy;
		x += velocityx;
		y += velocityy;

		if ((int) size <= 0)
			return false;

		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (lifetime < 10 ? lifetime * 25 : 255)));
		g.fillRect(((int) x - (int) size / 2) / (int) size * (int) size - cameraX + Panel.windowWidth / 2,
				((int) y - (int) size / 2) / (int) size * (int) size - cameraY + Panel.windowHeight / 2, (int) size,
				(int) size);

		return false;
	}

	public void setLifetime(int time) {
		lifetime = time;
	}
}
