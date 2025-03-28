package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import GameObjects.World;
import HelperObjects.JSONObject;
import HelperObjects.OpenSimplex2S;
import Window.Panel;

public class BlockFogged extends Block{
	private static final long windSeed = 61493765;

	public BlockFogged() {
		setColor(Color.black);
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);

		drawGrain(g, getSkyColor(x, y), x * size - cameraX + Panel.windowWidth / 2,
				y * size - cameraY + Panel.windowHeight / 2, size, 2);
	}

	public static Color getSkyColor(int x, int y) {
		double time = (double) System.currentTimeMillis() / 1000;
		double wind = OpenSimplex2S.noise2(windSeed,
				((double) x / (10 + Math.sin(time / 100)) + Math.sin(time) * Math.sin(y) / 10) - time / 7,
				((double) y / (10 + Math.sin(time / 100)) + Math.sin(time / 2) * Math.sin(y) / 10));
		int windmod = Math.max(Math.min((int) ((wind + 1) * 5 / ((double) (1 + y) / 200)), 55), 0);
		Color c = new Color(0 + windmod, 0 + windmod, 0 + windmod);
		return c;
	}
}
