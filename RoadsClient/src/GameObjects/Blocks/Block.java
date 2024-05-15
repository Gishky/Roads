package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Window.Panel;

public class Block {
	public static int size = 40;

	protected int breakThreshhold = 0;

	private Color c;

	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		/*if (x % 40 == 0 && y % 10 == 0) {

			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.drawString((x * size - 100) + "", x * size - cameraX + Panel.windowWidth / 2 - 100,
					y * size - cameraY + Panel.windowHeight / 2 - 100);

			g.drawString((y * size - 100) + "", x * size - cameraX + Panel.windowWidth / 2 - 100,
					y * size - cameraY + Panel.windowHeight / 2 - 100 + 15);
		}*/
	}

	public static Block getBlockFromID(String id) {
		switch (id) {
		case "0":
			return new BlockAir();
		case "1":
			return new BlockDirt();
		case "2":
			return new BlockGrass();
		case "3":
			return new BlockStone();
		case "4":
			return new BlockOven();
		case "5":
			return new BlockCoalOre();
		case "6":
			return new BlockIronOre();
		case "7":
			return new BlockIron();
		default:
			return null;
		}
	}

	public Color getColor() {
		return getC();
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
	}
}
