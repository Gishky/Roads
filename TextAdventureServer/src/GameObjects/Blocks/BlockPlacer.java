package GameObjects.Blocks;

import java.util.ArrayList;
import java.util.LinkedList;

import AdminConsole.AdminConsole;
import AdminConsole.MessageInterpreter;
import GameObjects.World;
import GameObjects.Entities.PlayerCharacter;
import HelperObjects.JSONObject;
import Server.GameMaster;

public class BlockPlacer extends Block {

	private int dirx = 0;
	private int diry = -1;

	public BlockPlacer() {
		id = 18;
		friction = 2;

		breakable = true;
		breakThreshhold = 20;
	}

	public BlockPlacer(JSONObject block) {
		id = 18;
		friction = 2;

		breakable = true;
		breakThreshhold = 20;

		inventory = new LinkedList<Block>();

		if (block == null)
			return;

		if (block.get("x") != null)
			setX(Integer.parseInt(block.get("x")));
		if (block.get("y") != null)
			setY(Integer.parseInt(block.get("y")));
	}

	public Block clone() {
		return new BlockPlacer();
	}

	public void update() {
		if (!place)
			return;
		place = false;

		Block from = World.getBlock(x + dirx, y + diry);
		Block to = World.getBlock(x - dirx, y - diry);
		if (from.getInventory() != null && from.getInventory().size() != 0 && !to.isBlocksMovement()) {
			Block b = from.popInventory();
			World.setBlock(x - dirx, y - diry, b);
		}

	}

	private boolean place = false;

	@Override
	public void activate(ArrayList<Block> activationchain) {
		Block from = World.getBlock(x + dirx, y + diry);
		Block to = World.getBlock(x - dirx, y - diry);
		if (from.getInventory() != null && from.getInventory().size() != 0 && !to.isBlocksMovement()) {
			place = true;
			scheduleUpdate();
		}
		GameMaster.sendToAll("{action:activate,x:" + x + ",y:" + y + "}", true);
	}

	public void activateAbility(PlayerCharacter e) {
		if (Math.abs(e.getMousePosition().getX()) > Math.abs(e.getMousePosition().getY())) {
			dirx = (e.getMousePosition().getX() > 0 ? -1 : 1);
			diry = 0;
		} else {
			diry = (e.getMousePosition().getY() > 0 ? -1 : 1);
			dirx = 0;
		}
		e.updateInventory();
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
