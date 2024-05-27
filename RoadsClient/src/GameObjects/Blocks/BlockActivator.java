package GameObjects.Blocks;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import GameObjects.World;
import HelperObjects.JSONObject;
import HelperObjects.Particle;
import HelperObjects.Position;
import Window.Panel;

public class BlockActivator extends Block {

	private Position activationBlock = null;

	public BlockActivator(JSONObject block) {
		setColor(new Color(109, 59, 9));

		if (block == null)
			return;
		String pos = block.get("act");
		activationBlock = new Position(Integer.parseInt(pos.split("/")[0]), Integer.parseInt(pos.split("/")[1]));
	}

	@Override
	public void draw(int x, int y, Graphics2D g, int cameraX, int cameraY) {
		super.draw(x, y, g, cameraX, cameraY);
		g.setColor(getColor());
		g.fillRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);

		g.setColor(new Color(255, 215, 0));
		if(activated) {
			g.setColor(Color.blue);
			activated = false;
		}
		g.fillRect(x * size - cameraX + Panel.windowWidth / 2 + size * 2 / 6,
				y * size - cameraY + Panel.windowHeight / 2 + size * 2 / 6, size * 2 / 6, size * 2 / 6);

		g.setColor(getColor().brighter());
		g.drawRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		g.setColor(new Color(255, 215, 0));
		if(activated) {
			g.setColor(Color.blue);
			activated = false;
		}
		g.fillRect(x + size * 2 / 6, y + size * 2 / 6, size * 2 / 6, size * 2 / 6);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);

		if (World.playerInventory[World.selectedInventory] == this) {
			Random r = new Random();
			for (int i = 0; i < 3; i++) {
				Panel.addParticle(new Particle(activationBlock.getX() + r.nextDouble() * Block.size,
						activationBlock.getY() + r.nextDouble() * Block.size, 0, 0, 0, 0,
						interpolateColor(Color.BLUE, Color.yellow, r.nextDouble()), 12));
			}
		}
	}
}
