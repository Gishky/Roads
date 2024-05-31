package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import HelperObjects.JSONObject;
import Window.Panel;

public class BlockChest extends Block {

	private int inventorySize = 0;
	private Block inventory = null;

	public BlockChest(JSONObject block) {
		setColor(new Color(109, 59, 9).brighter());

		if (block == null)
			return;

		inventorySize = Integer.parseInt(block.get("inventorysize"));
		if (inventorySize > 0)
			inventory = Block.getBlockFromID(block.get("inventory"), null);
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		if (inventory != null) {
			inventory.draw(x, y, g, cameraX, cameraY);
		}

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(getColor());
		g.fillRect(x, y, size / 6, size);
		g.fillRect(x + size * 5 / 6, y, size / 6, size);

		g.setColor(BlockAir.getSkyColor(x, y));
		g.fillRect(x + size / 6, y, size * 4 / 6, size * (100 - inventorySize) / 100);

		g.setColor(getColor().darker());
		g.fillRect(x, y + size * 2 / 6, size, size / 6);
		drawPixel(g, x, y, 3, 3, size);
		g.fillRect(x, y + size * 4 / 6, size, size / 6);

		g.setColor(darkerColor(getColor(), 20));
		drawPixel(g, x, y, 4, 4, size);
		drawPixel(g, x, y, 2, 4, size);
		drawPixel(g, x, y, 3, 2, size);
		drawPixel(g, x, y, 1, 2, size);
		drawPixel(g, x, y, 2, 3, size);
		drawPixel(g, x, y, 0, 5, size);
		drawPixel(g, x, y, 0, 3, size);
		drawPixel(g, x, y, 5, 0, size);

		if (inventory != null) {
			inventory.drawInventory(g, x + size / 4, y + size / 4, size / 2, false);
		}

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);
	}

}
