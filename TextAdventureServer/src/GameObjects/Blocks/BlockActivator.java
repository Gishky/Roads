package GameObjects.Blocks;

import java.util.ArrayList;

import GameObjects.World;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import HelperObjects.Position;
import Server.GameMaster;

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
		id = 13;
		friction = 2;

		breakable = true;
		breakThreshhold = 1;

		lastActivated = new Position(0, 0);

		if(block==null)
			return;

		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
	}

	public void updateBlock() {
		activate(new ArrayList<Block>());
	}

	public void activateAbility(PlayerCharacter e) {
		lastActivated.set(e.getMousePosition().getX() + e.getPos().getX(),
				e.getMousePosition().getY() + e.getPos().getY());
		World.getBlock((int) lastActivated.getX(), (int) lastActivated.getY()).activate(new ArrayList<Block>());

		e.updateInventory();
	}

	public int getAbilityCooldown() {
		return 10;
	}

	public Block clone() {
		return new BlockActivator();
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", "" + id);
		json.put("x", "" + getX());
		json.put("y", "" + getY());
		json.put("act", (int) lastActivated.getX() + "/" + (int) lastActivated.getY());
		return json.getJSON();
	}

	public void setPosition(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public void activate(ArrayList<Block> activationchain) {
		if (activationchain.contains(this))
			return;
		activationchain.add(this);

		if ((int) lastActivated.getX() == x && (int) lastActivated.getY() == y)
			return;
		World.getBlock((int) lastActivated.getX(), (int) lastActivated.getY()).activate(activationchain);
		GameMaster.sendToAll("{action:activate,x:" + x + ",y:" + y + "}", true);
	}
}
