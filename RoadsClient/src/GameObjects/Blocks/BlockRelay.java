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
		setColor(new Color(109, 59, 9));

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
		super.draw(x, y, g, cameraX, cameraY);
		g.setColor(getColor());
		g.fillRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);

		g.setColor(new Color(255, 215, 0));
		if (activated) {
			g.setColor(Color.blue);
			activated = false;
		}
		g.fillRect(x * size - cameraX + Panel.windowWidth / 2 + size * 2 / 6,
				y * size - cameraY + Panel.windowHeight / 2, size * 2 / 6, size);
		g.fillRect(x * size - cameraX + Panel.windowWidth / 2,
				y * size - cameraY + Panel.windowHeight / 2 + size * 2 / 6, size, size * 2 / 6);

		g.setColor(getColor().brighter());
		g.drawRect(x * size - cameraX + Panel.windowWidth / 2, y * size - cameraY + Panel.windowHeight / 2, size, size);
	}

	public void drawInventory(Graphics2D g, int x, int y, int size, boolean selected) {
		g.setColor(getColor());
		g.fillRect(x, y, size, size);

		g.setColor(new Color(255, 215, 0));
		Random r = new Random();
		if (activated) {
			g.setColor(Color.blue);
			activated = false;
		}
		g.fillRect(x + size * 2 / 6, y, size * 2 / 6, size);
		g.fillRect(x, y + size * 2 / 6, size, size * 2 / 6);

		g.setColor(getColor().brighter());
		g.drawRect(x, y, size, size);

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
