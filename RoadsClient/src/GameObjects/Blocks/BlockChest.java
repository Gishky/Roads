package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Font;
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
		super.draw(x, y, g, cameraX, cameraY);

		x = x * size - cameraX + Panel.windowWidth / 2;
		y = y * size - cameraY + Panel.windowHeight / 2;

		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		g.setColor(getColor().darker());
		g.drawRect(x, y + size * 2 / 5, size, size / 5);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);

		if (inventory != null) {
			inventory.drawInventory(g, x + size / 4, y + size / 4, size / 2, false);
			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.PLAIN, size / 3));
			g.drawString("" + inventorySize, x + size - 2 - g.getFontMetrics().stringWidth(inventorySize + ""),
					y + size - 4);
		}
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		g.setColor(getColor().darker());
		g.drawRect(x, y + size * 2 / 5, size, size / 5);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);

		if (inventory != null) {
			inventory.drawInventory(g, x + size / 4, y + size / 4, size / 2, false);

			g.setColor(Color.black);
			g.setFont(new Font("Arial", Font.PLAIN, size / 3));
			g.drawString("" + inventorySize, x + size - 2 - g.getFontMetrics().stringWidth(inventorySize + ""),
					y + size - 4);
		}
	}
}
