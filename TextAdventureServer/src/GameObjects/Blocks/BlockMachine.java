package GameObjects.Blocks;

import java.util.ArrayList;

import GameObjects.World;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import Server.GameMaster;

public class BlockMachine extends Block {

	private int dirx = 0;
	private int diry = -1;

	public BlockMachine() {
		id = 14;
		friction = 2;

		breakable = true;
		breakThreshhold = 5;
	}

	public BlockMachine(JSONObject block) {
		id = 14;
		friction = 2;

		breakable = true;
		breakThreshhold = 1;

		if (block == null)
			return;

		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
	}

	public void activateAbility(PlayerCharacter e) {
		if (Math.abs(e.getMousePosition().getX()) > Math.abs(e.getMousePosition().getY())) {
			dirx = (e.getMousePosition().getX() > 0 ? 1 : -1);
			diry = 0;
		} else {
			diry = (e.getMousePosition().getY() > 0 ? 1 : -1);
			dirx = 0;
		}
		e.updateInventory();
	}

	public int getAbilityCooldown() {
		return 0;
	}

	public Block clone() {
		return new BlockMachine();
	}

	public void activate(ArrayList<Block> activationchain) {
		if(activationchain.contains(this))
			return;
		activationchain.add(this);
		
		if (World.getBlock(x + dirx, y + diry).isBlocksMovement())
			if (pushBlock(x + dirx, y + diry, 5)) {
				World.setBlock(x + dirx, y + diry, new BlockAir());
			}
		GameMaster.sendToAll("{action:activate,x:" + x + ",y:" + y + "}", true);
	}

	private boolean pushBlock(int x, int y, int force) {
		if (!World.getBlock(x + dirx, y + diry).isBlocksMovement()) {
			World.setBlock(x + dirx, y + diry, World.getBlock(x, y));
			return true;
		} else {
			if (force > 0)
				if (pushBlock(x + dirx, y + diry, force - 1)) {
					World.setBlock(x + dirx, y + diry, World.getBlock(x, y));
					return true;
				}
		}

		return false;
	}

	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", "" + id);
		json.put("x", "" + getX());
		json.put("y", "" + getY());
		json.put("dir", "" + (dirx != 0 ? dirx + 2 : 0 + diry != 0 ? diry + 3 : 0));
		return json.getJSON();
	}
}
