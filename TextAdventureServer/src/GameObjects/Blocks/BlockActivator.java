package GameObjects.Blocks;

import java.util.ArrayList;

import GameObjects.World;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import HelperObjects.Position;

public class BlockActivator extends Block {

	private Position lastActivated;

	public BlockActivator() {
		id = 13;
		friction = 2;

		breakable = true;
		breakThreshhold = 5;

		lastActivated = new Position(0, 0);
	}

	public BlockActivator(JSONObject block) {
		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
		id = 13;
		friction = 2;

		breakable = true;
		breakThreshhold = 1;

		lastActivated = new Position(0, 0);
	}

	public void updateBlock() {
		activate(new ArrayList<Block>());
	}

	public void activateAbility(PlayerCharacter e) {
		if (Math.sqrt(Math.pow(e.getMousePosition().getX(), 2) + Math.pow(e.getMousePosition().getY(), 2)) > 4)
			return;

		lastActivated.set(e.getMousePosition().getX(), e.getMousePosition().getY());
		World.getBlock((int) (e.getMousePosition().getX() + e.getPos().getX()),
				(int) (e.getMousePosition().getY() + e.getPos().getY())).activate(new ArrayList<Block>());
	}

	public int getAbilityCooldown() {
		return 10;
	}

	public Block clone() {
		return new BlockActivator();
	}

	public void activate(ArrayList<Block> activationchain) {
		if(activationchain.contains(this))
			return;
		activationchain.add(this);
		
		if (lastActivated.getX() == 0 && lastActivated.getY() == 0)
			return;
		World.getBlock(x + (int) lastActivated.getX(), y + (int) lastActivated.getY()).activate(activationchain);
	}
}
