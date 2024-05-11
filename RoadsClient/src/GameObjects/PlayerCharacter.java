package GameObjects;

import java.awt.Color;
import java.awt.Graphics2D;

import Window.Panel;

public class PlayerCharacter extends Entity {

	public PlayerCharacter(String id, String x, String y, String hppercent) {
		super(id, x, y, hppercent);
	}

	@Override
	public void draw(Graphics2D g, int cameraX, int cameraY) {
		int breakMaximum = 20;
		double breakPercentile = (double) breakCount / breakMaximum;
		if (breakPercentile > 1) {
			breakPercentile = 1;
		}
		int ymod = (int) (breakPercentile * 14) - 4;
		if (ymod <= 0)
			ymod = 0;
		int xmod = ymod / 2;
		if (pos.getX() % Block.size > Block.size / 2) {
			xmod *= -1;
		}

		g.setColor(Color.black);
		if (heldBlock != null) {
			g.setColor(heldBlock.getColor().darker());
		}
		g.fillOval(pos.getX() - 5 - cameraX + Panel.windowWidth / 2 + xmod,
				pos.getY() - 5 - cameraY + Panel.windowHeight / 2 + ymod, 10, 10);

		if (breakCount <= 0)
			return;

		int height = (int) (Block.size * breakPercentile);
		if (height <= 0)
			return;
		int[] xPoints = new int[Block.size + 2];
		xPoints[0] = (pos.getX() / Block.size) * Block.size - cameraX + Panel.windowWidth / 2 + Block.size;
		xPoints[1] = (pos.getX() / Block.size) * Block.size - cameraX + Panel.windowWidth / 2;
		for (int x = 2; x < xPoints.length; x++) {
			xPoints[x] = xPoints[1] + (x - 2) * (Block.size / (xPoints.length - 2));
		}
		int[] yPoints = new int[Block.size + 2];
		yPoints[0] = (pos.getY() / Block.size + 1) * Block.size - cameraY + Panel.windowHeight / 2;
		yPoints[1] = (pos.getY() / Block.size + 1) * Block.size - cameraY + Panel.windowHeight / 2;
		for (int x = 2; x < xPoints.length; x++) {
			yPoints[x] = yPoints[1] + height
					+ (int) (Math
							.sin(System.currentTimeMillis()
									+ (double) x / ((double) 3 + Math.sin(System.currentTimeMillis() / 100 % 500)))
							* 3);
		}

		g.fillPolygon(xPoints, yPoints, xPoints.length);

		super.draw(g, cameraX, cameraY);
	}

}
