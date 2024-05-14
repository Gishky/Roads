package HelperObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.Blocks.Block;
import Window.Panel;

public class Particle {
	private double x;
	private double y;

	private double velocityx;
	private double velocityy;

	private double accellerationx;
	private double accellerationy;

	private int lifetime;

	private Color c;

	public Particle(double x, double y, double velocityx, double velocityy, double accellerationx,
			double accellerationy, Color c) {
		this.x = x * Block.size;
		this.y = y * Block.size;
		this.velocityx = velocityx*Block.size;
		this.velocityy = velocityy*Block.size;
		this.accellerationx = accellerationx*Block.size;
		this.accellerationy = accellerationy*Block.size;
		this.c = c;

		lifetime = new Random().nextInt(15) + 5;
	}

	public Particle(double x, double y, double velocityx, double velocityy, double accellerationx,
			double accellerationy, Color c, int lifetime) {
		this.x = x * Block.size;
		this.y = y * Block.size;
		this.velocityx = velocityx*Block.size;
		this.velocityy = velocityy*Block.size;
		this.accellerationx = accellerationx*Block.size;
		this.accellerationy = accellerationy*Block.size;
		this.c = c;

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

		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), (lifetime < 10 ? lifetime * 25 : 255)));
		g.drawOval((int) x - 1 - cameraX + Panel.windowWidth / 2, (int) y - 1 - cameraY + Panel.windowHeight / 2, 2, 2);

		return false;
	}

	public void setLifetime(int time) {
		lifetime = time;
	}
}
