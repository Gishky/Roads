package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import GameObjects.World;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import HelperObjects.Position;
import Window.Panel;

public class BlockRelay extends Block {

	private ArrayList<Position> activationList = new ArrayList<Position>();

	public BlockRelay(JSONObject block) {
		setColor(BlockWood.getDefaultColor().brighter());

		if (block == null)
			return;

		String pos;
		int count = 0;
		while ((pos = block.get("" + count++)) != null) {
			activationList.add(new Position(Integer.parseInt(pos.split("/")[0]), Integer.parseInt(pos.split("/")[1])));
		}
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		Color c = BlockGold.getDefaultColor();
		if (activated) {
			c = Color.blue;
			activated = false;
		}
		drawGrain(g, c, x, y, size, 20);

		g.setColor(getColor());
		drawPixel(g, x, y, 0, 0, size * 2);
		drawPixel(g, x, y, 0, 2, size * 2);
		drawPixel(g, x, y, 2, 0, size * 2);
		drawPixel(g, x, y, 2, 2, size * 2);
		g.setColor(darkerColor(getColor(), 10));
		drawPixel(g, x, y, 1, 4, size);
		drawPixel(g, x, y, 5, 1, size);
		g.setColor(brighterColor(getColor(), 10));
		drawPixel(g, x, y, 1, 1, size);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		super.drawInventory(g, x, y, size, selected);

		Random r = new Random();
		if (World.playerInventory[World.selectedInventory] == this) {
			for (int block = 0; block < activationList.size(); block++) {
				Position activationBlock = activationList.get(block);
				for (int i = 0; i < 3; i++) {
					Panel.addParticle(new Particle(activationBlock.getX() + r.nextDouble() * Block.size,
							activationBlock.getY() + r.nextDouble() * Block.size, 0, 0, 0, 0,
							interpolateColor(Color.BLUE, Color.yellow, r.nextDouble()), 12));
				}
			}
		}
	}
}
